package com.github.wglanzer.obsoleteaccessors.impl.registry;

import com.github.wglanzer.obsoleteaccessors.api.OAAccessor;
import org.jetbrains.annotations.*;

/**
 * @author W.Glanzer, 20.09.2017
 */
public interface IConvertableRegistry
{

  @Nullable
  OAAccessor find(@NotNull OAAccessor pAccessorToFind, @Nullable String pCategory) throws Exception;

}
