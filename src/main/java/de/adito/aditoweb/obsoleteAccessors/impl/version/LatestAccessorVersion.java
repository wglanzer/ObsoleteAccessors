package de.adito.aditoweb.obsoleteAccessors.impl.version;

/**
 * @author W.Glanzer, 07.09.2017
 */
class LatestAccessorVersion extends AbstractAccessorVersion
{

  public static final int LATEST_VERSION = Integer.MAX_VALUE;

  public LatestAccessorVersion(String pPkgName, String pId, Class<?> pReturnType)
  {
    super(LATEST_VERSION, pPkgName, pId, pReturnType);
  }

}
