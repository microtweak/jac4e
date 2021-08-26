package com.github.microtweak.jac4e.testing.tests.convertion;

import com.github.microtweak.jac4e.core.impl.ClassAttributeEnumConverter;
import com.github.microtweak.jac4e.testing.beans.Gender;
import com.github.microtweak.jac4e.testing.beans.Payment;
import com.github.microtweak.jac4e.testing.beans.YesNo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicConvertTest {

    @Test
    public void convertEnumCharacter() {
        ClassAttributeEnumConverter<Gender, Character> converter = new ClassAttributeEnumConverter<>(Gender.class, Character.class);

        Assertions.assertAll(
            () -> assertEquals(Gender.MALE, converter.toEnum('M')),
            () -> assertEquals((Character) 'M', converter.toValue(Gender.MALE))
        );
    }

    @Test
    public void convertEnumBoolean() {
        ClassAttributeEnumConverter<YesNo, Boolean> converter = new ClassAttributeEnumConverter<>(YesNo.class, Boolean.class);

        Assertions.assertAll(
            () -> assertEquals(YesNo.YES, converter.toEnum(true)),
            () -> assertEquals(true, converter.toValue(YesNo.YES))
        );
    }

    @Test
    public void convertEnumInteger() {
        ClassAttributeEnumConverter<Payment, Integer> converter = new ClassAttributeEnumConverter<>(Payment.class, Integer.class);

        Assertions.assertAll(
            () -> assertEquals(Payment.CREDIT_CARD, converter.toEnum(1)),
            () -> assertEquals((Integer) 1, converter.toValue(Payment.CREDIT_CARD))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "false", "true" })
    public void convertFromNull(boolean errorIfValueNotPresent) {
        ClassAttributeEnumConverter<Payment, Integer> converter = new ClassAttributeEnumConverter<>(Payment.class, Integer.class);
        converter.setErrorIfValueNotPresent(errorIfValueNotPresent);

        Assertions.assertAll(
            () -> Assertions.assertNull(converter.toEnum(null)),
            () -> Assertions.assertNull(converter.toValue(null))
        );
    }

}
