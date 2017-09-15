package com.github.wglanzer.obsoleteaccessors.impl.attributes.conversion;

import com.github.wglanzer.obsoleteaccessors.api.AttributeConversionException;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.IAccessorAttribute;

import java.util.List;

/**
 * @author W.Glanzer, 11.09.2017
 */
public interface IAccessorAttributeConverter
{

  List<IAccessorAttribute> convert(List<IAccessorAttribute> pAttributes) throws AttributeConversionException;

}
