package de.adito.aditoweb.obsoleteAccessors.impl.attributes.conversion;

import de.adito.aditoweb.obsoleteAccessors.api.AttributeConversionException;
import de.adito.aditoweb.obsoleteAccessors.impl.attributes.IAccessorAttribute;

import java.util.List;

/**
 * @author W.Glanzer, 11.09.2017
 */
public interface IAccessorAttributeConverter
{

  List<IAccessorAttribute> convert(List<IAccessorAttribute> pAttributes) throws AttributeConversionException;

}
