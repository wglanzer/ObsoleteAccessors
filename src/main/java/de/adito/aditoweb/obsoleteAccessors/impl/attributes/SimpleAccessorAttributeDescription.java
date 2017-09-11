package de.adito.aditoweb.obsoleteAccessors.impl.attributes;

/**
 * @author W.Glanzer, 08.09.2017
 */
public class SimpleAccessorAttributeDescription<T> implements IAccessorAttributeDescription<T>
{

  private final Class<T> type;

  public SimpleAccessorAttributeDescription(Class<T> pType)
  {
    type = pType;
  }

  @Override
  public Class<T> getType()
  {
    return type;
  }

}
