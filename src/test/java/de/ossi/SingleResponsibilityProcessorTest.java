package de.ossi;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

class SingleResponsibilityProcessorTest {

    private final SingleResponsibilityProcessor processor = new SingleResponsibilityProcessor();

    @CsvSource({
            "PublicAndPackagePrivateMethods",
            "PublicAndProtectedMethods",
            "TwoPackagePrivateMethods",
            "TwoProtectedMethods",
            "TwoPublicMethods",
    })
    @ParameterizedTest
    void atLeastTwoVisibleMethodsShouldFail(String filename) {
        Compilation compilation = javac()
                .withProcessors(processor)
                .compile(JavaFileObjects.forResource(toJavaFile(filename)));
        assertThat(compilation)
                .failed();
        assertThat(compilation)
                .hadErrorContaining("SingleResponsibility");
        //TODO asserts
    }

    @CsvSource({
            "OnePublicMethod",
            "OneProtectedMethod",
            "NoAnnotation",
            "OnlyPrivateMethods",
            "OnePackagePrivateMethod",
    })
    @ParameterizedTest
    void oneVisibleMethodShouldNotFail(String filename) {
        Compilation compilation = javac()
                .withProcessors(processor)
                .compile(JavaFileObjects.forResource(toJavaFile(filename)));
        assertThat(compilation)
                .succeeded();
    }

    private String toJavaFile(String filename) {
        return filename + ".java";
    }
}