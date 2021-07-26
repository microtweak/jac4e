package com.github.microtweak.jac4e.core;

import com.github.microtweak.jac4e.core.exception.EnumValueDuplicateException;
import com.github.microtweak.jac4e.core.exception.EnumValueNotPresentException;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@RequiredArgsConstructor
public abstract class EnumConverter<E extends Enum<E>, V extends Serializable> {

    @NonNull
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Class<E> enumType;

    @NonNull
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Class<V> valueType;

    @Getter @Setter
    private boolean errorIfValueNotPresent;

    private Map<E, V> values;
    private Map<V, E> constants;

    private boolean initialized;

    private synchronized void checkAndInitializeConverter() {
        if (!initialized) {
            values = new HashMap<>();
            constants = new HashMap<>();

            initializeConverter( getEnumType().getEnumConstants() );

            initialized = true;
        }
    }

    protected abstract void initializeConverter(E[] constants);

    protected final void putConstant(E contant, V value) {
        if (constants.containsKey(value)) {
            final String msg = "There are one or more constants in the %s enum with the value \"%s\"";
            throw new EnumValueDuplicateException( format(msg, enumType.getName(), value) );
        }

        values.put(contant, value);
        constants.put(value, contant);
    }

    protected final void checkValueNotPresent(E constant, V value) {
        if (!errorIfValueNotPresent || constant != null) {
            return;
        }

        final String msg = "The \"%s\" value is not present in any constant of %s enum!";
        throw new EnumValueNotPresentException( format(msg, value, enumType.getName()) );
    }

    public V toValue(E attribute) {
        checkAndInitializeConverter();
        return attribute == null ? null : values.get(attribute);
    }

    public E toEnum(V value) {
        checkAndInitializeConverter();

        if (value == null) {
            return null;
        }

        final E constant = constants.get(value);

        checkValueNotPresent(constant, value);

        return constant;
    }

}