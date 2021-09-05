package com.github.microtweak.jac4e.core.impl;

import com.github.microtweak.jac4e.core.exception.EnumMetadataException;
import org.apache.commons.lang3.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.Field;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ClassAttributeEnumConverter<E extends Enum<E>, V extends Serializable> extends AbstractEnumConverter<E, V> {

    public static final String DEFAULT_ATTRIBUTE_NAME = "value";

    private String attributeName;
    private Field valueAttribute;

    public ClassAttributeEnumConverter(Class<E> enumType, Class<V> valueType) {
        super(enumType, valueType);
        attributeName = DEFAULT_ATTRIBUTE_NAME;
    }

    @Override
    protected void init() {
        try {
            valueAttribute = getEnumType().getDeclaredField(attributeName);
        } catch (NoSuchFieldException e) {
            final String msg = "There is no \"%s\" attribute declared in enum \"%s\"!";
            throw new EnumMetadataException( format(msg, attributeName, getEnumType().getName()) );
        }

        checkTypeOfAttributeValue(valueAttribute.getType());
        valueAttribute.setAccessible(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected V getConstantValue(E constant) {
        try {
            return (V) valueAttribute.get(constant);
        } catch (IllegalAccessException e) {
            final String msg = "Unable to read \"%s\" attribute of enum \"%s\"!";
            throw new EnumMetadataException( format(msg, attributeName, getEnumType().getName()) );
        }
    }

    @SuppressWarnings("unchecked")
    private void checkTypeOfAttributeValue(Class<?> enumValueAttrType) {
        if (enumValueAttrType.isPrimitive()) {
            setValueType( (Class<V>) ClassUtils.wrapperToPrimitive(getValueType()) );
        }

        final String property = getEnumType().getName() + "." + attributeName;

        if (!getValueType().equals(enumValueAttrType)) {
            final String msg = "The type (%s) property \"%s\" is different from the type (%s) informed the AttributeConverter!";
            throw new EnumMetadataException( format(msg, enumValueAttrType.getName(), property, getValueType().getName()) );
        }
    }

    public void setAttributeName(String attributeName) {
        if (isBlank(attributeName)) {
            return;
        }
        this.attributeName = attributeName.trim();
    }
}
