package com.github.microtweak.jac4e.processor.spi;

import org.apache.commons.lang3.reflect.TypeUtils;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.TypeVariable;

public abstract class AbstractJavaFileGeneratorFactory<A extends Annotation> implements JavaFileGeneratorFactory<A> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<A> getSupportedAnnotationType() {
        final TypeVariable<?> typeVar = JavaFileGeneratorFactory.class.getTypeParameters()[0];
        return (Class<A>) TypeUtils.getRawType(typeVar, getClass());
    }

    protected A getAnnotation(TypeElement element) {
        return element.getAnnotation(getSupportedAnnotationType());
    }

    @Override
    public boolean isSupported(TypeElement element) {
        return getAnnotation(element) != null;
    }

}
