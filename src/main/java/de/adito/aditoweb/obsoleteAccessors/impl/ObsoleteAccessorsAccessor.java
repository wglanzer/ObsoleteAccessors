package de.adito.aditoweb.obsoleteAccessors.impl;

import de.adito.aditoweb.obsoleteAccessors.api.OAAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * @author W.Glanzer, 04.09.2017
 */
public class ObsoleteAccessorsAccessor
{

  @Nullable
  public static OAAccessor convert(OAAccessor pOldAccessor, @Nullable String pCategory)
  {
    return ConvertableRegistry.getInstance().find(pCategory, pOldAccessor.getPackageName(), pOldAccessor);
  }

}
