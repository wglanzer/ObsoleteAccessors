package de.adito.aditoweb.obsoleteAccessors.spi;

/**
 * @author W.Glanzer, 04.09.2017
 */
public @interface ObsoleteVersion
{

  int version();
  String pkgName() default "";
  String id() default "";
  Class<?>[] parameters() default Void.class;
  Class<? extends IParameterConverter> converter() default IParameterConverter.DEFAULT.class;
  Class<?> type() default Void.class;

}
