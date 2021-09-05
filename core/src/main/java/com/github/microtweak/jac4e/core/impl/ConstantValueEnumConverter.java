package com.github.microtweak.jac4e.core.impl;

import com.github.microtweak.jac4e.core.ConstantValue;
import com.github.microtweak.jac4e.core.ConstantValueEnumerated;
import com.github.microtweak.jac4e.core.ValueType;
import com.github.microtweak.jac4e.core.exception.EnumMetadataException;

import java.io.Serializable;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;

public class ConstantValueEnumConverter<E extends Enum<E>, V extends Serializable> extends AbstractEnumConverter<E, V> {

    private ConstantValueEnumerated enumerated;

    public ConstantValueEnumConverter(Class<E> enumType, Class<V> valueType) {
        super(enumType, valueType);
    }

    @Override
    protected void init() {
        enumerated = getEnumType().getAnnotation(ConstantValueEnumerated.class);
    }

    @Override
    protected V getConstantValue(E constant) {
        final ConstantValue constantValue = getConstantValue(constant.name());

        if (isBlank(constantValue.value())) {
            final String msg = "The constant \"%s\" of enum \"%s\" has an empty string as a value.";
            throw new RuntimeException(format(msg, constant.name(), getEnumType().getSimpleName()));
        }

        return ValueType.parseValue(enumerated.value(), constantValue.value().trim());
    }

    private ConstantValue getConstantValue(String name) {
        return ofNullable( getDeclaredField(getEnumType(), name) )
                .map(constant -> constant.getAnnotation(ConstantValue.class))
                .orElseThrow(() -> {
                    final String msg = "Constant \"%s\" of enum \"%s\" is not annotated with @%s";
                    return new EnumMetadataException(format(msg, name, getEnumType().getSimpleName(), ConstantValue.class.getSimpleName()));
                });
    }

}
