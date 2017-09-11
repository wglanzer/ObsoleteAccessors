package de.adito.aditoweb.obsoleteAccessors.impl.attrDescr;

/**
 * @author W.Glanzer, 08.09.2017
 */
public class ImmutableAccessorAttributeDescription<T> implements IAccessorAttributeDescription<T>
{

  private final Class<T> type;

  public ImmutableAccessorAttributeDescription(Class<T> pType)
  {
    type = pType;
  }

  @Override
  public Class<T> getType()
  {
    return type;
  }

}
