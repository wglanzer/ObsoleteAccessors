package de.adito.aditoweb.obsoleteAccessors.impl;

import de.adito.aditoweb.obsoleteAccessors.spi.IParameterConverter;

import java.util.Objects;

/**
 * @author W.Glanzer, 04.09.2017
 */
class AccessorVersion
{

  private int version;
  private String pkgName;
  private String id;
  private Class<? extends IParameterConverter> converter;
  private Class<?> returnType;

  public AccessorVersion(int pVersion, String pPkgName, String pId, Class<? extends IParameterConverter> pConverter, Class<?> pReturnType)
  {
    version = pVersion;
    pkgName = pPkgName;
    id = pId;
    converter = pConverter;
    returnType = pReturnType;
  }

  public int getVersion()
  {
    return version;
  }

  public String getPkgName()
  {
    return pkgName;
  }

  public String getId()
  {
    return id;
  }

  public Class<? extends IParameterConverter> getConverter()
  {
    return converter;
  }

  public Class<?> getReturnType()
  {
    return returnType;
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || getClass() != pO.getClass()) return false;
    AccessorVersion that = (AccessorVersion) pO;
    return (version == -1 || that.version == -1 || version == that.version) &&
        Objects.equals(pkgName, that.pkgName) &&
        Objects.equals(id, that.id) &&
        Objects.equals(returnType, that.returnType);
  }

  @Override
  public int hashCode()
  {
    if(version == -1)
      return Objects.hash(pkgName, id, returnType);
    return Objects.hash(version, pkgName, id, returnType);
  }

}
