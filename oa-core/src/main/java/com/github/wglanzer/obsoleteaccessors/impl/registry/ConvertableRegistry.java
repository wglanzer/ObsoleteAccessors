package com.github.wglanzer.obsoleteaccessors.impl.registry;

import com.github.wglanzer.annosave.api.IAnnotationContainer;
import com.github.wglanzer.obsoleteaccessors.api.*;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.*;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.conversion.*;
import com.github.wglanzer.obsoleteaccessors.impl.version.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author W.Glanzer, 04.09.2017
 */
class ConvertableRegistry implements IConvertableRegistry
{

  private VersionRegistryTree tree = new VersionRegistryTree();

  protected ConvertableRegistry(IAnnotationContainer[] pContainers)
  {
    for (IAnnotationContainer rootContainer : pContainers)
    {
      Arrays.stream(rootContainer.getChildren())
          .filter(pChildContainer -> pChildContainer.hasAnnotation(ObsoleteVersion.class) || pChildContainer.hasAnnotation(ObsoleteVersions.class))
          .map(pChildContainer -> VersionFactory.createVersion(rootContainer, pChildContainer))
          .forEach(pVersionHierarchy -> {
            for (int j = 0; j < pVersionHierarchy.length; j++)
            {
              IAccessorVersion child = pVersionHierarchy[j];
              IAccessorVersion previous = j == 0 ? null : pVersionHierarchy[j - 1];
              tree.addVersion(rootContainer.getAnnotation(ObsoleteVersionContainer.class).getParameterValue("category", String.class), previous, child);
            }
          });
    }
  }

  @Nullable
  @Override
  public OAAccessor convert(@NotNull OAAccessor pAccessorToFind, @Nullable String pCategory) throws Exception
  {
    IAccessorVersion accessorVersionToFind = VersionFactory.createVersion(pAccessorToFind);

    VersionRegistryTree.VersionNode treeNode = tree.getVersion(pCategory, accessorVersionToFind);
    if (treeNode == null)
      return null;

    List<IAccessorVersion> versionHierarchy = new ArrayList<>(); // [actualAccessorToFindVersion, latestVersion] --- including outter versions
    VersionRegistryTree.VersionNode next = treeNode;
    while (next != null)
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

  @NotNull
  @Override
  public List<OAAccessor> findAccessors(@Nullable String pCategory, @NotNull String pPkgName, @NotNull String pIdentifier)
  {
    return tree.findVersions(pCategory, pPkgName, pIdentifier).stream()
        .map(VersionRegistryTree.VersionNode::getMyVersion)
        .map(pVersion -> {
          List<OAAttribute> attrs = pVersion.getAttributeDescriptions().stream()
              .map(pDescr -> new OAAttribute(pDescr.getType(), null))
              .collect(Collectors.toList());
          return new OAAccessor(pVersion.getPkgName(), pVersion.getId(), attrs, pVersion.getType());
        })
        .collect(Collectors.toList());
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
    catch (Exception e)
    {
      throw new AttributeConversionException("Conversion for parameters failed for accessor: \"" + pOldAccessor + "\"", e);
    }

    return new OAAccessor(pLatestVersion.getPkgName(), pLatestVersion.getId(), params, pLatestVersion.getType());
  }

}
