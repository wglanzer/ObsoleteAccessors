package de.adito.aditoweb.obsoleteAccessors.impl.attributes;

/**
 * @author W.Glanzer, 11.09.2017
 */
public interface IAccessorAttribute<T>
{

  IAccessorAttributeDescription<T> getDescription();

  T getValue();

}
