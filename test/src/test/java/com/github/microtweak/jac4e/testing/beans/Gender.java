package com.github.microtweak.jac4e.testing.beans;

import com.github.microtweak.jac4e.core.AttributeEnumerated;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@AttributeEnumerated
public enum Gender {

    MALE('M'),
    FEMALE('F');

    private char value;

}
