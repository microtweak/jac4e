package com.github.microtweak.jac4e.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
public enum ValueType {

    BOOLEAN(Boolean.class),
    CHARACTER(Character.class),
    BYTE(Byte.class),
    SHORT(Short.class),
    INTEGER(Integer.class),
    FLOAT(Float.class),
    LONG(Long.class),
    DOUBLE(Double.class),
    BIG_INTEGER(BigInteger.class),
    BIG_DECIMAL(BigDecimal.class),
    STRING(String.class);

    @Getter
    private Class<?> classType;

    @SuppressWarnings("unchecked")
    public static <V extends Serializable> V parseValue(ValueType type, String value) {
        final Serializable result;

        switch (type) {
            case BOOLEAN:
                result = Boolean.parseBoolean(value);
                break;

            case CHARACTER:
                result = value.charAt(0);
                break;

            case BYTE:
                result = Byte.parseByte(value);
                break;

            case SHORT:
                result = Short.parseShort(value);
                break;

            case INTEGER:
                result = Integer.parseInt(value);
                break;

            case FLOAT:
                result = Float.parseFloat(value);
                break;

            case LONG:
                result = Long.parseLong(value);
                break;

            case DOUBLE:
                result = Double.parseDouble(value);
                break;

            case BIG_INTEGER:
                result = new BigInteger(value);
                break;

            case BIG_DECIMAL:
                result = new BigDecimal(value);
                break;

            case STRING:
            default:
                result = value;
                break;
        }

        return (V) result;
    }
}
