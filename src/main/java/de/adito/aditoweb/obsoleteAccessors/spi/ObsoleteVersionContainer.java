package de.adito.aditoweb.obsoleteAccessors.spi;

import de.adito.picoservice.PicoService;

import java.lang.annotation.*;

/**
 * An ObsoleteVersionContainer can contain methods/fields annotated with "ObsoleteVersion"
 *
 * @see ObsoleteVersion
 * @author W.Glanzer, 04.09.2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@PicoService
public @interface ObsoleteVersionContainer
{

  /**
   * @return Name of the package this container is in
   */
  String pkgName();

  /**
   * @return Additional category. <tt>""</tt> represents "no category"
   */
  String category() default "";

}
