package com.github.microtweak.jac4e.core;

import java.io.Serializable;

public interface EnumConverter<E extends Enum<E>, V extends Serializable> {

    V toValue(E attribute);

    E toEnum(V value);

}
