package com.github.microtweak.jac4e.core.impl;

import com.github.microtweak.jac4e.core.ConstantValue;
import com.github.microtweak.jac4e.core.EnumConverter;
import com.github.microtweak.jac4e.core.exception.EnumMetadataException;

import java.io.Serializable;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;

public class ConstantValueEnumConverter<E extends Enum<E>, V extends Serializable> extends EnumConverter<E, V> {

    public ConstantValueEnumConverter(Class<E> enumType, Class<V> valueType) {
        super(enumType, valueType);
    }

    @Override
    protected void initializeConverter(E[] constants) {
        for (E constantItem : constants) {
            final ConstantValue constantValue = getConstantValue(constantItem.name());

            if (isBlank(constantValue.value())) {
                final String msg = "The constant \"%s\" of enum \"%s\" has an empty string as a value.";
                throw new RuntimeException(format(msg, constantItem.name(), getEnumType().getSimpleName()));
            }

            putConstant(constantItem, toParsedValue(constantValue.value().trim()));
        }
    }

    private ConstantValue getConstantValue(String name) {
        return ofNullable( getDeclaredField(getEnumType(), name) )
                .map(constant -> constant.getAnnotation(ConstantValue.class))
                .orElseThrow(() -> {
                    final String msg = "Constant \"%s\" of enum \"%s\" is not annotated with @%s";
                    return new EnumMetadataException(format(msg, name, getEnumType().getSimpleName(), ConstantValue.class.getSimpleName()));
                });
    }

    private V toParsedValue(String value) {
        final Serializable result;

        if (Boolean.class.equals(getValueType()) || Boolean.TYPE.equals(getValueType())) {
            result = Boolean.parseBoolean(value);
        }
        else if (Character.class.equals(getValueType()) || Character.TYPE.equals(getValueType())) {
            result = value.charAt(0);
        }
        else if (Byte.class.equals(getValueType()) || Byte.TYPE.equals(getValueType())) {
            result = Byte.parseByte(value);
        }
        else if (Short.class.equals(getValueType()) || Short.TYPE.equals(getValueType())) {
            result = Short.parseShort(value);
        }
        else if (Integer.class.equals(getValueType()) || Integer.TYPE.equals(getValueType())) {
            result = Integer.parseInt(value);
        }
        else if (Float.class.equals(getValueType()) || Float.TYPE.equals(getValueType())) {
            result = Float.parseFloat(value);
        }
        else if (Long.class.equals(getValueType()) || Long.TYPE.equals(getValueType())) {
            result = Long.parseLong(value);
        }
        else if (Double.class.equals(getValueType()) || Double.TYPE.equals(getValueType())) {
            result = Double.parseDouble(value);
        }
        else if (String.class.equals(getValueType())) {
            result = value;
        }
        else {
            final String msg = "Failure to analyze \"%s\" to \"%s\". The \"%s\" type is not supported";
            throw new RuntimeException(format(msg, value, getValueType(), getValueType().getSimpleName()));
        }

        return getValueType().cast(result);
    }

}
