package de.adito.aditoweb.obsoleteAccessors.api;

import de.adito.aditoweb.obsoleteAccessors.impl.FunctionConverterAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * @author W.Glanzer, 04.09.2017
 */
public abstract class Obsoletes
{

  private Obsoletes()
  {
  }

  @Nullable
  public static Function convert(Function pOldFunction, @Nullable String pCategory)
  {
    return FunctionConverterAccessor.getInstance().convert(pOldFunction, pCategory);
  }

}
