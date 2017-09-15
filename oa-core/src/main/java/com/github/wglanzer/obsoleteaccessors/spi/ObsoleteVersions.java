package com.github.wglanzer.obsoleteaccessors.spi;

import java.lang.annotation.*;

/**
 * Container for "@ObsoleteVersion"-Annotation
 *
 * @author W.Glanzer, 04.09.2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ObsoleteVersions
{

  ObsoleteVersion[] value();

}
