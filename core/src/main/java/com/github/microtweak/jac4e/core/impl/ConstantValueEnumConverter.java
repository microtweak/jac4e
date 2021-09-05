package com.github.microtweak.jac4e.core.impl;

import com.github.microtweak.jac4e.core.ColumnValue;
import com.github.microtweak.jac4e.core.ConstantAnnotationEnumerated;
import com.github.microtweak.jac4e.core.ValueType;
import com.github.microtweak.jac4e.core.exception.EnumMetadataException;

import java.io.Serializable;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;

public class ConstantValueEnumConverter<E extends Enum<E>, V extends Serializable> extends AbstractEnumConverter<E, V> {

    private ConstantAnnotationEnumerated enumerated;

    public ConstantValueEnumConverter(Class<E> enumType, Class<V> valueType) {
        super(enumType, valueType);
    }

    @Override
    protected void init() {
        enumerated = getEnumType().getAnnotation(ConstantAnnotationEnumerated.class);
    }

    @Override
    protected V getConstantValue(E constant) {
        final ColumnValue columnValue = getColumnValueAnnotation(constant.name());

        if (isBlank(columnValue.value())) {
            final String msg = "The constant \"%s\" of enum \"%s\" has an empty string as a value.";
            throw new RuntimeException(format(msg, constant.name(), getEnumType().getSimpleName()));
        }

        return ValueType.parseValue(enumerated.value(), columnValue.value().trim());
    }

    private ColumnValue getColumnValueAnnotation(String name) {
        return ofNullable( getDeclaredField(getEnumType(), name) )
                .map(constant -> constant.getAnnotation(ColumnValue.class))
                .orElseThrow(() -> {
                    final String msg = "Constant \"%s\" of enum \"%s\" is not annotated with @%s";
                    return new EnumMetadataException(format(msg, name, getEnumType().getSimpleName(), ColumnValue.class.getSimpleName()));
                });
    }

}
