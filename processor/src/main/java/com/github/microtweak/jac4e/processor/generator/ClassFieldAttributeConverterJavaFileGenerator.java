package com.github.microtweak.jac4e.processor.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@Setter
@Accessors(fluent = true)
public class ClassFieldAttributeConverterJavaFileGenerator extends DefaultAttributeConverterJavaFileGenerator {

    private String attributeName;

    @Override
    protected void customStatementDefaultConstructor(MethodSpec.Builder constructorMethodSpec, FieldSpec converterField) {
        if (isNotBlank(attributeName)) {
            constructorMethodSpec.addStatement("$L.setAttributeName($S)", converterField.name, attributeName);
        }
    }

}
