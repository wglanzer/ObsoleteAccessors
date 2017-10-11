package com.github.wglanzer.obsoleteaccessors.impl.registry;

import com.github.wglanzer.obsoleteaccessors.api.OAAccessor;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * @author W.Glanzer, 20.09.2017
 */
public interface IConvertableRegistry
{

  @Nullable
  OAAccessor convert(@NotNull OAAccessor pAccessorToFind, @Nullable String pCategory) throws Exception;

  @NotNull
  List<OAAccessor> findAccessors(@Nullable String pCategory, @NotNull String pPkgName, @NotNull String pIdentifier);

  @NotNull
  Multimap<String, OAAccessor> getPackages(@Nullable String pCategory);

}
