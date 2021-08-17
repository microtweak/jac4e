package com.github.microtweak.jac4e.testing.beans;

import com.github.microtweak.jac4e.core.AttributeEnumerated;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@AttributeEnumerated
public enum YesNo {

    YES(true),
    NO(false);

    private boolean value;

}
