package com.github.microtweak.jac4e.processor;

import com.github.microtweak.jac4e.core.AttributeEnumerated;
import com.github.microtweak.jac4e.core.ValueNotFoundStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class Jac4eOptions {

    private static final String PREFIX = "jac4e.";

    public static final String PROPERTY_PACKAGE_NAME = PREFIX + "packageName";
    public static final String PROPERTY_ATTRIBUTE_NAME = PREFIX + "attributeName";
    public static final String PROPERTY_ERROR_IF_VALUE_NOT_FOUND = PREFIX + "errorIfValueNotFound";

    private final AttributeEnumerated enumOpts;

    private String aptPackageName;
    private String aptAttributeName;
    private boolean aptErrorIfValueNotPresent;

    public Jac4eOptions(AttributeEnumerated enumOpts, Map<String, String> aptOptions) {
        this.enumOpts = enumOpts;
        aptOptions.forEach(this::setProperty);
    }

    private void setProperty(String propertyName, String value) {
        switch (propertyName) {
            case PROPERTY_PACKAGE_NAME:
                aptPackageName = value;
                break;

            case PROPERTY_ATTRIBUTE_NAME:
                aptAttributeName = value;
                break;

            case PROPERTY_ERROR_IF_VALUE_NOT_FOUND:
                aptErrorIfValueNotPresent = Boolean.parseBoolean(value);
                break;
        }
    }

    public String getPackageName() {
        return StringUtils.defaultIfBlank(enumOpts.packageName(), aptPackageName);
    }

    public String getAttributeName() {
        return StringUtils.defaultIfBlank(enumOpts.name(), aptAttributeName);
    }

    public boolean isAutoApply() {
        return enumOpts.autoApply();
    }

    public boolean isErrorIfValueNotPresent() {
        ValueNotFoundStrategy valueNotFound = enumOpts.ifValueNotPresent();

        if (valueNotFound != ValueNotFoundStrategy.INHERITED) {
            return (valueNotFound == ValueNotFoundStrategy.THROW_ERROR);
        }

        return aptErrorIfValueNotPresent;
    }

}
