package com.github.wglanzer.obsoleteaccessors.impl;

import com.github.wglanzer.obsoleteaccessors.api.*;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.*;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.conversion.*;
import com.github.wglanzer.obsoleteaccessors.impl.version.*;
import com.github.wglanzer.obsoleteaccessors.spi.*;
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

  private VersionRegistryTree tree = new VersionRegistryTree();

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

      for (IAccessorVersion[] functionHistory : versions)
      {
        for (int i = 0; i < functionHistory.length; i++)
        {
          IAccessorVersion version = functionHistory[i];
          IAccessorVersion previous = i == 0 ? null : functionHistory[i - 1];
          tree.addVersion(category, previous, version);
        }
      }
    });
  }

  @Nullable
  public OAAccessor find(String pCategory, OAAccessor pAccessorToFind) throws Exception
  {
    IAccessorVersion accessorVersionToFind = VersionFactory.createVersion(pAccessorToFind);

    VersionRegistryTree.VersionNode treeNode = tree.getVersion(pCategory, accessorVersionToFind);
    if(treeNode == null)
      return null;

    List<IAccessorVersion> versionHierarchy = new ArrayList<>(); // [actualAccessorToFindVersion, latestVersion] --- including outter versions
    VersionRegistryTree.VersionNode next = treeNode;
    while(next != null)
    {
      versionHierarchy.add(next.getMyVersion());
      next = next.getNewerVersion();
    }

    IAccessorAttributeConverter[] converters = versionHierarchy.stream()
        .map(IAccessorVersion::getConverter)
        .filter(Objects::nonNull)
        .toArray(IAccessorAttributeConverter[]::new);
    ProxyAttributeConverter converter = new ProxyAttributeConverter(converters);
    return _createFunction(versionHierarchy.get(versionHierarchy.size() - 1), converter, pAccessorToFind);
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
