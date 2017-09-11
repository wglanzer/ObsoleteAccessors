package de.adito.aditoweb.obsoleteAccessors.impl.attributes.conversion;

import de.adito.aditoweb.obsoleteAccessors.api.AttributeConversionException;
import de.adito.aditoweb.obsoleteAccessors.impl.attributes.IAccessorAttribute;

import java.util.List;

/**
 * @author W.Glanzer, 11.09.2017
 */
public class ProxyAttributeConverter implements IAccessorAttributeConverter
{

  private final IAccessorAttributeConverter[] converters;

  public ProxyAttributeConverter(IAccessorAttributeConverter... pConverters)
  {
    converters = pConverters;
  }

  @Override
  public List<IAccessorAttribute> convert(List<IAccessorAttribute> pAttributes) throws AttributeConversionException
  {
    for (IAccessorAttributeConverter converter : converters)
    {
      if(converter != null)
        pAttributes = converter.convert(pAttributes);
    }
    return pAttributes;
  }

}
