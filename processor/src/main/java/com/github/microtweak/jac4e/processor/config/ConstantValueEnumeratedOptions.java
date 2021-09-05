package com.github.microtweak.jac4e.processor.config;

import com.github.microtweak.jac4e.core.ConstantAnnotationEnumerated;

import javax.annotation.processing.ProcessingEnvironment;

public class ConstantValueEnumeratedOptions extends BaseOptions<ConstantAnnotationEnumerated> {

    public ConstantValueEnumeratedOptions(ProcessingEnvironment processingEnv, ConstantAnnotationEnumerated annotation) {
        super(processingEnv, annotation);
    }

}
