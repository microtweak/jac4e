package com.github.microtweak.jac4e.processor.config;

import com.github.microtweak.jac4e.core.ConstantValueEnumerated;

import javax.annotation.processing.ProcessingEnvironment;

public class ConstantValueEnumeratedOptions extends BaseOptions<ConstantValueEnumerated> {

    public ConstantValueEnumeratedOptions(ProcessingEnvironment processingEnv, ConstantValueEnumerated annotation) {
        super(processingEnv, annotation);
    }

}
