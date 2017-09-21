package com.github.wglanzer.obsoleteaccessors.api;

import com.github.wglanzer.annosave.processor.AnnoPersist;

import java.lang.annotation.*;

/**
 * Container for "@ObsoleteVersion"-Annotation
 *
 * @author W.Glanzer, 04.09.2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@AnnoPersist
public @interface ObsoleteVersions
{

  ObsoleteVersion[] value();

}
