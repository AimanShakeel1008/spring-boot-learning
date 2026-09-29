// WHY THE SAME PACKAGE AS THE MAIN CLASS: test classes live under
// src/test/java, a completely separate source folder, but they mirror the
// package structure of src/main/java. Same package name, different root folder.
// That gives a test access to package-private members of the class it is
// testing, and it keeps tests findable - the test for com.ecommerce.Foo is
// always at com.ecommerce.FooTests.
package com.ecommerce;

// WHY THIS IMPORT: @Test is the annotation that marks a method as a test.
// It comes from JUnit - the standard Java testing library. "jupiter" is the
// name of JUnit 5's programming model; you will see that word a lot and it
// simply means "JUnit 5 and later". Our pinned Boot version brings in
// JUnit Jupiter 6.0.3, chosen for us by the parent POM.
import org.junit.jupiter.api.Test;

// WHY THIS IMPORT: @SpringBootTest is the annotation that tells Spring's test
// support to start a real, complete Spring application before running the
// tests in this class.
import org.springframework.boot.test.context.SpringBootTest;

// WHY THIS ANNOTATION: without it, this would be a plain Java test - Spring
// would never start, and nothing about our configuration would be checked.
// With it, the test framework performs the full startup: it finds the class
// annotated with @SpringBootApplication (our EcommerceApplication), scans the
// com.ecommerce package, runs every auto-configuration rule, and builds the
// complete container of objects. If ANY part of that fails - a missing
// dependency, a typo in application.yml, two objects that cannot be told apart
// - startup throws, and the test fails.
@SpringBootTest
// WHY NOT public: JUnit 5 can run package-private test classes, and leaving
// "public" off is the modern convention because a test class is never used from
// outside its own package. It is a small readability win, nothing more.
class EcommerceApplicationTests {

    // WHY THIS ANNOTATION: it marks the method below as something to run. A
    // method without @Test inside a test class is just a helper method and will
    // be ignored.
    @Test
    // WHY A TEST WITH AN EMPTY BODY IS STILL A REAL TEST - this surprises
    // everyone, so it is worth being precise about.
    //
    // A test fails if it throws an exception. The work of this test happens
    // BEFORE the first line of the body: @SpringBootTest has to successfully
    // start the whole application in order for the method to be called at all.
    // If startup blows up, the exception propagates and the test is reported as
    // a failure - the empty body is never reached.
    //
    // So what this asserts is: "the application can start." That single fact
    // catches an enormous class of mistakes - a dependency you deleted, a bean
    // that cannot be created, a malformed YAML file, a duplicate definition.
    // It is the cheapest and most valuable test in a Spring project, and it is
    // why Spring Initializr generates it for you.
    void contextLoads() {
        // Deliberately empty. See the explanation above: the assertion is the
        // successful startup itself, not anything written in here.
    }

}
