// WHY THIS PACKAGE: mirrors EcommerceApplication's package under src/test/java.
package com.ecommerce;

// WHY THESE IMPORTS: JUnit's lifecycle annotations. @BeforeAll runs once before
// every test in the class, @AfterAll once after all of them. We use them so
// the application starts ONCE for the whole class instead of once per test -
// starting Spring takes a couple of seconds, and all four tests below only
// read from the same running application.
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

// WHY THIS IMPORT: the exact same class our main() method calls. These tests
// start the application the way main() does, so what they observe is what
// main() produces.
import org.springframework.boot.SpringApplication;

// WHY THIS IMPORT: a small Spring Boot helper that reports which package was
// recorded as "the application's package" by @AutoConfigurationPackage.
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;

// WHY THIS IMPORT: the concrete class of the embedded Tomcat wrapper. NOTE THE
// PACKAGE: in Spring Boot 3.x this class lived in
// org.springframework.boot.web.embedded.tomcat. Spring Boot 4 moved it into the
// Tomcat module's own package, org.springframework.boot.tomcat (verified
// against the 4.1.1 source). Old tutorials will show the old import.
import org.springframework.boot.tomcat.TomcatWebServer;

// WHY THIS IMPORT: the interface every "application context that owns a web
// server" implements. It adds one method we need: getWebServer(). Also moved
// in Boot 4 - 3.x had it in org.springframework.boot.web.context.
import org.springframework.boot.web.server.context.WebServerApplicationContext;

// WHY THIS IMPORT: the type SpringApplication.run() returns. "Configurable"
// means it is not only readable (ask it for objects) but also controllable
// (refresh it, close it).
import org.springframework.context.ConfigurableApplicationContext;

// WHY THIS IMPORT: Spring MVC's front-door servlet. We look it up to prove that
// auto-configuration created it without us writing a line.
import org.springframework.web.servlet.DispatcherServlet;

// WHY THIS IMPORT: AssertJ's readable assertions, supplied by the test starter.
import static org.assertj.core.api.Assertions.assertThat;

// WHY THIS TEST CLASS EXISTS: Lesson 03 calls the ApplicationContext "the brain"
// of the application. This class opens the brain and checks what is inside,
// using the same call main() uses. It guards four facts that the rest of the
// course depends on: run() returns a live context; our main class is one of
// its managed objects; auto-configuration built an embedded Tomcat and a
// DispatcherServlet; and the context can be shut down cleanly.
//
// WHY NOT @SpringBootTest: @SpringBootTest would start the application for us,
// but in its default mode it does NOT start a real server - it uses a
// simulated one. We want the real embedded Tomcat, exactly as main() creates
// it, so we call SpringApplication.run ourselves.
class EcommerceApplicationContextTests {

    // WHY static: @BeforeAll and @AfterAll methods must be static in JUnit's
    // default mode, so the context they share must be static too.
    private static ConfigurableApplicationContext context;

    // WHY THIS METHOD: start the application once, exactly as main() does, but
    // with one extra command-line setting.
    @BeforeAll
    static void startTheApplicationExactlyAsMainDoes() {
        // WHY "--server.port=0": port 0 asks the operating system for any free
        // port. Without it, this test would try to take port 8080 and fail with
        // "Port 8080 was already in use" whenever you also have the app running
        // in another terminal. Command-line settings beat application.yml, so
        // this overrides the server.port: 8080 written there.
        context = SpringApplication.run(EcommerceApplication.class, "--server.port=0");
    }

    // WHY THIS METHOD: always shut the application down after the tests, so
    // Tomcat's thread and port are released. Otherwise every test run would
    // leave a live server behind until the test process exits.
    @AfterAll
    static void closeTheApplication() {
        // WHY THE NULL CHECK: if startup itself failed, context was never
        // assigned. Closing null would throw a NullPointerException and hide
        // the real startup error behind a second, irrelevant one.
        if (context != null) {
            context.close();
        }
    }

