// WHY THIS LINE: it declares the package - the folder-shaped namespace this
// class belongs to. It must match the folder path under src/main/java exactly:
// this file lives in src/main/java/com/ecommerce/, so the package is
// com.ecommerce. If the two disagree, the compiler refuses to compile the file.
// This particular package also matters for a second reason: Spring will later
// search THIS package and everything beneath it for your classes, and nowhere
// else. That is why the main class sits at the top of the tree. (Covered fully
// in Lesson 03.)
package com.ecommerce;

// WHY THIS IMPORT: SpringApplication is the class that actually starts a Spring
// Boot application. An import tells the compiler where to find a class we are
// about to use by its short name, so we can write "SpringApplication" instead
// of "org.springframework.boot.SpringApplication" every time.
import org.springframework.boot.SpringApplication;

// WHY THIS IMPORT: @SpringBootApplication is an annotation - a marker written
// with an at-sign that attaches information to a class for other code to read.
// It lives in the autoconfigure package because switching auto-configuration on
// is one of the three things it does.
import org.springframework.boot.autoconfigure.SpringBootApplication;

// WHY THIS ANNOTATION: it marks this class as the starting point of a Spring
// Boot application. It is a shorthand for three separate annotations at once:
// it says "this class may itself define objects for Spring", "search this
// package and below for my classes", and "switch on auto-configuration". We
// take all three apart properly in Lesson 03; for now, all you need is that
// removing it would leave an application that starts, configures nothing, and
// exits immediately.
@SpringBootApplication
// WHY A CLASS AT ALL: Java has no free-standing functions. Every method must
// live inside a class, so even a program whose only job is "start Spring" needs
// a class to hold its main method. "public" makes it visible to the launcher.
public class EcommerceApplication {

    // WHY THIS METHOD SIGNATURE, EXACTLY: this is Java's fixed entry point. When
    // the Java runtime starts a program it looks for a method that is public
    // (callable from outside), static (callable without first creating an
    // object - which matters, because at this instant no objects exist yet),
    // returns void (nothing to give back; the program simply ends), is named
    // main, and takes an array of Strings. Change any one of those five things
    // and the program will compile but refuse to start.
    public static void main(String[] args) {
        // WHY THIS CALL: this single line is the whole application. It tells
        // Spring Boot to start up, and it hands over two pieces of information.
        //
        // First argument - EcommerceApplication.class: a reference to THIS
        // class. Boot uses it as the origin point of its search: it reads the
        // class's package (com.ecommerce), then looks in that package and every
        // package below it for classes to manage. It also reads the annotations
        // on this class to learn that auto-configuration is switched on.
        //
        // Second argument - args: whatever was typed on the command line after
        // the program name. Boot turns those into configuration settings, so
        // someone can start the app on a different port without editing code.
        //
        // What happens inside this call, in order: Boot works out that this is
        // a web application, builds its container of objects, runs every
        // auto-configuration rule against the classpath, starts the embedded
        // Tomcat server, and then blocks - the method does not return while the
        // server is running. That is deliberate: the application stays alive
        // waiting for requests, and only ends when you stop it.
        SpringApplication.run(EcommerceApplication.class, args);
    }

}
