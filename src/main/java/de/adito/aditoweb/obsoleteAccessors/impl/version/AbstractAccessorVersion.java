package de.adito.aditoweb.obsoleteAccessors.impl.version;

/**
 * @author W.Glanzer, 08.09.2017
 */
abstract class AbstractAccessorVersion implements IAccessorVersion
{

  private final int version;
  private final String pkgName;
  private final String id;
  private final Class<?> type;

  public AbstractAccessorVersion(int pVersion, String pPkgName, String pID, Class<?> pType)
  {
    version = pVersion;
    pkgName = pPkgName;
    id = pID;
    type = pType;
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

}
