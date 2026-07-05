// EcommerceApplication.java - the front door of the entire E-Commerce system.
// This is the class you start, and the class Spring Boot organizes everything around.

// The package declaration: Java's folder-like namespace for classes. It MUST mirror
// the folder path this file sits in (src/main/java/com/ecommerce/ <-> com.ecommerce),
// or the compiler refuses. Everything we build lives under com.ecommerce.
package com.ecommerce;

// Import: tells the compiler where the names used below actually live,
// so we can write short names instead of full addresses every time.
// SpringApplication = the class whose run(...) method boots the whole framework.
import org.springframework.boot.SpringApplication;
// The annotation (the @-label) that marks this class as a Spring Boot application.
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The sticky note from Lesson 01, now on a real class: it tells Spring Boot
// "this class is the application's front door - start here, scan for our classes
// from this package downward, and run auto-configuration." It is actually three
// annotations rolled into one, and Lesson 03 unpacks all three in full.
@SpringBootApplication
public class EcommerceApplication {

	// Plain old Java main(): the standard entry point of ANY Java program -
	// the JVM always begins here. Nothing Spring-specific about this line.
	public static void main(String[] args) {
		// The ignition. This one call runs the whole Lesson-01 startup sequence:
		// load the auto-configuration recipes, check their conditions against the
		// classpath, build what survives, start embedded Tomcat on port 8080, and
		// keep the application alive, listening for requests.
		// Arguments: WHICH class is the front door, plus any command-line args
		// passed through so Spring can read settings from them (used much later).
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
