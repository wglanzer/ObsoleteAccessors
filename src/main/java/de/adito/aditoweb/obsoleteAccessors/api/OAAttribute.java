package de.adito.aditoweb.obsoleteAccessors.api;

import java.util.Objects;

/**
 * An OAAttribute is mainly used for parameters of methods inside OAAccessors
 *
 * @author W.Glanzer, 04.09.2017
 */
public class OAAttribute
{

  private final Class<?> type;
  private final Object value;

  public OAAttribute(Class<?> pType, Object pValue)
  {
    type = pType;
    value = pValue;
  }

  public Class<?> getType()
  {
    return type;
  }

  public Object getValue()
  {
    return value;
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || getClass() != pO.getClass()) return false;
    OAAttribute attribute = (OAAttribute) pO;
    return Objects.equals(type, attribute.type) &&
        Objects.equals(value, attribute.value);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(type, value);
  }

  @Override
  public String toString()
  {
    return "OAAttribute{" +
        "type=" + type +
        ", value=" + value +
        '}';
  }

}
