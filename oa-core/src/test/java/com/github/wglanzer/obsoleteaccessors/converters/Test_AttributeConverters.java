package com.github.wglanzer.obsoleteaccessors.converters;

import com.github.wglanzer.obsoleteaccessors.api.*;
import com.github.wglanzer.obsoleteaccessors.api.converters.AttributeConverters;
import com.github.wglanzer.obsoleteaccessors.spi.IAttributeConverter;
import org.junit.*;

import java.util.*;
import java.util.stream.*;

/**
 * @author W.Glanzer, 05.10.2017
 */
public class Test_AttributeConverters
{

  @Test
  public void test_orderConverter() throws AttributeConversionException
  {
    IAttributeConverter conv = new AttributeConverters.OrderConverter(new String[]{"0->1", "2->0"});
    List<OAAttribute> params = Arrays.asList(_of(double.class, 5.0), _of(String.class, "asdf"), _of(int.class, 2));
    List<OAAttribute> converted = conv.convert(params, _createAccessor(double.class, String.class, int.class));
    Assert.assertEquals(Arrays.asList(_of(int.class, 2), _of(String.class, "asdf"), _of(double.class, 5.0)), converted);
  }

  @Test
  public void test_dynamicSizeConverter() throws AttributeConversionException
  {
    IAttributeConverter conv = new AttributeConverters.DynamicSizeConverter();

    // Shrink
    List<OAAttribute> params = Arrays.asList(_of(double.class, 5.0), _of(String.class, "asdf"), _of(int.class, 2));
    List<OAAttribute> converted = conv.convert(params, _createAccessor(double.class, String.class));
    Assert.assertEquals(Arrays.asList(_of(double.class, 5.0), _of(String.class, "asdf")), converted);

    // Expand
    params = Arrays.asList(_of(double.class, 5.0), _of(String.class, "asdf"));
    converted = conv.convert(params, _createAccessor(double.class, String.class, String.class, int[].class));
    Assert.assertEquals(Arrays.asList(_of(double.class, 5.0), _of(String.class, "asdf"), _of(String.class, null), _of(int[].class, null)), converted);

    // Stay same
    params = Arrays.asList(_of(double.class, 5.0), _of(String.class, "asdf"));
    converted = conv.convert(params, _createAccessor(double.class, String.class));
    Assert.assertEquals(Arrays.asList(_of(double.class, 5.0), _of(String.class, "asdf")), converted);
  }

  /**
   * @return Creates an OAAttribute with the given value/type
   */
  private <T> OAAttribute _of(Class<T> pType, T pValue)
  {
    return new OAAttribute(pType, pValue);
  }

  private OAAccessor _createAccessor(Class<?>... pAttributeDescriptions)
  {
    return new OAAccessor("_DUMMY_", "_DUMMY_", Stream.of(pAttributeDescriptions)
        .map(pClass -> _of(pClass, null))
        .collect(Collectors.toList()), Object.class);
  }

}
