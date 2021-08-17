package com.github.microtweak.jac4e.testing.beans;

import com.github.microtweak.jac4e.core.AttributeEnumerated;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@AttributeEnumerated(name = "isoCode", autoApply = false)
public enum Country {

    AUSTRALIA("AU",61),
    BRAZIL("BR",55),
    CANADA("CA",1),
    SPAIN("ES",34),
    UNITED_STATES("US",1);

    private String isoCode; // ISO 3166 Code
    private int callingCode;

}
