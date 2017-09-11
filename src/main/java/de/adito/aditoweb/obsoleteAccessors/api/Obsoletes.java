package de.adito.aditoweb.obsoleteAccessors.api;

import de.adito.aditoweb.obsoleteAccessors.impl.ObsoleteAccessorsAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Contains all API-Methods for ObsoleteAccessors
 *
 * @author W.Glanzer, 04.09.2017
 */
public abstract class Obsoletes
{

  private Obsoletes()
  {
  }

  /**
   * Converts an old OAAcessor to the newest version.
   * If no ObsoleteVersion is found in this category, <tt>null</tt> will be returned.
   *
   * @param pOldAccessor Accessor which should be converted to newest version
   * @param pCategory    Category, or <tt>null</tt> if no filtering should be done
   * @return the newest Accessor available or <tt>null</tt>
   */
  @Nullable
  public static OAAccessor convert(OAAccessor pOldAccessor, @Nullable String pCategory)
  {
    return ObsoleteAccessorsAccessor.convert(pOldAccessor, pCategory);
  }

}
