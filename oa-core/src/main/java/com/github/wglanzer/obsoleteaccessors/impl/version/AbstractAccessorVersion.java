package com.github.wglanzer.obsoleteaccessors.impl.version;

import com.github.wglanzer.obsoleteaccessors.impl.attributes.IAccessorAttributeDescription;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.conversion.IAccessorAttributeConverter;

import java.util.*;

/**
 * @author W.Glanzer, 08.09.2017
 */
abstract class AbstractAccessorVersion implements IAccessorVersion
{
  private final String uid;
  private final int version;
  private final String pkgName;
  private final String id;
  private final Class<?> type;
  private final List<IAccessorAttributeDescription<?>> attributeDescriptions;

  public AbstractAccessorVersion(String pUID, int pVersion, String pPkgName, String pID, Class<?> pType, List<IAccessorAttributeDescription<?>> pAttributeDescriptions)
  {
    uid = pUID;
    version = pVersion;
    pkgName = pPkgName;
    id = pID;
    type = pType;
    attributeDescriptions = pAttributeDescriptions == null ? Collections.emptyList() : pAttributeDescriptions;
  }

  @Override
  public String getUID()
  {
    return uid;
  }

  @Override
  public int getBranch()
  {
    return 0; //0 = Default-Branch
  }

  @Override
  public int getVersion()
  {
    return version;
  }

  @Override
  public String getPkgName()
  {
    return pkgName;
  }

  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public Class<?> getType()
  {
    return type;
  }

  @Override
  public List<IAccessorAttributeDescription<?>> getAttributeDescriptions()
  {
    return attributeDescriptions;
  }

  @Override
  public IAccessorAttributeConverter getConverter()
  {
    return null;
  }

  @Override
  public boolean isLatestVersion()
  {
    return false;
  }

}
