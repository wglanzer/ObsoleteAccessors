package de.adito.aditoweb.obsoleteAccessors.impl.attrConv;

import de.adito.aditoweb.obsoleteAccessors.impl.attrDescr.IAccessorAttributeDescription;

/**
 * @author W.Glanzer, 11.09.2017
 */
public interface IAccessorAttribute<T>
{

  IAccessorAttributeDescription<T> getDescription();

  T getValue();

}
