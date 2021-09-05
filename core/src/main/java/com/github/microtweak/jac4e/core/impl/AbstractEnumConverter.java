package com.github.microtweak.jac4e.core.impl;

import com.github.microtweak.jac4e.core.EnumConverter;
import com.github.microtweak.jac4e.core.exception.EnumValueDuplicateException;
import com.github.microtweak.jac4e.core.exception.EnumValueNotPresentException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public abstract class AbstractEnumConverter<E extends Enum<E>, V extends Serializable> implements EnumConverter<E, V> {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Class<E> enumType;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Class<V> valueType;

    @Getter @Setter
    private boolean errorIfValueNotPresent;

    private boolean initialized;
    private Map<E, V> values;
    private Map<V, E> constants;

    public AbstractEnumConverter(Class<E> enumType, Class<V> valueType) {
        this.enumType = enumType;
        this.valueType = valueType;
    }

    private synchronized void lazyInitConverter() {
        if (!initialized) {
            init();
            fillConverter();

            initialized = true;
        }
    }

    protected void init() {
    }

    private void fillConverter() {
        values = new HashMap<>();
        constants = new HashMap<>();

        for (E constant : getEnumType().getEnumConstants()) {
            V value = getConstantValue(constant);

            if (constants.containsKey(value)) {
                final String msg = "There are one or more constants in the %s enum with the value \"%s\"";
                throw new EnumValueDuplicateException( format(msg, enumType.getName(), value) );
            }

            values.put(constant, value);
            constants.put(value, constant);
        }
    }

    protected abstract V getConstantValue(E constant);

    public V toValue(E attribute) {
        lazyInitConverter();
        return attribute == null ? null : values.get(attribute);
    }

    public E toEnum(V value) {
        lazyInitConverter();

        if (value == null) {
            return null;
        }

        final E constant = constants.get(value);

        checkValueNotPresent(constant, value);

        return constant;
    }

    protected final void checkValueNotPresent(E constant, V value) {
        if (!errorIfValueNotPresent || constant != null) {
            return;
        }

        final String msg = "The \"%s\" value is not present in any constant of %s enum!";
        throw new EnumValueNotPresentException( format(msg, value, enumType.getName()) );
    }

}