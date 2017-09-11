package de.adito.aditoweb.obsoleteAccessors.spi;

/**
 * An "ObsoleteVersion" defines a specific version of an accessor via annotation.
 *
 * @author W.Glanzer, 04.09.2017
 */
public @interface ObsoleteVersion
{

  /**
   * @return the version this annotation represents
   */
  int version();

  /**
   * @return the name of the package in which this accessor was.
   * <tt>""</tt> if the name has not changed in this version
   */
  String pkgName() default "";

  /**
   * @return the id/name which the accessor had in this version.
   * <tt>""</tt> if it has not changed.
   */
  String id() default "";

  /**
   * @return Type of this accessor. Mainly the returnType of a method, or the type of a field.
   * If it has not changed in this version -> Void.class
   */
  Class<?> type() default Void.class; //todo not void...

  /**
   * @return array containing all parameter-classes the accessor had.
   * If it is an accessor without parameters (fields) this value has no effect.
   * It the parameters did not change in this version -> Void.class
   */
  Class<?>[] parameters() default Void.class; //todo not void...

  /**
   * @return a converter which describes how to convert the parameters from this version to the newer one
   */
  Class<? extends IAttributeConverter> converter() default IAttributeConverter.DEFAULT.class;

}
