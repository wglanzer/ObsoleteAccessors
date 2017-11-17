package com.github.wglanzer.obsoleteaccessors.impl;

import com.github.wglanzer.obsoleteaccessors.api.*;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * @author W.Glanzer, 07.10.2017
 */
public class InternalAccessor extends OAAccessor
{

  private final boolean isLatest;

  public InternalAccessor(@Nullable String pUUID, @NotNull String pPackageName, @NotNull String pIdentifier, @Nullable List<OAAttribute> pAttributes, @NotNull Class<?> pType, boolean pIsLatest)
  {
    super(pUUID, pPackageName, pIdentifier, pAttributes, pType);
    isLatest = pIsLatest;
  }

  @Override
  public boolean isLatestAccessorVersion()
  {
    return isLatest;
  }

}
