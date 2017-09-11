package de.adito.aditoweb.obsoleteAccessors.api;

import java.util.List;

/**
 * @author W.Glanzer, 04.09.2017
 */
public class Function
{

  private String packageName;
  private String identifier;
  private List<Parameter> parameters;
  private Class<?> returnType;

  public Function(String pPackageName, String pIdentifier, List<Parameter> pParameters, Class<?> pReturnType)
  {
    packageName = pPackageName;
    identifier = pIdentifier;
    parameters = pParameters;
    returnType = pReturnType;
  }

  public String getPackageName()
  {
    return packageName;
  }

  public String getIdentifier()
  {
    return identifier;
  }

  public List<Parameter> getParameters()
  {
    return parameters;
  }

  public Class<?> getReturnType()
  {
    return returnType;
  }

  @Override
  public String toString()
  {
    return "Function{" +
        "packageName='" + packageName + '\'' +
        ", identifier='" + identifier + '\'' +
        ", parameters=" + parameters +
        ", returnType=" + returnType +
        '}';
  }
}
