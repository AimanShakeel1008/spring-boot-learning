// WHY THIS PACKAGE: the same package as EcommerceApplication, mirrored under
// src/test/java, so the test sits next to the class it checks.
package com.ecommerce;

// WHY THIS IMPORT: @Test marks a method as a test for JUnit to run.
import org.junit.jupiter.api.Test;

// WHY THESE IMPORTS: the three annotations that @SpringBootApplication stands
// for, plus the two hidden one level further down. We import them so the test
// can ask "is THIS annotation present?" for each one by name.
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// WHY THIS IMPORT: AnnotatedElementUtils is Spring's own annotation reader. It
// follows annotations THROUGH other annotations (meta-annotations), which plain
// Java reflection does not. It is the same kind of lookup Spring performs at
// startup when it inspects our main class.
import org.springframework.core.annotation.AnnotatedElementUtils;

// WHY THIS IMPORT: assertThat comes from AssertJ, the assertion library that
// spring-boot-starter-test brings in. assertThat(x).isTrue() reads like a
// sentence and, when it fails, prints both what it expected and what it got.
import static org.assertj.core.api.Assertions.assertThat;

// WHY THIS TEST CLASS EXISTS: Lesson 03 claims that @SpringBootApplication is
// "really" three annotations. This class turns that claim into something the
// machine checks. It also guards the project: if someone ever replaces
// @SpringBootApplication with a half-copied set of annotations, these tests
// fail and say exactly which piece went missing.
//
// WHY THERE IS NO @SpringBootTest HERE: nothing in these tests needs a running
// application. We are only reading annotations off a class file. Leaving Spring
// un-started makes these tests take milliseconds instead of seconds.
class EcommerceApplicationAnnotationTests {

    // WHY THIS TEST: it shows the limit of plain Java. Java's own reflection
    // only sees the annotation physically written on the class. It has no idea
    // that @SpringBootApplication itself carries @Configuration - it never looks
    // inside an annotation. This is precisely why Spring had to write its own
    // annotation reader, and why "meta-annotations" are a Spring idea rather
    // than a Java one.
    @Test
    void plainJavaReflectionSeesOnlyTheAnnotationWrittenOnTheClass() {
        // WHY: this annotation IS written directly on the class, and it has
        // RUNTIME retention (it survives into the compiled class file and can
        // be read while the program runs), so Java can see it.
        assertThat(EcommerceApplication.class.isAnnotationPresent(SpringBootApplication.class)).isTrue();

        // WHY: @Configuration is NOT written on the class - it is written on
        // @SpringBootConfiguration, which is written on @SpringBootApplication.
        // Plain Java does not follow that chain, so it answers "not present".
        assertThat(EcommerceApplication.class.isAnnotationPresent(Configuration.class)).isFalse();
    }

    // WHY THIS TEST: the heart of the lesson. Spring's reader follows the chain
    // and finds every annotation hidden inside @SpringBootApplication.
    @Test
    void springSeesEveryAnnotationHiddenInsideSpringBootApplication() {
        // WHY: job 1 of 3. @SpringBootApplication carries this directly.
        assertThat(AnnotatedElementUtils.hasAnnotation(EcommerceApplication.class, SpringBootConfiguration.class)).isTrue();

        // WHY: two levels down (@SpringBootApplication -> @SpringBootConfiguration
        // -> @Configuration). This is what makes the main class a
        // "configuration class" - a source of setup instructions.
        assertThat(AnnotatedElementUtils.hasAnnotation(EcommerceApplication.class, Configuration.class)).isTrue();

        // WHY: three levels down (... -> @Configuration -> @Component). Proof
        // that our main class is itself a component Spring manages - the hook
        // Lesson 05 picks up.
        assertThat(AnnotatedElementUtils.hasAnnotation(EcommerceApplication.class, Component.class)).isTrue();

        // WHY: job 2 of 3. This one switches on the auto-configuration engine.
        assertThat(AnnotatedElementUtils.hasAnnotation(EcommerceApplication.class, EnableAutoConfiguration.class)).isTrue();

        // WHY: hidden inside @EnableAutoConfiguration. It records our package
        // (com.ecommerce) as "the application's package" for later features
        // such as database-entity scanning in Chapter 5.
        assertThat(AnnotatedElementUtils.hasAnnotation(EcommerceApplication.class, AutoConfigurationPackage.class)).isTrue();

        // WHY: job 3 of 3. This one searches our package tree for classes.
        assertThat(AnnotatedElementUtils.hasAnnotation(EcommerceApplication.class, ComponentScan.class)).isTrue();
    }

    // WHY THIS TEST: it pins down WHERE scanning starts and what it skips,
    // straight from the annotation's own settings.
    @Test
    void componentScanStartsAtTheMainClassPackageAndCarriesTwoExcludeFilters() {
        // WHY findMergedAnnotation: it returns the @ComponentScan as Spring
        // actually sees it after combining it with the attributes set on
        // @SpringBootApplication (for example scanBasePackages is declared as
        // an alias that feeds @ComponentScan's basePackages).
        ComponentScan scan = AnnotatedElementUtils.findMergedAnnotation(EcommerceApplication.class, ComponentScan.class);

        // WHY: guards against a null result before we read its attributes, so
        // a failure here says "not found" instead of a confusing
        // NullPointerException on the next line.
        assertThat(scan).isNotNull();

        // WHY: an EMPTY list of base packages is not "scan nothing" - it means
        // "fall back to the package of the class that carries this
        // annotation". That fallback is the entire reason the main class's
        // package is the scanning boundary.
        assertThat(scan.basePackages()).isEmpty();

        // WHY: @SpringBootApplication configures exactly two exclude filters -
        // TypeExcludeFilter (an extension point that lets test support hide
        // test-only classes) and AutoConfigurationExcludeFilter (stops
        // auto-configuration recipes that happen to sit in a scanned package
        // from being loaded twice, bypassing their conditions).
        assertThat(scan.excludeFilters()).hasSize(2);
    }

}
