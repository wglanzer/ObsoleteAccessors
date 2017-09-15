package com.github.wglanzer.obsoleteaccessors.impl.attributes;

import com.github.wglanzer.obsoleteaccessors.api.OAAttribute;

/**
 * @author W.Glanzer, 11.09.2017
 */
public class SimpleAccessorAttribute<T> implements IAccessorAttribute<T>
{

  private final IAccessorAttributeDescription<T> description;
  private final T value;

  public static IAccessorAttribute<?> of(OAAttribute pAttribute)
  {
    return new SimpleAccessorAttribute(SimpleAccessorAttributeDescription.of(pAttribute), pAttribute.getValue());
  }

  private SimpleAccessorAttribute(IAccessorAttributeDescription<T> pDescription, T pValue)
  {
    description = pDescription;
    value = pValue;
  }

  @Override
  public IAccessorAttributeDescription<T> getDescription()
  {
    return description;
  }

  @Override
  public T getValue()
  {
    return value;
  }

}
