package de.adito.aditoweb.obsoleteAccessors.impl.version;

import de.adito.aditoweb.obsoleteAccessors.spi.IParameterConverter;

/**
 * @author W.Glanzer, 04.09.2017
 */
class ObsoleteAccessorVersion extends AbstractAccessorVersion
{

  private final Class<? extends IParameterConverter> converter;

  public ObsoleteAccessorVersion(int pVersion, String pPkgName, String pId, Class<? extends IParameterConverter> pConverter, Class<?> pType)
  {
    super(pVersion, pPkgName, pId, pType);
    converter = pConverter;
  }

  public Class<? extends IParameterConverter> getParameterConverter()
  {
    return converter;
  }

}
