// WHY THIS LINE: it declares the package - the folder-shaped namespace this
// class belongs to. It must match the folder path under src/main/java exactly:
// this file lives in src/main/java/com/ecommerce/, so the package is
// com.ecommerce. If the two disagree, the compiler refuses to compile the file.
//
// This package matters for a second, bigger reason (Lesson 03): it is the
// STARTING POINT of Spring's search for our classes. Component scanning begins
// at com.ecommerce and covers every package BENEATH it - com.ecommerce.product,
// com.ecommerce.order, and so on - and nothing outside it. That is why this
// class sits at the very top of our package tree and must stay there. Move it
// one level down and every class above or beside it silently becomes invisible.
package com.ecommerce;

// WHY THIS IMPORT: SpringApplication is the class that performs the whole
// startup sequence - it decides what kind of application this is, builds the
// ApplicationContext, runs auto-configuration and starts the embedded server.
// Importing it lets us write the short name instead of the full
// "org.springframework.boot.SpringApplication" every time.
import org.springframework.boot.SpringApplication;

// WHY THIS IMPORT: @SpringBootApplication lives in the "autoconfigure" package
// because switching on auto-configuration is one of the three jobs it bundles.
// This package did NOT move in Spring Boot 4 (verified against the 4.1.1
// source) - unlike the web-server classes, which did.
import org.springframework.boot.autoconfigure.SpringBootApplication;

// WHY THIS ANNOTATION: it is a shorthand for three annotations at once. Spring
// reads through it and finds all three (our EcommerceApplicationAnnotationTests
// prove this):
//
//   @SpringBootConfiguration  (which itself carries @Configuration)
//       "this class is a source of setup instructions for the container" -
//       so the class itself becomes a managed object, and later it may hold
//       @Bean methods (Lesson 07).
//
//   @EnableAutoConfiguration
//       "read every auto-configuration recipe on the classpath and apply the
//       ones whose conditions pass" - this is what creates the embedded Tomcat,
//       the DispatcherServlet, the JSON converter and roughly a hundred more
//       objects we never wrote.
//
//   @ComponentScan
//       "search this class's package and everything beneath it for classes
//       Spring should manage" - the reason the package line above matters.
//
// Remove @EnableAutoConfiguration alone and startup FAILS: this is still
// detected as a web application, but no recipe ever defines a web server
// factory, so Spring has no way to build Tomcat and reports
// "Web application could not be started as there was no ...ServletWebServerFactory
// bean defined in the context." (Lesson 03, section 4.)
@SpringBootApplication
// WHY A CLASS AT ALL: Java has no free-standing functions. Every method must
// live inside a class, so even a program whose only job is "start Spring" needs
// a class to hold its main method. "public" makes it visible to the launcher.
// Note: because of @SpringBootApplication, Spring ALSO creates an object of
// this class and keeps it in the container under the name
// "ecommerceApplication". Our main class is itself one of Spring's managed
// objects.
public class EcommerceApplication {

    // WHY THIS METHOD SIGNATURE, EXACTLY: this is Java's fixed entry point. When
    // the Java runtime starts a program it looks for a method that is public
    // (callable from outside), static (callable without first creating an
    // object - which matters, because at this instant no objects exist yet),
    // returns void (nothing to give back), is named main, and takes an array
    // of Strings. Change any one of those and the program will compile but
    // refuse to start.
    public static void main(String[] args) {
        // WHY THIS CALL: this single line is the whole application. It hands
        // Spring Boot two pieces of information.
        //
        // First argument - EcommerceApplication.class: a reference to THIS
        // class, called the "primary source". Boot registers it as the first
        // object in the container, then reads its annotations - which is how
        // @SpringBootApplication gets noticed at all - and uses its package
        // (com.ecommerce) as the starting point for scanning.
        //
        // Second argument - args: whatever was typed on the command line after
        // the program name. Boot turns every "--name=value" into a setting with
        // the HIGHEST priority, above application.yml, so someone can start the
        // app with --server.port=9090 without editing any file.
        //
        // What happens inside, in order (Lesson 03, section 3.9): decide the
        // application type (servlet web, because Spring MVC is on the
        // classpath); create the ApplicationContext; register this class;
        // "refresh" the context - scan, run auto-configuration, create the
        // embedded Tomcat, create every singleton object, start Tomcat
        // listening on port 8080; then RETURN the finished context.
        //
        // CORRECTION to what Lesson 02 said: run() does NOT block forever. It
        // returns as soon as startup is complete, and then main() ends too. The
        // application keeps running anyway because Tomcat started a thread of
        // its own ("container-0") that is marked non-daemon - and the Java
        // runtime refuses to exit while any non-daemon thread is still alive.
        // Ctrl+C triggers the shutdown hook, which closes the context, which
        // stops Tomcat, which ends that thread - and only then does Java exit.
        //
        // We ignore the returned context here because nothing in main() needs
        // it. Our tests (EcommerceApplicationContextTests) capture it and look
        // inside.
        SpringApplication.run(EcommerceApplication.class, args);
    }

}
