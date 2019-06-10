package com.github.microtweak.jac4e.core;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface EnumAttributeConverter {

    String packageName() default "";

    String attributeName() default "";

    boolean autoApply() default true;

    ValueNotFoundStrategy ifValueNotPresent() default ValueNotFoundStrategy.INHERITED;

}
