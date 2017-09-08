package de.adito.aditoweb.obsoleteAccessors.spi;

import de.adito.picoservice.PicoService;

import java.lang.annotation.*;

/**
 * @author W.Glanzer, 04.09.2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@PicoService
public @interface ObsoleteVersionContainer
{

  String pkgName();
  String category() default "";

}
