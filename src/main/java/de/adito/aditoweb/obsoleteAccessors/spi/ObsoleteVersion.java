package de.adito.aditoweb.obsoleteAccessors.spi;

/**
 * @author W.Glanzer, 04.09.2017
 */
public @interface ObsoleteVersion
{

  int version();
  String pkgName() default "";
  String id() default "";
  Class<? extends IParameterConverter> converter() default IParameterConverter.DEFAULT.class;
  Class<?> type() default Object.class;

}
