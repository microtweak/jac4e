package com.github.microtweak.jac4e.processor.spi;

import com.github.microtweak.jac4e.processor.generator.JavaFileGenerator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.String.format;

public interface JavaFileGeneratorFactory<A extends Annotation> {

    static Set<String> getSupportedAnnotationTypes() {
        return StreamSupport
                .stream(ServiceLoader.load(JavaFileGeneratorFactory.class, JavaFileGeneratorFactory.class.getClassLoader()).spliterator(), false)
                .map(f -> f.getSupportedAnnotationType().getCanonicalName())
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("rawtypes")
    static void processEnums(TypeElement enumTypeElement, ProcessingEnvironment processingEnv) throws IOException {
        final Set<JavaFileGeneratorFactory> supportedGenerators = StreamSupport
                .stream(ServiceLoader.load(JavaFileGeneratorFactory.class, JavaFileGeneratorFactory.class.getClassLoader()).spliterator(), false)
                .filter(f -> f.isSupported(enumTypeElement))
                .collect(Collectors.toSet());

        final boolean forceDisableAutoApply = supportedGenerators.size() > 1;

        if (forceDisableAutoApply) {
            final String msg = "Option \"autoApply\" disabled for enum \"%s\". Annotations found in this enum: %s";
            final String foundAnnotations = supportedGenerators.stream()
                    .map(s -> "@" + s.getSupportedAnnotationType().getSimpleName())
                    .collect(Collectors.joining(", "));

            processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING, format(msg, enumTypeElement.getQualifiedName(), foundAnnotations));
        }

        for (JavaFileGeneratorFactory<?> factory : supportedGenerators) {
            final String msg = format("Processing %s annotation for the \"%s\" enum...", factory.getSupportedAnnotationType().getSimpleName(), enumTypeElement.getSimpleName());
            processingEnv.getMessager().printMessage(Kind.NOTE, msg);

            factory.create(processingEnv, enumTypeElement, forceDisableAutoApply)
                    .toJavaFile()
                    .writeTo(processingEnv.getFiler());
        }
    }

    Class<A> getSupportedAnnotationType();

    boolean isSupported(TypeElement element);

    JavaFileGenerator create(ProcessingEnvironment processingEnv, TypeElement enumTypeElement, boolean forceDisableAutoApply);

}
