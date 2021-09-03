package com.github.microtweak.jac4e.processor.config;

import com.github.microtweak.jac4e.core.AttributeEnumerated;

import javax.annotation.processing.ProcessingEnvironment;

import static com.github.microtweak.jac4e.processor.SupportedOptions.ATTRIBUTE_NAME;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

public class AttributeEnumeratedOptions extends BaseOptions<AttributeEnumerated> {

    public AttributeEnumeratedOptions(ProcessingEnvironment processingEnv, AttributeEnumerated annotation) {
        super(processingEnv, annotation);
    }

    public String getAttributeName() {
        return defaultIfBlank(getAnnotation().name(), getOptions().get(ATTRIBUTE_NAME));
    }

}
