package de.adito.aditoweb.obsoleteAccessors.spi;

import de.adito.aditoweb.obsoleteAccessors.api.OAAttribute;

import java.util.List;

/**
 * This converter can convert attributes from one version (ObsoleteVersion) to a newer one.
 *
 * @author W.Glanzer, 04.09.2017
 */
public interface IAttributeConverter
{

  /**
   * Converts an ordered list of attributes to a newer one.
   * The result list has to be conform with the next ObsoleteVersion-Annotation.
   *
   * @param pAttributes Attributes to be converted
   * @return attributes of the next version
   */
  List<OAAttribute> convert(List<OAAttribute> pAttributes);  //todo ex if not possible

  /**
   * Default-Implementation with no conversion.
   * It just returns the original list back.
   */
  class DEFAULT implements IAttributeConverter
  {
    @Override
    public List<OAAttribute> convert(List<OAAttribute> pAttributes)
    {
      return pAttributes;
    }
  }

}
