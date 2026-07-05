// ApplicationContextTests.java
// Turns Lesson 03's central claim into an automatic check: "SpringApplication.run
// produces a real ApplicationContext, and it is populated." If a future change ever
// breaks that, `mvn test` goes red here and tells us immediately.

// Same package as the code it checks, per our test convention.
package com.ecommerce;

// Static import of JUnit's assertions so we can write assertTrue(...) directly.
// An assertion is a claim that MUST hold; if it's false, the test fails.
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @Test marks one runnable test case (Lesson 02).
import org.junit.jupiter.api.Test;
// @Autowired asks Spring to hand a bean INTO this field for us (full story: Chapter 2).
import org.springframework.beans.factory.annotation.Autowired;
// @SpringBootTest starts the WHOLE application for real inside the test (Lesson 59),
// which means a genuine ApplicationContext is built - exactly what we want to inspect.
import org.springframework.boot.test.context.SpringBootTest;
// The container type we're asserting about - this lesson's star object.
import org.springframework.context.ApplicationContext;

// Boot the full app for this test class, producing a real context to examine.
@SpringBootTest
class ApplicationContextTests {

    // Spring injects the very ApplicationContext it built for this test run into this
    // field. That it can inject the context at all is itself proof the container exists.
    @Autowired
    private ApplicationContext context;

    // Claim 1: the context object is real (not null). If run(...) had failed to
    // produce a container, Spring couldn't have injected one and this would fail.
    @Test
    void applicationContextIsCreated() {
        // assertNotNull fails the test if 'context' is null. It won't be - proving
        // the container was built and handed to us.
        assertNotNull(context, "The ApplicationContext should have been created at startup");
    }

    // Claim 2: our own main class became a managed bean, proving @Configuration +
    // component scanning did their jobs, and that the container is genuinely populated.
    @Test
    void ourMainClassIsManagedAsABean() {
        // Spring names the bean after the class with a lowercase first letter.
        // containsBean returns true only if the container is actually holding it.
        assertTrue(context.containsBean("ecommerceApplication"),
                "'ecommerceApplication' should be a bean inside the context");
    }

    // Claim 3: the container is full, not empty. We assert a generous lower bound
    // (more than 50) rather than an exact count, because the precise number shifts
    // between Spring Boot versions - and pinning an exact number would make the test
    // brittle for no benefit. "Many beans exist" is the real thing we want to guarantee.
    @Test
    void contextHoldsManyBeans() {
        // getBeanDefinitionCount() is the same call the demo prints; here we assert
        // it comfortably exceeds a floor, confirming auto-configuration populated it.
        assertTrue(context.getBeanDefinitionCount() > 50,
                "Auto-configuration should have populated the context with many beans");
    }
}
