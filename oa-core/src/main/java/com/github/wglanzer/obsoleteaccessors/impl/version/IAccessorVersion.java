package com.github.wglanzer.obsoleteaccessors.impl.version;

import com.github.wglanzer.obsoleteaccessors.impl.attributes.IAccessorAttributeDescription;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.conversion.IAccessorAttributeConverter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author W.Glanzer, 08.09.2017
 */
public interface IAccessorVersion
{

  @Nullable
  String getUID();

  int getBranch();

  int getVersion();

  String getPkgName();

  String getId();

  Class<?> getType();

  List<IAccessorAttributeDescription<?>> getAttributeDescriptions();

  IAccessorAttributeConverter getConverter();

  boolean isLatestVersion();

  default boolean equalTo(IAccessorVersion pVersion)
  {
    if(pVersion == null)
      return true;

    if(System.identityHashCode(this) == System.identityHashCode(pVersion))
      return true;

    if(getUID() == null || pVersion.getUID() == null)
    {
      boolean equal = true;
      if(getVersion() != -1 && pVersion.getVersion() != -1)
        equal = Objects.equals(getVersion(), pVersion.getVersion());

      if(getBranch() != -1 && pVersion.getBranch() != -1)
        equal = equal && Objects.equals(getBranch(), pVersion.getBranch());

      equal = equal && Objects.equals(getPkgName(), pVersion.getPkgName());
      equal = equal && Objects.equals(getId(), pVersion.getId());
      equal = equal && Objects.equals(getType(), pVersion.getType());

      List<Class<?>> myDescr = getAttributeDescriptions().stream().map(IAccessorAttributeDescription::getType).collect(Collectors.toList());
      List<Class<?>> thatDescr = pVersion.getAttributeDescriptions().stream().map(IAccessorAttributeDescription::getType).collect(Collectors.toList());
      equal = equal && Objects.equals(myDescr, thatDescr);

      return equal;
    }
    else
      return getUID().equals(pVersion.getUID());
  }

}
