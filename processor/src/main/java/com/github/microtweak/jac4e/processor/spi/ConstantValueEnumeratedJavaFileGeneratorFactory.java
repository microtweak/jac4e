package com.github.microtweak.jac4e.processor.spi;

import com.github.microtweak.jac4e.core.ConstantAnnotationEnumerated;
import com.github.microtweak.jac4e.core.impl.ConstantValueEnumConverter;
import com.github.microtweak.jac4e.processor.config.ConstantValueEnumeratedOptions;
import com.github.microtweak.jac4e.processor.generator.DefaultAttributeConverterJavaFileGenerator;
import com.github.microtweak.jac4e.processor.generator.JavaFileGenerator;
import com.squareup.javapoet.ClassName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

public class ConstantValueEnumeratedJavaFileGeneratorFactory extends AbstractJavaFileGeneratorFactory<ConstantAnnotationEnumerated> {

    @Override
    public JavaFileGenerator create(ProcessingEnvironment processingEnv, TypeElement enumTypeElement, boolean forceDisableAutoApply) {
        final ConstantAnnotationEnumerated annotation = getAnnotation(enumTypeElement);
        final ConstantValueEnumeratedOptions options = new ConstantValueEnumeratedOptions(processingEnv, annotation);

        final ClassName enumType = ClassName.get(enumTypeElement);
        final ClassName valueType = ClassName.get( annotation.value().getClassType() );

        return new DefaultAttributeConverterJavaFileGenerator()
                .packageName(options.getPackageName())
                .discriminatorName(ConstantAnnotationEnumerated.class.getSimpleName())
                .enumType(enumType)
                .valueType(valueType)
                .autoApply(!forceDisableAutoApply && options.isAutoApply())
                .errorIfValueNotPresent(options.isErrorIfValueNotPresent())
                .converterType(ConstantValueEnumConverter.class);
    }

}
