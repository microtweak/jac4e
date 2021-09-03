package com.github.microtweak.jac4e.processor;

public final class SupportedOptions {

    private static final String PREFIX = "jac4e.";

    public static final String PACKAGE_NAME = PREFIX + "packageName";
    public static final String ERROR_IF_VALUE_NOT_FOUND = PREFIX + "errorIfValueNotFound";
    public static final String ATTRIBUTE_NAME = PREFIX + "attributeName";

    private SupportedOptions() {
        throw new UnsupportedOperationException();
    }

}