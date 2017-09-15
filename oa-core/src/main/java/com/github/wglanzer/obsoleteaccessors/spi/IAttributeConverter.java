package com.github.wglanzer.obsoleteaccessors.spi;

import com.github.wglanzer.obsoleteaccessors.api.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This converter can convert attributes from one version (ObsoleteVersion) to a newer one.
 *
 * @author W.Glanzer, 04.09.2017
 */
public interface IAttributeConverter
{

  /**
   * Converts an ordered list of attributes to a newer one.
   * The result list has to be conform with the next ObsoleteVersion-Annotation.
   *
   * @param pAttributes Attributes to be converted
   * @return attributes of the next version
   */
  @NotNull
  List<OAAttribute> convert(@NotNull List<OAAttribute> pAttributes) throws AttributeConversionException;

  /**
   * Default-Implementation with no conversion.
   * It just returns the original list back.
   */
  class DEFAULT implements IAttributeConverter
  {
    @NotNull
    @Override
    public List<OAAttribute> convert(@NotNull List<OAAttribute> pAttributes)
    {
      return pAttributes;
    }
  }

}
