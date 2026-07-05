// ReceiptSender.java
// FOR LEARNING ONLY: this whole `demos/ioc` package exists purely to demonstrate
// Inversion of Control and Dependency Injection as PLAIN-JAVA principles, before
// Spring's annotations arrive in Lesson 05. The E-Commerce product does not use
// these classes yet; they are here so you can SEE tight coupling and its cure with
// your own hands, and run a test that proves the difference. Nothing here is a
// Spring bean (no @Component), so component scanning ignores it and the running app
// is completely unaffected.

// The package declaration: this file sits in src/main/java/com/ecommerce/demos/ioc,
// so its package name must mirror that folder path exactly, or the compiler refuses.
package com.ecommerce.demos.ioc;

// An interface: a NAMED PROMISE. It lists what any "receipt sender" must be able to
// do, without saying HOW. Why does a promise-with-no-body exist here? Because the
// whole point of this lesson is that code should depend on WHAT it needs ("something
// that can send a receipt"), not on one exact HOW ("this specific email class").
// The interface is that "what". Concrete classes below fulfil the promise different ways.
public interface ReceiptSender {

    // The single ability every receipt sender must provide: given an order id and a
    // customer's email, send them their receipt and return a short line describing
    // what was sent (we return a String only so a test can read and assert on it).
    // There is no method body here on purpose - an interface states the promise; the
    // classes that "implement" it supply the actual steps.
    String send(String orderId, String customerEmail);
}
