package de.adito.aditoweb.obsoleteAccessors.impl.attributes;

import de.adito.aditoweb.obsoleteAccessors.api.OAAttribute;

/**
 * @author W.Glanzer, 08.09.2017
 */
public class SimpleAccessorAttributeDescription<T> implements IAccessorAttributeDescription<T>
{

  private final Class<T> type;

  public static IAccessorAttributeDescription<?> of(OAAttribute pAttribute)
  {
    return new SimpleAccessorAttributeDescription<>(pAttribute.getType());
  }

  private SimpleAccessorAttributeDescription(Class<T> pType)
  {
    type = pType;
  }

  @Override
  public Class<T> getType()
  {
    return type;
  }

}
