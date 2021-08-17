package com.github.microtweak.jac4e.testing.tests.convertion;

import com.github.microtweak.jac4e.core.exception.EnumMetadataException;
import com.github.microtweak.jac4e.core.exception.EnumValueDuplicateException;
import com.github.microtweak.jac4e.core.exception.EnumValueNotPresentException;
import com.github.microtweak.jac4e.core.impl.ClassAttributeConverter;
import com.github.microtweak.jac4e.testing.beans.Country;
import com.github.microtweak.jac4e.testing.beans.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomOptionsConvertTest {

    @Test
    public void convertEnumWithCustomAttributeName() {
        ClassAttributeConverter<Country, String> converter = new ClassAttributeConverter<>(Country.class, String.class);

        Assertions.assertThrows(EnumMetadataException.class, () -> assertEquals(Country.AUSTRALIA, converter.toEnum("AU")));

        converter.setAttributeName("isoCode");

        Assertions.assertAll(
            () -> assertEquals(Country.BRAZIL, converter.toEnum("BR")),
            () -> Assertions.assertEquals("BR", converter.toValue(Country.BRAZIL))
        );
    }

    @Test
    public void errorOnDuplicateValues() {
        ClassAttributeConverter<Country, Integer> converter = new ClassAttributeConverter<>(Country.class, Integer.class);
        converter.setAttributeName("callingCode");

        Assertions.assertThrows(EnumValueDuplicateException.class, () -> assertEquals(Country.UNITED_STATES, converter.toEnum(1)));
    }

    @Test
    public void convertEnumWithUnknownValue() {
        ClassAttributeConverter<Payment, Integer> converter = new ClassAttributeConverter<>(Payment.class, Integer.class);

        Assertions.assertNull(converter.toEnum(100));

        converter.setErrorIfValueNotPresent(true);

        Assertions.assertThrows(EnumValueNotPresentException.class, () -> converter.toEnum(100));
    }

}
