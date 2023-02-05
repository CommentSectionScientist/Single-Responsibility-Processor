package de.ossi;

import org.assertj.core.api.Assertions;
import org.joor.CompileOptions;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SingleResponsibilityProcessorTest {

    private final SingleResponsibilityProcessor processor = new SingleResponsibilityProcessor();

    @CsvSource({
            "OnePackagePrivateMethod," + onePackagePrivateMethod,
            "OneProtectedMethod," + oneProtectedMethod,
            "PublicAndPackagePrivateMethods," + publicAndPackagePrivateMethods,
            "PublicAndProtectedMethods," + publicAndProtectedMethods,
            "TwoPackagePrivateMethods," + twoPackagePrivateMethods,
            "TwoProtectedMethods," + twoProtectedMethods,
            "TwoPublicMethods," + twoPublicMethods,
    })
    @ParameterizedTest
    void shouldFail(String filename, String content) {
        Assertions.assertThatExceptionOfType(ReflectException.class)
                  .isThrownBy(() -> Reflect.compile(filename, content, new CompileOptions().processors(processor))
                                           .create()
                                           .get())
                  .withMessageContaining("SingleResponsibility");
    }

    private static final String onePackagePrivateMethod = "import de.ossi.annotation.SingleResponsibility; @SingleResponsibility public class OnePackagePrivateMethod { void method1(){} private void private Method1(){}}";
    private static final String oneProtectedMethod = "import de.ossi.annotation.SingleResponsibility; @SingleResponsibility public class OneProtectedMethod { protected void method1() {} private void private Method1() {}}";
    private static final String publicAndPackagePrivateMethods = "import de.ossi.annotation.SingleResponsibility; @SingleResponsibility public class PublicAndPackagePrivateMethods { public void method1() {} void method2() {} private void privateMethod1() {}}";
    private static final String publicAndProtectedMethods = "import de.ossi.annotation.SingleResponsibility; @SingleResponsibility public class PublicAndProtectedMethods { public void method1() {} protected void method2() {} private void privateMethod1() {}}";
    private static final String twoPackagePrivateMethods = "import de.ossi.annotation.SingleResponsibility; @SingleResponsibility public class TwoPackagePrivateMethods { void method1() {} void method2() {} private void privateMethod1() {}}";
    private static final String twoProtectedMethods = "import de.ossi.annotation.SingleResponsibility; @SingleResponsibility public class TwoProtectedMethods { protected void method1() {} protected void method2() {} private void privateMethod1() {} }";
    private static final String twoPublicMethods = "import de.ossi.annotation.SingleResponsibility; @SingleResponsibility public class TwoPublicMethods { public void method1() {} public void method2() {} private void privateMethod1() {} }";


    @CsvSource({
            "OnePublicMethod, " + onePublicMethod,
            "NoAnnotation, " + noAnnotation,
            "OnlyPrivateMethods, " + onlyPrivateMethods,
    })
    @ParameterizedTest
    void shouldNotFail(String filename, String content) {
        Reflect.compile(filename, content, new CompileOptions().processors(processor))
               .create()
               .get();
    }

    private static final String onePublicMethod = "import de.ossi.annotation.SingleResponsibility; @SingleResponsibility public class OnePublicMethod { public void method1() {} private void privateMethod1() {}}";
    private static final String noAnnotation = "public class NoAnnotation { public void method1(){} public void method2(){}}";
    private static final String onlyPrivateMethods = "import de.ossi.annotation.SingleResponsibility; @SingleResponsibility public class OnlyPrivateMethods { private void privateMethod1() {} private void privateMethod2() {}}";


}