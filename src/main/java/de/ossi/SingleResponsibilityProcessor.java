package de.ossi;

import com.google.auto.service.AutoService;
import de.ossi.annotation.SingleResponsibility;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

import static java.util.function.Predicate.not;

@SupportedAnnotationTypes({"de.ossi.annotation.SingleResponsibility"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class SingleResponsibilityProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(SingleResponsibility.class)
                .stream()
                .filter(this::isClass)
                .filter(this::hasMoreThanOneVisibleMethod)
                .findAny()
                .ifPresent(this::errorMsg);
        return true;
    }

    private boolean hasMoreThanOneVisibleMethod(Element classElement) {
        return classElement.getEnclosedElements()
                           .stream()
                           .filter(this::isMethod)
                           .filter(not(this::isPrivate))
                           .count() > 1;
    }

    private boolean isClass(Element element) {
        return element.getKind() == ElementKind.CLASS;
    }

    private boolean isMethod(Element e) {
        return e.getKind() == ElementKind.METHOD;
    }

    private boolean isPrivate(Element e) {
        return e.getModifiers()
                .contains(Modifier.PRIVATE);
    }

    private void errorMsg(Element element) {
        processingEnv.getMessager()
                     .printMessage(Diagnostic.Kind.ERROR,
                             String.format("The Class \"%s\" has more than one visible Method but is annotated with SingleResponsibility.",
                                     element.getSimpleName()), element);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }
}
