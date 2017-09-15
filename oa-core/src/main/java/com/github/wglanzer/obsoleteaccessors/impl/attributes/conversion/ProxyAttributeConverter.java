package com.github.wglanzer.obsoleteaccessors.impl.attributes.conversion;

import com.github.wglanzer.obsoleteaccessors.api.AttributeConversionException;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.IAccessorAttribute;

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
      pAttributes = converter.convert(pAttributes);
    return pAttributes;
  }

}
