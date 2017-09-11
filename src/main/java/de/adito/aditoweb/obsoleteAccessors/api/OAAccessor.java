package de.adito.aditoweb.obsoleteAccessors.api;

import java.util.List;

/**
 * An OAAccessor represents a method/field annotated with @ObsoleteVersion
 *
 * @author W.Glanzer, 04.09.2017
 */
public class OAAccessor
{

  private final String packageName;
  private final String identifier;
  private final List<OAAttribute> attributes;
  private final Class<?> returnType;

  public OAAccessor(String pPackageName, String pIdentifier, List<OAAttribute> pAttributes, Class<?> pReturnType)
  {
    packageName = pPackageName;
    identifier = pIdentifier;
    attributes = pAttributes;
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

  public List<OAAttribute> getAttributes()
  {
    return attributes;
  }

  public Class<?> getReturnType()
  {
    return returnType;
  }

  @Override
  public String toString()
  {
    return "OAAccessor{" +
        "packageName='" + packageName + '\'' +
        ", identifier='" + identifier + '\'' +
        ", attributes=" + attributes +
        ", returnType=" + returnType +
        '}';
  }
}
