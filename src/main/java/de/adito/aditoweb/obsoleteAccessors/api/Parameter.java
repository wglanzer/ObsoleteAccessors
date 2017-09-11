package de.adito.aditoweb.obsoleteAccessors.api;

import java.util.Objects;

/**
 * @author W.Glanzer, 04.09.2017
 */
public class Parameter
{

  private Class<?> type;
  private Object value;

  public Parameter(Class<?> pType, Object pValue)
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
    Parameter parameter = (Parameter) pO;
    return Objects.equals(type, parameter.type) &&
        Objects.equals(value, parameter.value);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(type, value);
  }

}
