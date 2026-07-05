// EmailReceiptSender.java
// FOR LEARNING ONLY (see ReceiptSender.java for the package-wide disclaimer).
// This is ONE concrete way to fulfil the ReceiptSender promise: by email. In a real
// system this would call an email provider; here it just builds and prints a line,
// because the lesson is about WIRING, not about really sending mail.

package com.ecommerce.demos.ioc;

// "implements ReceiptSender" = this class formally keeps the promise the interface
// made. The compiler now FORCES us to provide a real body for every method the
// interface listed (here, send). If we forgot one, the code would not compile - that
// enforced completeness is a big reason interfaces are worth using.
public class EmailReceiptSender implements ReceiptSender {

    // @Override is a label that says "this method is fulfilling a method promised by a
    // parent type (the interface)." It is optional but valuable: if we mistype the name
    // or the parameters so it no longer matches the interface, the compiler errors
    // instead of silently creating an unrelated method. A safety net, checked at compile time.
    @Override
    public String send(String orderId, String customerEmail) {
        // Build the human-readable description of what we "sent". String concatenation
        // with + just glues the pieces together into one line.
        String receipt = "EMAIL receipt for order " + orderId + " sent to " + customerEmail;
        // Print it so that when this runs you can SEE which sender did the work - this is
        // how the demo makes the otherwise-invisible wiring visible in the console.
        System.out.println("[EmailReceiptSender] " + receipt);
        // Hand the description back to the caller so a test can assert on it, or a
        // service can log it. Returning a value keeps this method easy to check.
        return receipt;
    }
}
