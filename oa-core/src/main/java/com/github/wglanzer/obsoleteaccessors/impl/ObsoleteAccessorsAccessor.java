package com.github.wglanzer.obsoleteaccessors.impl;

import com.github.wglanzer.obsoleteaccessors.api.OAAccessor;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author W.Glanzer, 04.09.2017
 */
public class ObsoleteAccessorsAccessor
{

  @Nullable
  public static OAAccessor convert(OAAccessor pOldAccessor, @Nullable String pCategory) throws Exception
  {
    return ConvertableRegistry.getInstance().find(pCategory, pOldAccessor);
  }

  @Nullable
  public static OAAccessor convert(OAAccessor pOldAccessor, @Nullable String pCategory, File pRegistry) throws Exception
  {
    return ConvertableRegistry.getInstance().find(pCategory, pOldAccessor);
  }

}
