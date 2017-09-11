package de.adito.aditoweb.obsoleteAccessors.impl.attrConv;

import de.adito.aditoweb.obsoleteAccessors.api.AttributeConversionException;

import java.util.List;

/**
 * @author W.Glanzer, 11.09.2017
 */
public interface IAccessorAttributeConverter
{

  List<IAccessorAttribute> convert(List<IAccessorAttribute> pAttributes) throws AttributeConversionException;

}