    // WHY THIS TEST: proves run() RETURNS (it does not block forever), that
    // what it returns is running, and that our own main class lives inside it.
    @Test
    void runReturnsALiveContextThatHoldsOurMainClassAsAManagedObject() {
        // WHY: we only reach this line because run() returned. isActive() is
        // true between a successful startup ("refresh") and close().
        assertThat(context.isActive()).isTrue();

        // WHY "ecommerceApplication": the container names every managed object.
        // For a class registered by Spring, the default name is the simple
        // class name with its first letter lower-cased.
        assertThat(context.containsBean("ecommerceApplication")).isTrue();

        // WHY: asking by TYPE instead of by name. getBean hands back the object
        // Spring created from our main class. isNotNull() proves it exists.
        assertThat(context.getBean(EcommerceApplication.class)).isNotNull();

        // WHY: we wrote exactly ONE class, yet the context holds far more
        // objects. Every one of the others came from auto-configuration. The
        // real number on your machine will be much larger than 10; 10 is a
        // deliberately safe floor that still proves the point.
        assertThat(context.getBeanDefinitionCount()).isGreaterThan(10);

        // WHY: the context also owns the Environment - every setting from
        // application.yml and the command line, merged. This value comes from
        // spring.application.name in our application.yml.
        assertThat(context.getEnvironment().getProperty("spring.application.name")).isEqualTo("ecommerce");
    }

    // WHY THIS TEST: proves the embedded server is real, is Tomcat, and was
    // created by auto-configuration - we never wrote "new Tomcat()" anywhere.
    @Test
    void autoConfigurationStartedARealEmbeddedTomcat() {
        // WHY: Spring MVC on the classpath made Boot choose a servlet web
        // application, whose context type owns a web server.
        assertThat(context).isInstanceOf(WebServerApplicationContext.class);

        // WHY THE CAST: the variable's declared type does not have
        // getWebServer(); the check above proved the object does.
        WebServerApplicationContext webContext = (WebServerApplicationContext) context;

        // WHY: the server object is Tomcat, created by the
        // TomcatServletWebServerAutoConfiguration recipe.
        assertThat(webContext.getWebServer()).isInstanceOf(TomcatWebServer.class);

        // WHY: port 0 was a request for "any free port"; once Tomcat is
        // listening, it reports the real port the operating system gave it,
        // which is always a positive number.
        assertThat(webContext.getWebServer().getPort()).isGreaterThan(0);

        // WHY: the DispatcherServlet - Spring MVC's front door from Lesson 02 -
        // was created by DispatcherServletAutoConfiguration under this name.
        assertThat(context.getBean("dispatcherServlet")).isInstanceOf(DispatcherServlet.class);
    }

    // WHY THIS TEST: proves @AutoConfigurationPackage (hidden inside
    // @EnableAutoConfiguration) recorded our main class's package.
    @Test
    void theApplicationPackageIsTheMainClassPackage() {
        // WHY: exactly one package, and it is ours. Chapter 5 uses this list to
        // find database entity classes; if the main class moved, this list
        // would move with it.
        assertThat(AutoConfigurationPackages.get(context)).containsExactly("com.ecommerce");
    }

    // WHY THIS TEST: proves close() really stops the application. We start a
    // SECOND, separate copy here so that closing it cannot disturb the shared
    // context the other tests are reading.
    @Test
    void closingTheContextShutsTheWholeApplicationDown() {
        // WHY a second run: one context per test would be slow, but this one
        // test needs a context it is allowed to destroy.
        ConfigurableApplicationContext throwaway = SpringApplication.run(EcommerceApplication.class, "--server.port=0");

        // WHY: sanity check - it started.
        assertThat(throwaway.isActive()).isTrue();

        // WHY: close() destroys every managed object in reverse order and stops
        // the embedded Tomcat. This is the same thing Ctrl+C triggers.
        throwaway.close();

        // WHY: after close the context is no longer active; asking it for
        // objects would now throw an exception.
        assertThat(throwaway.isActive()).isFalse();
    }

}
