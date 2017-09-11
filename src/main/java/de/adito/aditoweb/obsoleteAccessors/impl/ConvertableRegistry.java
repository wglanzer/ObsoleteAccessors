package de.adito.aditoweb.obsoleteAccessors.impl;

import de.adito.aditoweb.obsoleteAccessors.api.*;
import de.adito.aditoweb.obsoleteAccessors.impl.attrConv.*;
import de.adito.aditoweb.obsoleteAccessors.impl.attrDescr.ImmutableAccessorAttributeDescription;
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
  public OAAccessor find(String pCategory, String pPkgName, OAAccessor pAccessorToFind)
  {
    Map<String, Map<IAccessorVersion[], IAccessorVersion>> category = _CONVERSIONMAP.get(pCategory);
    if(category == null)
      throw new RuntimeException("Category not found");
    Map<IAccessorVersion[], IAccessorVersion> pkg = category.get(pPkgName);
    if(pkg == null)
      throw new RuntimeException("Package not found");

    IAccessorVersion accessor = VersionFactory.createVersion(pAccessorToFind);

    for (Map.Entry<IAccessorVersion[], IAccessorVersion> entry : pkg.entrySet())
    {
      IAccessorVersion[] key = entry.getKey();
      for (int i = 0; i < key.length; i++)
      {
        IAccessorVersion obsoleteVersion = key[i];
        if (accessor.equalTo(obsoleteVersion))
        {
          IAccessorVersion[] allVersionsAfter = new IAccessorVersion[key.length - i - 1];
          System.arraycopy(key, i, allVersionsAfter, 0, key.length - i - 1);
          ProxyAttributeConverter converter = new ProxyAttributeConverter(Arrays.stream(allVersionsAfter)
                                                                                            .map(IAccessorVersion::getConverter)
                                                                                            .toArray(IAccessorAttributeConverter[]::new));
          return _createFunction(entry.getValue(), converter, pAccessorToFind);
        }
      }
    }

    return null;
  }

  private OAAccessor _createFunction(IAccessorVersion pLatestVersion, IAccessorAttributeConverter pAttributeConverter, OAAccessor pOldAccessor)
  {
    List<OAAttribute> attributes = pOldAccessor.getAttributes();
    List<IAccessorAttribute> oldParameters = attributes != null ? attributes.stream()
        .map(pParameter -> new SimpleAccessorAttribute(new ImmutableAccessorAttributeDescription(pParameter.getType()), pParameter.getValue()))
        .collect(Collectors.toList()) : Collections.emptyList();
    List<IAccessorAttribute> convertedParameters = pAttributeConverter.convert(oldParameters);
    List<OAAttribute> params = convertedParameters.stream()
        .map(pAttribute -> new OAAttribute(pAttribute.getDescription().getType(), pAttribute.getValue()))
        .collect(Collectors.toList());

    return new OAAccessor(pLatestVersion.getPkgName(), pLatestVersion.getId(), params, pLatestVersion.getType());
  }

}
