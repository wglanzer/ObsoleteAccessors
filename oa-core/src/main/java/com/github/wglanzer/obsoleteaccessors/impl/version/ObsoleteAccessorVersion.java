package com.github.wglanzer.obsoleteaccessors.impl.version;

import com.github.wglanzer.obsoleteaccessors.impl.attributes.IAccessorAttributeDescription;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.conversion.*;
import com.github.wglanzer.obsoleteaccessors.spi.IAttributeConverter;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author W.Glanzer, 04.09.2017
 */
class ObsoleteAccessorVersion extends AbstractAccessorVersion
{

  private final Class<? extends IAttributeConverter> converter;
  private final String[] converterAttributes;
  private IAccessorAttributeConverter attrConverterInstance;

  public ObsoleteAccessorVersion(int pVersion, String pPkgName, String pId, Class<? extends IAttributeConverter> pConverter, String[] pConverterAttributes,
                                 Class<?> pType, List<IAccessorAttributeDescription<?>> pAttributeDescriptions)
  {
    super(pVersion, pPkgName, pId, pType, pAttributeDescriptions);
    converter = pConverter;
    converterAttributes = pConverterAttributes;
  }

  @Override
  public IAccessorAttributeConverter getConverter()
  {
    if(attrConverterInstance == null)
    {
      try
      {
        IAttributeConverter paramConv;
        try
        {
          Constructor<? extends IAttributeConverter> constWithAttrs = converter.getDeclaredConstructor(String[].class);
          String[] attrs = _isEmpty(converterAttributes) ? new String[0] : converterAttributes;
          paramConv = constWithAttrs.newInstance((Object) attrs);
        }
        catch(NoSuchMethodException nsme)
        {
          paramConv = converter.newInstance();
        }

        attrConverterInstance = new ParameterAttributeConverter(paramConv);
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }

    return attrConverterInstance;
  }

  /**
   * @return <tt>true</tt> if the array is empty (empty = null or contains one empty string)
   */
  private boolean _isEmpty(String[] pArray)
  {
    return pArray == null || pArray.length == 0 ||
        (pArray.length == 1 && (pArray[0] == null || pArray[0].isEmpty()));
  }

}
