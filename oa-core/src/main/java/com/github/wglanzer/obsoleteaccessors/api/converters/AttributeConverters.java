package com.github.wglanzer.obsoleteaccessors.api.converters;

import com.github.wglanzer.obsoleteaccessors.api.*;
import com.github.wglanzer.obsoleteaccessors.spi.IAttributeConverter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author W.Glanzer, 05.10.2017
 */
public abstract class AttributeConverters
{

  private AttributeConverters()
  {
  }

  /**
   * The OrderConverter changes the order of the given OAAttributes.
   * The new order is specified in converterAttributes with the following syntax:
   * "oldIndex->newIndex" (Example: "0->1", it moves attribute on index 0 to index 1).
   * All converterAttributes are executed in order and applied one after another
   */
  public static class OrderConverter implements IAttributeConverter
  {
    private final String[] attributes;

    public OrderConverter(@NotNull String[] pAttributes)
    {
      attributes = pAttributes;
    }

    @NotNull
    @Override
    public List<OAAttribute> convert(@NotNull List<OAAttribute> pAttributes, @NotNull OAAccessor pNextAccessor) throws AttributeConversionException
    {
      Map<Integer, Integer> mapping = _getMappingOldIndicesToNew(attributes);
      ArrayList<OAAttribute> attributesCopy = new ArrayList<>(pAttributes);
      mapping.forEach((pFrom, pTo) -> attributesCopy.add(pTo, attributesCopy.remove((int) pFrom)));
      return attributesCopy;
    }

    @NotNull
    private Map<Integer, Integer> _getMappingOldIndicesToNew(String[] pAttributes) throws AttributeConversionException
    {
      HashMap<Integer, Integer> result = new HashMap<>();
      for (String attribute : pAttributes)
      {
        String[] split = attribute.split("->");
        if(split.length != 2)
          throw new AttributeConversionException("Invalid format of attribute given for OrderConverter (\"" + attribute + "\"). " +
                                                     "It has to be {0}->{1}!");
        int fromIndex = Integer.parseInt(split[0]);
        int toIndex = Integer.parseInt(split[1]);
        result.put(fromIndex, toIndex);
      }
      return result;
    }
  }

  /**
   * The DynamicSizeConverter removes unneccesary attributes or adds
   * attributes with NULL-Values if those are requested.
   */
  public static class DynamicSizeConverter implements IAttributeConverter
  {
    @NotNull
    @Override
    public List<OAAttribute> convert(@NotNull List<OAAttribute> pAttributes, @NotNull OAAccessor pNextAccessor)
    {
      List<OAAttribute> originalAttributes = pNextAccessor.getAttributes();
      List<OAAttribute> attributesToConvert = new ArrayList<>(pAttributes);
      if(attributesToConvert.size() == originalAttributes.size())
        return attributesToConvert;

      if(attributesToConvert.size() > originalAttributes.size()) //remove unneccessary attributes
      {
        while (attributesToConvert.size() > originalAttributes.size())
          attributesToConvert.remove(attributesToConvert.size() - 1); //remove last
      }
      else //add missing attributes with NULL-Value
      {
        while (attributesToConvert.size() < originalAttributes.size())
          attributesToConvert.add(new OAAttribute(originalAttributes.get(attributesToConvert.size()).getType(), null));
      }

      return attributesToConvert;
    }
  }

}
