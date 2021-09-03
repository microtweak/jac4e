package com.github.microtweak.jac4e.processor.generator;

import com.github.microtweak.jac4e.core.EnumConverter;
import com.github.microtweak.jac4e.core.impl.ClassAttributeEnumConverter;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@Setter
@Accessors(fluent = true)
public class ClassFieldAttributeConverterJavaFileGenerator extends DefaultAttributeConverterJavaFileGenerator {

    private String attributeName;

    public ClassFieldAttributeConverterJavaFileGenerator() {
        super.converterType(ClassAttributeEnumConverter.class);
    }

    @Override
    public DefaultAttributeConverterJavaFileGenerator converterType(Class<? extends EnumConverter> converterType) {
        final String msg = "Object %s does not allow overwriting the value of %s!";
        throw new UnsupportedOperationException(format(msg, getClass().getSimpleName(), "converterType"));
    }

    @Override
    protected void customStatementDefaultConstructor(MethodSpec.Builder constructorMethodSpec, FieldSpec converterField) {
        if (isNotBlank(attributeName)) {
            constructorMethodSpec.addStatement("$L.setAttributeName($S)", converterField.name, attributeName);
        }
    }

}
