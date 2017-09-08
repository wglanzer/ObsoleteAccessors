package de.adito.aditoweb.obsoleteAccessors.impl;

import de.adito.aditoweb.obsoleteAccessors.api.Function;
import de.adito.aditoweb.obsoleteAccessors.spi.*;
import de.adito.picoservice.IPicoRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

/**
 * @author W.Glanzer, 04.09.2017
 */
public class ConvertableRegistry
{

  private static ConvertableRegistry _INSTANCE;

  // category -> pkgname -> alte functionversions -> neuste functionversion
  private Map<String, Map<String, Map<AccessorVersion[], AccessorVersion>>> _CONVERSIONMAP = new HashMap<>();

  public static ConvertableRegistry getInstance()
  {
    if(_INSTANCE == null)
      _INSTANCE = new ConvertableRegistry();
    return _INSTANCE;
  }

  public ConvertableRegistry()
  {
    Map<Class<?>, ObsoleteVersionContainer> obsoleteVersionContainers = IPicoRegistry.INSTANCE.find(Object.class, ObsoleteVersionContainer.class);
    obsoleteVersionContainers.forEach((pContainerClass, pContainer) -> {
      ObsoleteVersionContainer container = pContainerClass.getDeclaredAnnotation(ObsoleteVersionContainer.class);
      Stream<AccessorVersion[]> methodVersions = Stream.of(pContainerClass.getDeclaredMethods())
          .filter(pMethod -> pMethod.isAnnotationPresent(ObsoleteVersions.class))
          .map(pMethod -> _createVersion(container, pMethod));
      Stream<AccessorVersion[]> fieldVersions = Stream.of(pContainerClass.getDeclaredFields())
          .filter(pField -> pField.isAnnotationPresent(ObsoleteVersions.class))
          .map(pField -> _createVersion(container, pField));
      List<AccessorVersion[]> versions = Stream.concat(methodVersions, fieldVersions).collect(Collectors.toList());

      String category = pContainer.category();
      String pkgName = pContainer.pkgName();
      Map<String, Map<AccessorVersion[], AccessorVersion>> pkgName2FunctionMap = _CONVERSIONMAP.computeIfAbsent(category, pCategory -> new HashMap<>());
      Map<AccessorVersion[], AccessorVersion> functionMap = pkgName2FunctionMap.computeIfAbsent(pkgName, pPkgname -> new HashMap<>());

      for (AccessorVersion[] functionHistory : versions)
      {
        AccessorVersion newestFunction = functionHistory[functionHistory.length - 1];
        functionMap.put(functionHistory, newestFunction);
      }
    });
  }

  @Nullable
  public Function find(String pCategory, String pPkgName, Function pAccessorVersion)
  {
    Map<String, Map<AccessorVersion[], AccessorVersion>> category = _CONVERSIONMAP.get(pCategory);
    if(category == null)
      throw new RuntimeException("Category not found");
    Map<AccessorVersion[], AccessorVersion> pkg = category.get(pPkgName);
    if(pkg == null)
      throw new RuntimeException("Package not found");

    AccessorVersion accessor = _createVersion(pAccessorVersion);

    for (Map.Entry<AccessorVersion[], AccessorVersion> entry : pkg.entrySet())
    {
      for (AccessorVersion obsoleteVersion : entry.getKey())
      {
        if(Objects.equals(accessor, obsoleteVersion))
          return _createFunction(entry.getValue());
      }
    }

    return null;
  }

  private AccessorVersion[] _createVersion(ObsoleteVersionContainer pContainer, Method pReflMethod)
  {
    AccessorVersion[] obsoleteVersions = _createVersion(pReflMethod.getDeclaredAnnotations());
    LatestAccessorVersion latest = new LatestAccessorVersion(pContainer.pkgName(), pReflMethod.getName(), pReflMethod.getReturnType());
    AccessorVersion[] versions = new AccessorVersion[obsoleteVersions.length + 1];
    System.arraycopy(obsoleteVersions, 0, versions, 0, obsoleteVersions.length);
    versions[versions.length - 1] = latest;
    return versions;
  }

  private AccessorVersion[] _createVersion(ObsoleteVersionContainer pContainer, Field pReflField)
  {
    AccessorVersion[] obsoleteVersions = _createVersion(pReflField.getDeclaredAnnotations());
    LatestAccessorVersion latest = new LatestAccessorVersion(pContainer.pkgName(), pReflField.getName(), pReflField.getType());
    AccessorVersion[] versions = new AccessorVersion[obsoleteVersions.length + 1];
    System.arraycopy(obsoleteVersions, 0, versions, 0, obsoleteVersions.length);
    versions[versions.length - 1] = latest;
    return versions;
  }

  private AccessorVersion[] _createVersion(Annotation[] pAnnotations)
  {
    ArrayList<ObsoleteVersion> obsoleteVersions = new ArrayList<>();
    for (Annotation annotation : pAnnotations)
    {
      if(annotation instanceof ObsoleteVersions)
        obsoleteVersions.addAll(Arrays.asList(((ObsoleteVersions) annotation).value()));
      else if(annotation instanceof ObsoleteVersion)
        obsoleteVersions.add((ObsoleteVersion) annotation);
    }

    return obsoleteVersions.stream()
        .sorted(Comparator.comparingInt(ObsoleteVersion::version))
        .map(pVersion -> new AccessorVersion(pVersion.version(), pVersion.pkgName(), pVersion.id(), pVersion.converter(), pVersion.returnType()))
        .toArray(AccessorVersion[]::new);
  }

  private AccessorVersion _createVersion(Function pFunction)
  {
    return new AccessorVersion(-1, pFunction.getPackageName(), pFunction.getIdentifier(), null, pFunction.getReturnType());
  }

  private Function _createFunction(AccessorVersion pVersion)
  {
    return new Function(pVersion.getPkgName(), pVersion.getId(), null, pVersion.getReturnType());
  }

}
