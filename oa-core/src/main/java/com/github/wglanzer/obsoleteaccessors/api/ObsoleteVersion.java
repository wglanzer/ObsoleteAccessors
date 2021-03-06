package com.github.wglanzer.obsoleteaccessors.api;

import com.github.wglanzer.annosave.processor.api.AnnoPersist;
import com.github.wglanzer.obsoleteaccessors.impl.util.NotChangedType;
import com.github.wglanzer.obsoleteaccessors.spi.IAttributeConverter;

import java.lang.annotation.*;

/**
 * An "ObsoleteVersion" defines a specific version of an accessor via annotation.
 *
 * @author W.Glanzer, 04.09.2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Repeatable(ObsoleteVersions.class)
@AnnoPersist
public @interface ObsoleteVersion
{

  /**
   * @return a branch describes multiple version-occurrences with multiple conversion methods.
   */
  int branch() default 0;

  /**
   * @return the version this annotation represents
   */
  int version();

  /**
   * @return the name of the package in which this accessor was.
   * Not necessary if the name has not changed in this version
   */
  String pkgName() default "";

  /**
   * @return the id/name which the accessor had in this version.
   * Not necessary if it has not changed.
   */
  String id() default "";

  /**
   * @return Type of this accessor. Mainly the returnType of a method, or the type of a field.
   * Not necessary if it has not changed in this version
   */
  Class<?> type() default NotChangedType.class;

  /**
   * @return array containing all parameter-classes the accessor had.
   * If it is an accessor without parameters (fields) this value has no effect.
   * Not necessary if it has not changed in this version
   */
  Class<?>[] parameters() default NotChangedType.class;

  /**
   * @return a converter which describes how to convert the parameters from this version to the newer one
   * Not necessary if it has not changed in this version
   */
  Class<? extends IAttributeConverter> converter() default IAttributeConverter.DEFAULT.class;

  /**
   * @return additional attributes for an IAttributeConverter instance
   * if a constructor inside the specified Converter consumes a String-Array
   */
  String[] converterAttributes() default "";

}
