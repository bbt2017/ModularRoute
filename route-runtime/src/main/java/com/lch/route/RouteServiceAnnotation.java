package com.lch.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lichenghang on 2017/5/28.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface RouteServiceAnnotation {

    String serviceInterface();
}
