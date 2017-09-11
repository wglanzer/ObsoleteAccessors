package de.adito.aditoweb.obsoleteAccessors.impl;

import de.adito.aditoweb.obsoleteAccessors.api.*;
import de.adito.aditoweb.obsoleteAccessors.impl.attributes.*;
import de.adito.aditoweb.obsoleteAccessors.impl.attributes.conversion.*;
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
  public OAAccessor find(String pCategory, String pPkgName, OAAccessor pAccessorToFind) throws Exception
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
          IAccessorAttributeConverter[] converters = Arrays.stream(key, i, key.length - 1)
              .filter(Objects::nonNull)
              .map(IAccessorVersion::getConverter)
              .toArray(IAccessorAttributeConverter[]::new);
          ProxyAttributeConverter converter = new ProxyAttributeConverter(converters);
          return _createFunction(entry.getValue(), converter, pAccessorToFind);
        }
      }
    }

    return null;
  }

  private OAAccessor _createFunction(IAccessorVersion pLatestVersion, IAccessorAttributeConverter pAttributeConverter, OAAccessor pOldAccessor) throws AttributeConversionException
  {
    List<OAAttribute> params;

    try
    {
      List<OAAttribute> attributes = pOldAccessor.getAttributes();
      List<IAccessorAttribute> oldParameters = attributes.stream()
                .map(SimpleAccessorAttribute::of)
                .collect(Collectors.toList());
      List<IAccessorAttribute> convertedParameters = pAttributeConverter.convert(oldParameters);
      params = convertedParameters.stream()
          .map(pAttribute -> new OAAttribute(pAttribute.getDescription().getType(), pAttribute.getValue()))
          .collect(Collectors.toList());
    }
    catch(Exception e)
    {
      throw new AttributeConversionException("Conversion for parameters failed for accessor: \"" + pOldAccessor + "\"", e);
    }

    return new OAAccessor(pLatestVersion.getPkgName(), pLatestVersion.getId(), params, pLatestVersion.getType());
  }

}
