package com.github.microtweak.jac4e.processor.spi;

import com.github.microtweak.jac4e.core.AttributeEnumerated;
import com.github.microtweak.jac4e.core.exception.EnumMetadataException;
import com.github.microtweak.jac4e.core.impl.ClassAttributeEnumConverter;
import com.github.microtweak.jac4e.processor.config.AttributeEnumeratedOptions;
import com.github.microtweak.jac4e.processor.generator.ClassFieldAttributeConverterJavaFileGenerator;
import com.github.microtweak.jac4e.processor.generator.JavaFileGenerator;
import com.squareup.javapoet.ClassName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class AttributeEnumeratedJavaFileGeneratorFactory extends AbstractJavaFileGeneratorFactory<AttributeEnumerated> {

    @Override
    public JavaFileGenerator create(ProcessingEnvironment processingEnv, TypeElement enumTypeElement, boolean forceDisableAutoApply) {
        final AttributeEnumerated annotation = getAnnotation(enumTypeElement);
        final AttributeEnumeratedOptions options = new AttributeEnumeratedOptions(processingEnv, annotation);

        final ClassName enumType = ClassName.get(enumTypeElement);
        final ClassName valueType = ClassName.get( getValueType(processingEnv, enumTypeElement, options.getAttributeName()) );

        return new ClassFieldAttributeConverterJavaFileGenerator()
                .attributeName(options.getAttributeName())
                .packageName(options.getPackageName())
                .discriminatorName(AttributeEnumerated.class.getSimpleName())
                .enumType(enumType)
                .valueType(valueType)
                .autoApply(!forceDisableAutoApply && options.isAutoApply())
                .errorIfValueNotPresent(options.isErrorIfValueNotPresent());
    }

    private TypeElement getValueType(ProcessingEnvironment processingEnv, TypeElement enumTypeElement, String attributeName) {
        if (isBlank(attributeName)) {
            attributeName = ClassAttributeEnumConverter.DEFAULT_ATTRIBUTE_NAME;
        }

        for (Element element : enumTypeElement.getEnclosedElements()) {
            final String fieldName = element.getSimpleName().toString();

            if (element.getKind() != ElementKind.FIELD || !fieldName.equals(attributeName)) {
                continue;
            }

            final TypeMirror type = element.asType();

            if (type.getKind().isPrimitive()) {
                return processingEnv.getTypeUtils().boxedClass( processingEnv.getTypeUtils().getPrimitiveType(type.getKind()) );
            }

            return (TypeElement) processingEnv.getTypeUtils().asElement(type);
        }

        throw new EnumMetadataException("The enum \"" + enumTypeElement.getQualifiedName() + "\" does not have the \"" + attributeName + "\" attribute!");
    }

}
