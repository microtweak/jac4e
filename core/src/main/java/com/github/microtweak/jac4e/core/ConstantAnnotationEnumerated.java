package com.github.microtweak.jac4e.core;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface ConstantAnnotationEnumerated {

    String packageName() default "";

    ValueType value() default ValueType.STRING;

    boolean autoApply() default true;

    ValueNotFoundStrategy ifValueNotPresent() default ValueNotFoundStrategy.INHERITED;

}
