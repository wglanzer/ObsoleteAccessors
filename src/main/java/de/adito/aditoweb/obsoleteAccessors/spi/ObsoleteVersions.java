package de.adito.aditoweb.obsoleteAccessors.spi;

import java.lang.annotation.*;

/**
 * @author W.Glanzer, 04.09.2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ObsoleteVersions
{

  ObsoleteVersion[] value();

}
