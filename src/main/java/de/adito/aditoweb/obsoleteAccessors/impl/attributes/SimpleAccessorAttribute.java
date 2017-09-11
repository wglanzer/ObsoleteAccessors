package de.adito.aditoweb.obsoleteAccessors.impl.attributes;

/**
 * @author W.Glanzer, 11.09.2017
 */
public class SimpleAccessorAttribute<T> implements IAccessorAttribute<T>
{

  private final IAccessorAttributeDescription<T> description;
  private final T value;

  public SimpleAccessorAttribute(IAccessorAttributeDescription<T> pDescription, T pValue)
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
