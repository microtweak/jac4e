package com.github.microtweak.jac4e.processor.generator;

import com.github.microtweak.jac4e.core.EnumConverter;
import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.firstNonBlank;

@Getter
@Setter
@Accessors(fluent = true)
public class DefaultAttributeConverterJavaFileGenerator implements JavaFileGenerator {

    private ClassName enumType;
    private ClassName valueType;
    private Class<? extends EnumConverter> converterType;
    private String packageName;
    private boolean autoApply;
    private boolean errorIfValueNotPresent;

    protected final ParameterizedTypeName toParameterizedTypeName(ClassName parameterizedType, TypeName... types) {
        return ParameterizedTypeName.get(parameterizedType, types);
    }

    protected final ParameterizedTypeName toParameterizedTypeName(Class<?> parameterizedType, TypeName... types) {
        return toParameterizedTypeName(ClassName.get(parameterizedType), types);
    }

    protected AnnotationSpec jpaConverterAnnotationSpec() {
        AnnotationSpec.Builder spec = AnnotationSpec.builder(Converter.class);

        if (autoApply) {
            spec.addMember("autoApply", "$L", true);
        }

        return spec.build();
    }

    protected FieldSpec converterFieldSpec(Class<? extends EnumConverter> converterType, ClassName enumTypeClassName, ClassName valueTypeClassName) {
        return FieldSpec.builder(toParameterizedTypeName(converterType, enumTypeClassName, valueTypeClassName), "converter")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    protected MethodSpec defaultConstructorMethodSpec(FieldSpec converterField, Class<? extends EnumConverter> converterType, ClassName enumTypeClassName, ClassName valueTypeClassName) {
        final MethodSpec.Builder spec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$L = new $T<>($T.class, $T.class)", converterField.name, ClassName.get(converterType), enumTypeClassName, valueTypeClassName);

        if (errorIfValueNotPresent) {
            spec.addStatement("$L.setErrorIfValueNotPresent($L)", converterField.name, true);
        }

        customStatementDefaultConstructor(spec, converterField);

        return spec.build();
    }

    protected void customStatementDefaultConstructor(MethodSpec.Builder constructorMethodSpec, FieldSpec converterField) {
    }

    protected MethodSpec convertToDatabaseColumnMethodSpec(FieldSpec converterField, ClassName enumTypeClassName, ClassName valueTypeClassName) {
        return MethodSpec.methodBuilder("convertToDatabaseColumn")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(valueTypeClassName)
                .addParameter(enumTypeClassName, "attribute")
                .addStatement("return $L.toValue(attribute)", converterField.name)
                .build();
    }

    protected MethodSpec convertToEntityAttributeMethodSpec(FieldSpec converterField, ClassName enumTypeClassName, ClassName valueTypeClassName) {
        return MethodSpec.methodBuilder("convertToEntityAttribute")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(enumTypeClassName)
                .addParameter(valueTypeClassName, "dbData")
                .addStatement("return $L.toEnum(dbData)", converterField.name)
                .build();
    }

    public JavaFile toJavaFile() {
        final String packageName = firstNonBlank(this.packageName, enumType.packageName());
        final String converterName = join("", enumType.simpleNames()) + "AttributeConverter";

        final FieldSpec converterFieldSpec = converterFieldSpec(converterType, enumType, valueType);

        final TypeSpec attributeConverterImplSpec = TypeSpec.classBuilder(converterName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(toParameterizedTypeName(AttributeConverter.class, enumType, valueType))
                .addAnnotation( jpaConverterAnnotationSpec() )
                .addField( converterFieldSpec(converterType, enumType, valueType) )
                .addMethod( defaultConstructorMethodSpec(converterFieldSpec, converterType, enumType, valueType) )
                .addMethod( convertToDatabaseColumnMethodSpec(converterFieldSpec, enumType, valueType) )
                .addMethod( convertToEntityAttributeMethodSpec(converterFieldSpec, enumType, valueType) )
                .build();

        return JavaFile.builder(packageName, attributeConverterImplSpec)
                .skipJavaLangImports(true)
                .build();
    }

}
