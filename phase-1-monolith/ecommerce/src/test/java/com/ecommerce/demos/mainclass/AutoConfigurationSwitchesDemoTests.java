// =============================================================================
// FOR LEARNING ONLY
// This is included purely to demonstrate two switches that control what
// @SpringBootApplication does at startup - spring.main.web-application-type
// and auto-configuration exclusion (spring.autoconfigure.exclude, the property
// twin of @SpringBootApplication(exclude = ...)). The E-Commerce product does
// not actually need either of them: it wants a web server and every recipe
// that applies. These tests exist so you can watch each switch change the
// outcome on your own machine. (Lesson 03, section 6.)
// =============================================================================

// WHY THIS PACKAGE: a dedicated "demos" area, one sub-package per lesson topic,
// so for-learning-only code is visibly separate from the real project's tests.
// It is still under com.ecommerce so it can use EcommerceApplication directly.
package com.ecommerce.demos.mainclass;

// WHY THIS IMPORT: the real main class. Every demo below starts the REAL
// application with one setting changed, so the only difference you observe is
// the effect of that one setting.
import com.ecommerce.EcommerceApplication;

// WHY THIS IMPORT: marks each method as a test for JUnit to run.
import org.junit.jupiter.api.Test;

// WHY THIS IMPORT: the same startup entry point main() uses.
import org.springframework.boot.SpringApplication;

// WHY THIS IMPORT: the "context that owns a web server" interface (moved to
// this package in Spring Boot 4 - see EcommerceApplicationContextTests).
import org.springframework.boot.web.server.context.WebServerApplicationContext;

// WHY THIS IMPORT: the exception type Spring throws when the context cannot
// finish starting because the web server could not be created.
import org.springframework.context.ApplicationContextException;

// WHY THIS IMPORT: the type run() returns; we close each one we start.
import org.springframework.context.ConfigurableApplicationContext;

// WHY THESE IMPORTS: AssertJ assertions. assertThatThrownBy runs a piece of
// code and then lets us describe the exception it MUST throw.
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// WHY THIS CLASS: three experiments, each flipping one switch and checking the
// consequence the lesson predicts.
class AutoConfigurationSwitchesDemoTests {

    // WHY THIS TEST: shows that the embedded server exists only because Boot
    // DECIDED this is a servlet web application. Override the decision and
    // there is no server at all - and nothing to keep the program alive.
    @Test
    void withWebApplicationTypeNoneThereIsNoServerAndNoDispatcherServlet() {
        // WHY try-with-resources: ConfigurableApplicationContext is
        // AutoCloseable, so Java calls close() for us when the block ends, even
        // if an assertion fails. Every context we start, we stop.
        try (ConfigurableApplicationContext context = SpringApplication.run(EcommerceApplication.class,
                // WHY THIS SETTING: overrides Boot's automatic detection. "none"
                // means: treat this as a plain program, not a web application.
                "--spring.main.web-application-type=none")) {

            // WHY: the context still starts - @SpringBootApplication still
            // scanned and still ran auto-configuration.
            assertThat(context.isActive()).isTrue();

            // WHY: but it is now an ordinary context that owns no web server.
            assertThat(context).isNotInstanceOf(WebServerApplicationContext.class);

            // WHY: DispatcherServletAutoConfiguration carries the condition
            // "only for a servlet web application". That condition now fails,
            // so the recipe is skipped and the object never exists.
            assertThat(context.containsBean("dispatcherServlet")).isFalse();
        }
    }

    // WHY THIS TEST: shows that Tomcat really does come from ONE auto-
    // configuration recipe. Remove that recipe and the web application cannot
    // start - the same failure you get if you drop @EnableAutoConfiguration
    // from the main class (Lesson 03, section 4).
    @Test
    void excludingTheTomcatRecipeMeansTheWebApplicationCannotStart() {
        // WHY assertThatThrownBy: we EXPECT startup to fail; the test passes
        // only if it fails in exactly the predicted way.
        assertThatThrownBy(() -> SpringApplication.run(EcommerceApplication.class,
                // WHY: avoid clashing with a copy of the app on 8080 (we never
                // get as far as binding a port here, but it keeps this demo
                // harmless if the prediction turns out wrong).
                "--server.port=0",
                // WHY THIS SETTING: removes one recipe from the list before any
                // conditions are checked. This is the Boot 4 location of the
                // class that defines the Tomcat factory (verified against the
                // 4.1.1 source); in 3.x the equivalent was a different class
                // in a different package.
                "--spring.autoconfigure.exclude=org.springframework.boot.tomcat.autoconfigure.servlet.TomcatServletWebServerAutoConfiguration"))
                // WHY: the servlet web context wraps any web-server failure in
                // this exception type during its "create the server" step.
                .isInstanceOf(ApplicationContextException.class)
                // WHY: the wrapper's message, set by the context itself.
                .hasMessageContaining("Unable to start web server")
                // WHY: the deepest cause names exactly what was missing - the
                // object that knows how to build a servlet web server.
                .rootCause()
                .hasMessageContaining("missing ServletWebServerFactory bean");
    }

    // WHY THIS TEST: the dangerous Boot 4 gotcha. An exclusion written as TEXT
    // (in a property, or excludeName) that names a class which does not exist
    // is IGNORED, with no warning. Many 3.x-era tutorials exclude
    // "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration";
    // in Boot 4 that class moved to org.springframework.boot.jdbc.autoconfigure,
    // so the old text silently does nothing.
    @Test
    void excludingANameThatDoesNotExistIsSilentlyIgnored() {
        // WHY try-with-resources: close the context when done, as always.
        try (ConfigurableApplicationContext context = SpringApplication.run(EcommerceApplication.class,
                // WHY: use a random free port so this demo never collides with
                // a copy of the app you may have running on 8080.
                "--server.port=0",
                // WHY: the Boot 3.x package name - it is not on our classpath at
                // all, so Boot's check ("is this class present but not an
                // auto-configuration?") never fires.
                "--spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")) {

            // WHY: startup succeeded as if the exclusion were not there. No
            // error, no warning. If that exclusion had been important, you
            // would now be debugging a symptom far away from its cause.
            assertThat(context.isActive()).isTrue();
        }
    }

}
