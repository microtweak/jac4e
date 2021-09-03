package com.github.microtweak.jac4e.processor;

import com.github.microtweak.jac4e.processor.spi.JavaFileGeneratorFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.util.Set;

import static com.github.microtweak.jac4e.processor.SupportedOptions.*;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

@SupportedOptions({ PACKAGE_NAME, ERROR_IF_VALUE_NOT_FOUND, ATTRIBUTE_NAME })
public class Processor extends AbstractProcessor {

    private static final boolean ANNOTATIONS_CLAIMED_EXCLUSIVELY = false;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return JavaFileGeneratorFactory.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || annotations.isEmpty()) {
            return ANNOTATIONS_CLAIMED_EXCLUSIVELY;
        }

        for (TypeElement enumTypeElement : lookAnnotatedEnumElements(annotations, roundEnv)) {
            try {
                JavaFileGeneratorFactory.processEnums(enumTypeElement, processingEnv);
            } catch (Exception e) {
                final String msg = format("Failed to generate AttributeConverter for enum \"%s\": %s", enumTypeElement.getQualifiedName(), e.getMessage());
                processingEnv.getMessager().printMessage(Kind.ERROR, msg, enumTypeElement);
            }
        }

        return ANNOTATIONS_CLAIMED_EXCLUSIVELY;
    }

    private Set<TypeElement> lookAnnotatedEnumElements(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return annotations.stream()
                .flatMap(annotation -> roundEnv.getElementsAnnotatedWith(annotation).stream())
                .filter(e -> e.getKind() == ElementKind.ENUM)
                .map(e -> (TypeElement) e)
                .collect(toSet());
    }

}
