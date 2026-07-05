// ContextInspectorDemo.java
// FOR LEARNING ONLY: this class exists purely to demonstrate that the
// ApplicationContext from Lesson 03 is a real, inspectable object full of beans.
// The E-Commerce product does not need it; it just prints facts at startup so you
// can SEE the container. Delete it any time and the app behaves the same.

// It lives in a child package of com.ecommerce, so component scanning (Lesson 03)
// discovers it automatically - the very mechanism this lesson is about.
package com.ecommerce.demos;

// CommandLineRunner: the "run my code once, right after startup" hook (Lesson 03).
import org.springframework.boot.CommandLineRunner;
// ApplicationContext: the container itself - the star of this lesson. We ask Spring
// to hand us the very context it just built, so we can look inside it.
import org.springframework.context.ApplicationContext;
// @Component marks THIS class as one of our beans, so component scanning registers
// it and Spring will honor its CommandLineRunner role. (Full treatment: Chapter 2.)
import org.springframework.stereotype.Component;

// The label that turns this plain class into a Spring-managed bean. Without it,
// the scan would pass right over the class and the run() method would never fire.
@Component
public class ContextInspectorDemo implements CommandLineRunner {

    // A place to keep the context Spring gives us, so run() can use it below.
    // 'final' = set once in the constructor and never reassigned.
    private final ApplicationContext context;

    // Constructor injection (previewed here, taught fully in Lesson 06): because this
    // class is a bean, Spring builds it FOR us, and when it sees the constructor asks
    // for an ApplicationContext, it passes in the real running context. The container
    // is itself available as a bean you can receive - it can hand you a handle to itself.
    public ContextInspectorDemo(ApplicationContext context) {
        // Store the handed-in context for use after startup completes.
        this.context = context;
    }

    // The single method CommandLineRunner requires. Spring calls this ONCE, at the
    // very end of startup, after every bean exists and Tomcat is already listening.
    // 'args' are the command-line arguments (unused here); required by the interface.
    @Override
    public void run(String... args) {
        // Ask the context how many beans it is currently managing. This is the number
        // that proves the container is full even though WE have written no beans yet -
        // these are the ~200 beans auto-configuration created for the web layer.
        int beanCount = context.getBeanDefinitionCount();

        // Print a clearly-marked banner so it's easy to spot in the startup logs.
        System.out.println("==================================================");
        System.out.println("  [Lesson 03 demo] ApplicationContext is alive.");
        // The headline fact: the container holds this many beans right now.
        System.out.println("  Beans currently managed by the context: " + beanCount);
        // Prove OUR OWN main class became a bean too. Spring names beans by their
        // class name with a lowercase first letter, so EcommerceApplication ->
        // "ecommerceApplication". This confirms @Configuration made it a managed object.
        System.out.println("  Is 'ecommerceApplication' a bean? "
                + context.containsBean("ecommerceApplication"));
        // Prove the embedded Tomcat / web machinery is present: the DispatcherServlet
        // is the central web bean auto-configuration created (we meet it in Chapter 3).
        // Its presence here is the fingerprint of the web auto-configuration having run.
        System.out.println("  Is the web 'dispatcherServlet' bean present? "
                + context.containsBean("dispatcherServlet"));
        System.out.println("==================================================");
    }
}
