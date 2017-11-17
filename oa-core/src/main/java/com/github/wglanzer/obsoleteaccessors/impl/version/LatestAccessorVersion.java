package com.github.wglanzer.obsoleteaccessors.impl.version;

import com.github.wglanzer.obsoleteaccessors.impl.attributes.IAccessorAttributeDescription;

import java.util.List;

/**
 * @author W.Glanzer, 07.09.2017
 */
class LatestAccessorVersion extends AbstractAccessorVersion
{

  public static final int LATEST_VERSION = Integer.MAX_VALUE;

  public LatestAccessorVersion(String pUID, String pPkgName, String pId, Class<?> pReturnType, List<IAccessorAttributeDescription<?>> pAttributeDescriptions)
  {
    super(pUID, LATEST_VERSION, pPkgName, pId, pReturnType, pAttributeDescriptions);
  }

  @Override
  public boolean isLatestVersion()
  {
    return true;
  }

}
