package com.github.microtweak.jac4e.processor;

import com.github.microtweak.jac4e.core.BaseEnumAttributeConverter;
import com.github.microtweak.jac4e.core.EnumAttributeConverter;
import com.github.microtweak.jac4e.core.exception.EnumMetadataException;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Jac4eProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton( EnumAttributeConverter.class.getCanonicalName() );
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> annotatedEnuns = roundEnv.getElementsAnnotatedWith(EnumAttributeConverter.class).stream()
                .filter(e -> e.getKind() == ElementKind.ENUM)
                .map(e -> (TypeElement) e)
                .collect(Collectors.toSet());

        try {
            for (TypeElement enumType : annotatedEnuns) {
                Jac4eOptions opts = new Jac4eOptions(enumType.getAnnotation(EnumAttributeConverter.class), processingEnv.getOptions());

                TypeElement valueType = findAttributeTypeElementByName(enumType, opts.getAttributeName());

                createConverterJavaFile(enumType, valueType, opts).writeTo(processingEnv.getFiler());
            }
        } catch (Exception e){
            throw new RuntimeException("Failed to generate the AttributeConverter of enums", e);
        }

        return true;
    }

    private TypeElement findAttributeTypeElementByName(TypeElement enumType, String attributeName) {
        if (StringUtils.isBlank(attributeName)) {
            attributeName = BaseEnumAttributeConverter.DEFAULT_ATTRIBUTE_NAME;
        }

        for (Element element : enumType.getEnclosedElements()) {
            final String fieldName = element.getSimpleName().toString();

            if (element.getKind() != ElementKind.FIELD || !fieldName.equals(attributeName)) {
                continue;
            }

            TypeMirror type = element.asType();

            if (type.getKind().isPrimitive()) {
                return processingEnv.getTypeUtils().boxedClass( processingEnv.getTypeUtils().getPrimitiveType(type.getKind()) );
            }

            return (TypeElement) processingEnv.getTypeUtils().asElement(type);
        }

        throw new EnumMetadataException("The enum \"" + enumType.getQualifiedName() + "\" does not have the \"" + attributeName + "\" attribute!");
    }

    private JavaFile createConverterJavaFile(TypeElement enumType, TypeElement attributeType, Jac4eOptions opts) {
        final ClassName enumTypeClassName = ClassName.get(enumType);
        final ClassName attributeTypeClassName = ClassName.get(attributeType);

        String packageName = opts.getPackageName();

        if (StringUtils.isBlank(packageName)) {
            packageName = enumTypeClassName.packageName();
        }

        final String converterName = String.join("", enumTypeClassName.simpleNames()) + "AttributeConverter";

        TypeSpec.Builder builder = TypeSpec.classBuilder(converterName)
                .addModifiers(Modifier.PUBLIC)
                .superclass(toParameterizedTypeName(BaseEnumAttributeConverter.class, enumTypeClassName, attributeTypeClassName))
                .addSuperinterface(toParameterizedTypeName(AttributeConverter.class, enumTypeClassName, attributeTypeClassName));

        addJpaConverterAnnotation(builder, opts);

        addConverterConstructor(builder, enumTypeClassName, attributeTypeClassName, opts);

        return JavaFile.builder(packageName, builder.build())
                .skipJavaLangImports(true)
                .build();
    }

    private ParameterizedTypeName toParameterizedTypeName(Class<?> parameterizedType, TypeName... types) {
        return ParameterizedTypeName.get(ClassName.get(parameterizedType), types);
    }

    private void addJpaConverterAnnotation(TypeSpec.Builder converterBuilder, Jac4eOptions opts) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(Converter.class);

        if (opts.isAutoApply()) {
            builder.addMember("autoApply", "$L", true);
        }

        converterBuilder.addAnnotation(builder.build());
    }

    private void addConverterConstructor(TypeSpec.Builder converterBuilder, ClassName enumTypeClassName, ClassName attributeTypeClassName, Jac4eOptions opts) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super($T.class, $T.class)", enumTypeClassName, attributeTypeClassName);

        if (StringUtils.isNotBlank(opts.getAttributeName())) {
            builder.addStatement("setAttributeName($S)", opts.getAttributeName());
        }

        if (opts.isErrorIfValueNotPresent()) {
            builder.addStatement("setErrorIfValueNotPresent($L)", true);
        }

        converterBuilder.addMethod(builder.build());
    }

}
