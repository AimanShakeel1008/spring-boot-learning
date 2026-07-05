// EcommerceApplicationTests.java - the project's first automatic self-check.
// A test is code that checks other code and fails loudly if something is broken;
// `mvn test` runs every test in this folder tree. (Testing gets a whole chapter - 11;
// today's single test is the standard one Spring Initializr generates for every project.)

// Tests mirror the main code's package so they sit "next to" what they check.
package com.ecommerce;

// @Test comes from JUnit, Java's standard testing library (pulled in by our
// webmvc-test starter). It marks a method as one runnable test case.
import org.junit.jupiter.api.Test;
// @SpringBootTest marks a test that starts the ENTIRE application, for real,
// inside the test run - full treatment in Lesson 59.
import org.springframework.boot.test.context.SpringBootTest;

// The label that makes this a whole-application test: when it runs, Spring Boot
// performs its complete startup - recipes, conditions, wiring - exactly as if
// launched normally (just without opening the network port).
@SpringBootTest
class EcommerceApplicationTests {

	// One test case. Its body is EMPTY on purpose - and it still checks something
	// real: reaching the inside of this method means the whole application STARTED
	// successfully. Any broken configuration anywhere makes startup throw, which
	// fails this test before its (empty) body is ever reached.
	// In short: this test asserts "the application can boot." It will quietly
	// guard every future lesson - any change that breaks startup turns it red.
	@Test
	void contextLoads() {
	}

}
