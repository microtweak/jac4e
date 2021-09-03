package com.github.microtweak.jac4e.processor.config;

import com.github.microtweak.jac4e.core.ValueNotFoundStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

import javax.annotation.processing.ProcessingEnvironment;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static com.github.microtweak.jac4e.processor.SupportedOptions.ERROR_IF_VALUE_NOT_FOUND;
import static com.github.microtweak.jac4e.processor.SupportedOptions.PACKAGE_NAME;
import static org.apache.commons.lang3.StringUtils.firstNonBlank;
import static org.apache.commons.lang3.reflect.MethodUtils.invokeMethod;

@Getter(AccessLevel.PROTECTED)
public class BaseOptions<A extends Annotation> implements Options {

    private Map<String, String> options;
    private A annotation;

    public BaseOptions(ProcessingEnvironment processingEnv, A annotation) {
        this.options = processingEnv.getOptions();
        this.annotation = annotation;
    }

    @Override
    public String getPackageName() {
        return firstNonBlank(getAnnotationProperty("packageName"), options.get(PACKAGE_NAME), "");
    }

    @Override
    public boolean isAutoApply() {
        return getAnnotationProperty("autoApply");
    }

    @Override
    public boolean isErrorIfValueNotPresent() {
        ValueNotFoundStrategy valueNotFound = getAnnotationProperty("ifValueNotPresent");

        if (valueNotFound == ValueNotFoundStrategy.INHERITED) {
            valueNotFound = getOptionAsEnum(ERROR_IF_VALUE_NOT_FOUND, ValueNotFoundStrategy.class);
        }

        return valueNotFound == ValueNotFoundStrategy.THROW_ERROR;
    }

    @SuppressWarnings("unchecked")
    private <V> V getAnnotationProperty(String propertyName) {
        try {
            return (V) invokeMethod(getAnnotation(), propertyName);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    protected <E extends Enum<E>> E getOptionAsEnum(String property, Class<E> clazz) {
        return EnumUtils.getEnumIgnoreCase(clazz, getOptions().get(property));
    }

}
