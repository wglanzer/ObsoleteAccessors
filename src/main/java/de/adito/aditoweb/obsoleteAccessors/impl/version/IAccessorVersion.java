package de.adito.aditoweb.obsoleteAccessors.impl.version;

import java.util.Objects;

/**
 * @author W.Glanzer, 08.09.2017
 */
public interface IAccessorVersion
{

  int getVersion();

  String getPkgName();

  String getId();

  Class<?> getType();

  default boolean equalTo(IAccessorVersion pVersion)
  {
    if(pVersion == null)
      return true;

    boolean equal = true;
    if(getVersion() != -1 && pVersion.getVersion() != -1)
      equal = Objects.equals(getVersion(), pVersion.getVersion());

    equal = equal && Objects.equals(getPkgName(), pVersion.getPkgName());
    equal = equal && Objects.equals(getId(), pVersion.getId());
    equal = equal && Objects.equals(getType(), pVersion.getType());
    return equal;
  }

}
