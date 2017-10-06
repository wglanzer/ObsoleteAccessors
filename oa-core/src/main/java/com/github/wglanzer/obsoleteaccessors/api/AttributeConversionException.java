package com.github.wglanzer.obsoleteaccessors.api;

/**
 * Exception that is thrown, if a attribute-conversion has (partially) failed
 *
 * @author W.Glanzer, 11.09.2017
 */
public class AttributeConversionException extends Exception
{

  public AttributeConversionException()
  {
  }

  public AttributeConversionException(Throwable cause)
  {
    super(cause);
  }

  public AttributeConversionException(String message)
  {
    super(message);
  }

  public AttributeConversionException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
