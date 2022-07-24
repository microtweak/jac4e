package com.github.microtweak.jac4e.testing.tests.convertion;

import com.github.microtweak.jac4e.core.impl.AbstractEnumConverter;
import com.github.microtweak.jac4e.core.impl.ClassAttributeEnumConverter;
import com.github.microtweak.jac4e.testing.beans.Gender;
import com.github.microtweak.jac4e.testing.beans.Payment;
import com.github.microtweak.jac4e.testing.beans.YesNo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicConvertTest {

    @Test
    public void convertEnumCharacter() {
        final AbstractEnumConverter<Gender, Character> converter = new AbstractEnumConverter<Gender, Character>(Gender.class, Character.class) {
            @Override
            protected Character getConstantValue(Gender constant) {
                return constant != null ? constant.name().charAt(0) : null;
            }
        };

        Assertions.assertAll(
            () -> assertEquals(Gender.MALE, converter.toEnum('M')),
            () -> assertEquals((Character) 'M', converter.toValue(Gender.MALE))
        );
    }

    @Test
    public void convertEnumBoolean() {
        final AbstractEnumConverter<YesNo, Boolean> converter = new AbstractEnumConverter<YesNo, Boolean>(YesNo.class, Boolean.class) {
            @Override
            protected Boolean getConstantValue(YesNo constant) {
                return constant != null ? constant == YesNo.YES : null;
            }
        };

        Assertions.assertAll(
            () -> assertEquals(YesNo.YES, converter.toEnum(true)),
            () -> assertEquals(true, converter.toValue(YesNo.YES))
        );
    }

    @Test
    public void convertEnumInteger() {
        final AbstractEnumConverter<Payment, Integer> converter = new AbstractEnumConverter<Payment, Integer>(Payment.class, Integer.class) {
            @Override
            protected Integer getConstantValue(Payment constant) {
                return constant != null ? constant.getValue() : null;
            }
        };

        Assertions.assertAll(
            () -> assertEquals(Payment.CREDIT_CARD, converter.toEnum(1)),
            () -> assertEquals((Integer) 1, converter.toValue(Payment.CREDIT_CARD))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "false", "true" })
    public void convertFromNull(boolean errorIfValueNotPresent) {
        final AbstractEnumConverter<Payment, Integer> converter = new AbstractEnumConverter<Payment, Integer>(Payment.class, Integer.class) {
            @Override
            protected Integer getConstantValue(Payment constant) {
                return constant != null ? constant.getValue() : null;
            }
        };
        converter.setErrorIfValueNotPresent(errorIfValueNotPresent);

        Assertions.assertAll(
            () -> Assertions.assertNull(converter.toEnum(null)),
            () -> Assertions.assertNull(converter.toValue(null))
        );
    }

}
