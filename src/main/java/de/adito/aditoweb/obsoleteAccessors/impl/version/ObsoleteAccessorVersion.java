package de.adito.aditoweb.obsoleteAccessors.impl.version;

import de.adito.aditoweb.obsoleteAccessors.impl.attrDescr.IAccessorAttributeDescription;
import de.adito.aditoweb.obsoleteAccessors.spi.IParameterConverter;

import java.util.List;

/**
 * @author W.Glanzer, 04.09.2017
 */
class ObsoleteAccessorVersion extends AbstractAccessorVersion
{

  private final Class<? extends IParameterConverter> converter;

  public ObsoleteAccessorVersion(int pVersion, String pPkgName, String pId, Class<? extends IParameterConverter> pConverter, Class<?> pType, List<IAccessorAttributeDescription<?>> pAttributeDescriptions)
  {
    super(pVersion, pPkgName, pId, pType, pAttributeDescriptions);
    converter = pConverter;
  }

}
