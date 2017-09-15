package com.github.wglanzer.obsoleteaccessors.impl;

import com.github.wglanzer.obsoleteaccessors.impl.version.IAccessorVersion;
import com.google.common.collect.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This class contains the inner structure for the ConvertableRegistry.
 * It consists of two things: A tree (grouped by category and package)
 * and a linkage between all leafs this tree contains.
 *
 * @author W.Glanzer, 12.09.2017
 */
class VersionRegistryTree
{

  private HashMap<String, Multimap<String, VersionNode>> tree = new HashMap<>();

  public VersionNode getVersion(String pCategory, IAccessorVersion pVersion)
  {
    Multimap<String, VersionNode> categoryMap = tree.get(pCategory);
    if(categoryMap == null)
      throw new RuntimeException("Category not found (\"" + pCategory + "\")");
    Collection<VersionNode> pkg = categoryMap.get(pVersion.getPkgName());
    if(pkg == null)
      throw new RuntimeException("Package not found (\"" + pVersion.getPkgName() + "\")");
    return pkg.stream()
        .filter(pNode -> pVersion.equalTo(pNode.getMyVersion()))
        .findFirst().orElse(null);
  }

  public void addVersion(String pCategory, @Nullable IAccessorVersion pPreviousVersion, IAccessorVersion pVersion)
  {
    String pkgName = pVersion.getPkgName();

    Multimap<String, VersionNode> package2Nodes = tree.computeIfAbsent(pCategory, pCat -> ArrayListMultimap.create());
    VersionNode actualNode = new VersionNode(pVersion);
    package2Nodes.put(pkgName, actualNode);

    if(pPreviousVersion != null)
    {
      VersionNode previousNode = getVersion(pCategory, pPreviousVersion);
      Objects.requireNonNull(previousNode);
      previousNode._setNewer(actualNode);
      actualNode._setOlder(previousNode);
    }
  }

  public static class VersionNode
  {
    private VersionNode newer;
    private VersionNode older;
    private final IAccessorVersion version;

    public VersionNode(IAccessorVersion pVersion)
    {
      version = pVersion;
    }

    public IAccessorVersion getMyVersion()
    {
      return version;
    }

    public VersionNode getNewerVersion()
    {
      return newer;
    }

    public VersionNode getOlderVersion()
    {
      return older;
    }

    private void _setNewer(VersionNode pNewer)
    {
      newer = pNewer;
    }

    private void _setOlder(VersionNode pOlder)
    {
      older = pOlder;
    }
  }

}
