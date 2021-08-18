package com.github.microtweak.jac4e.core;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface ConstantValueEnumerated {

    String packageName() default "";

    Class<? extends Serializable> value() default String.class;

    boolean autoApply() default true;

    ValueNotFoundStrategy ifValueNotPresent() default ValueNotFoundStrategy.INHERITED;

}
