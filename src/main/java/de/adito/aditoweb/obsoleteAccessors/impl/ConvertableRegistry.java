package de.adito.aditoweb.obsoleteAccessors.impl;

import de.adito.aditoweb.obsoleteAccessors.api.*;
import de.adito.aditoweb.obsoleteAccessors.impl.version.*;
import de.adito.aditoweb.obsoleteAccessors.spi.*;
import de.adito.picoservice.IPicoRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.*;

/**
 * @author W.Glanzer, 04.09.2017
 */
class ConvertableRegistry
{

  private static ConvertableRegistry _INSTANCE;

  // category -> pkgname -> alte functionversions -> neuste functionversion
  private Map<String, Map<String, Map<IAccessorVersion[], IAccessorVersion>>> _CONVERSIONMAP = new HashMap<>();

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
      Stream<IAccessorVersion[]> methodVersions = Stream.of(pContainerClass.getDeclaredMethods())
          .filter(pMethod -> pMethod.isAnnotationPresent(ObsoleteVersions.class))
          .map(pMethod -> VersionFactory.createVersion(container, pMethod));
      Stream<IAccessorVersion[]> fieldVersions = Stream.of(pContainerClass.getDeclaredFields())
          .filter(pField -> pField.isAnnotationPresent(ObsoleteVersions.class))
          .map(pField -> VersionFactory.createVersion(container, pField));
      List<IAccessorVersion[]> versions = Stream.concat(methodVersions, fieldVersions).collect(Collectors.toList());

      String category = pContainer.category();
      String pkgName = pContainer.pkgName();
      Map<String, Map<IAccessorVersion[], IAccessorVersion>> pkgName2FunctionMap = _CONVERSIONMAP.computeIfAbsent(category, pCategory -> new HashMap<>());
      Map<IAccessorVersion[], IAccessorVersion> functionMap = pkgName2FunctionMap.computeIfAbsent(pkgName, pPkgname -> new HashMap<>());

      for (IAccessorVersion[] functionHistory : versions)
      {
        IAccessorVersion newestFunction = functionHistory[functionHistory.length - 1];
        functionMap.put(functionHistory, newestFunction);
      }
    });
  }

  @Nullable
  public Function find(String pCategory, String pPkgName, Function pFunctionToFind)
  {
    Map<String, Map<IAccessorVersion[], IAccessorVersion>> category = _CONVERSIONMAP.get(pCategory);
    if(category == null)
      throw new RuntimeException("Category not found");
    Map<IAccessorVersion[], IAccessorVersion> pkg = category.get(pPkgName);
    if(pkg == null)
      throw new RuntimeException("Package not found");

    IAccessorVersion accessor = VersionFactory.createVersion(pFunctionToFind);

    for (Map.Entry<IAccessorVersion[], IAccessorVersion> entry : pkg.entrySet())
    {
      for (IAccessorVersion obsoleteVersion : entry.getKey())
      {
        if(accessor.equalTo(obsoleteVersion))
          return _createFunction(entry.getValue(), pFunctionToFind);
      }
    }

    return null;
  }

  private Function _createFunction(IAccessorVersion pLatestVersion, Function pOldFunction)
  {
    //todo Converter
    List<Parameter> params = pLatestVersion.getAttributeDescriptions().stream()
        .map(pDescription -> new Parameter(pDescription.getType(), null))
        .collect(Collectors.toList());

    return new Function(pLatestVersion.getPkgName(), pLatestVersion.getId(), params, pLatestVersion.getType());
  }

}
