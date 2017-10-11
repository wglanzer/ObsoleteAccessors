package com.github.wglanzer.obsoleteaccessors.api;

import com.google.common.base.Objects;
import org.jetbrains.annotations.*;

import java.util.*;

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
  private final Class<?> type;

  public OAAccessor(@NotNull String pPackageName, @NotNull String pIdentifier, @Nullable List<OAAttribute> pAttributes, @NotNull Class<?> pType)
  {
    packageName = pPackageName;
    identifier = pIdentifier;
    attributes = pAttributes == null ? Collections.emptyList() : pAttributes;
    type = pType;
  }

  public String getPackageName()
  {
    return packageName;
  }

  public String getIdentifier()
  {
    return identifier;
  }

  @NotNull
  public List<OAAttribute> getAttributes()
  {
    return attributes;
  }

  public Class<?> getType()
  {
    return type;
  }

  /**
   * @return returns <tt>true</tt> if this accessor represents one, that has no newer versions
   */
  public boolean isLatestAccessorVersion()
  {
    throw new UnsupportedOperationException("isLatestAccessorVersion() is not supported");
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || (getClass() != pO.getClass() &&
        (!getClass().isAssignableFrom(pO.getClass()) &&
            !pO.getClass().isAssignableFrom(getClass()))))
      return false;
    OAAccessor that = (OAAccessor) pO;
    return Objects.equal(packageName, that.packageName) &&
        Objects.equal(identifier, that.identifier) &&
        Objects.equal(attributes, that.attributes) &&
        Objects.equal(type, that.type);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(packageName, identifier, attributes, type);
  }

  @Override
  public String toString()
  {
    return "OAAccessor{" +
        "packageName='" + packageName + '\'' +
        ", identifier='" + identifier + '\'' +
        ", attributes=" + attributes +
        ", type=" + type +
        '}';
  }
}
