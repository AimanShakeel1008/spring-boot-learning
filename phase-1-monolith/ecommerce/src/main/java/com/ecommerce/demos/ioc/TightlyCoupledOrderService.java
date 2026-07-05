// TightlyCoupledOrderService.java
// FOR LEARNING ONLY: this class deliberately shows the WRONG WAY - tight coupling.
// It builds its OWN dependency with `new` inside itself. Read it, then read
// LooselyCoupledOrderService.java to see the cure. This class exists only to be
// compared against; the product never uses it.

package com.ecommerce.demos.ioc;

// A service that places an order and then sends a receipt. This is the naive version
// every beginner writes first, and it looks perfectly reasonable - which is exactly
// why the trap is worth seeing clearly.
public class TightlyCoupledOrderService {

    // THE PROBLEM LINE. This service reaches out and CONSTRUCTS its own receipt sender
    // with `new`, hard-wiring itself to one exact class: EmailReceiptSender. From this
    // moment the service and the email class are welded together ("tightly coupled").
    // Consequences, all bad:
    //   - You cannot swap email for SMS without EDITING this file.
    //   - You cannot test placeOrder() without a REAL EmailReceiptSender running.
    //   - This service now decides which sender exists - a job that isn't really its own.
    // Note the type is the concrete EmailReceiptSender, not the ReceiptSender promise:
    // the service is nailed to a HOW, not depending on a WHAT.
    private final EmailReceiptSender receiptSender = new EmailReceiptSender();

    // The order-placing action. Imagine real work here: check stock, save the order,
    // charge payment. We keep it to the essence so the coupling is the only thing on show.
    public String placeOrder(String orderId, String customerEmail) {
        // (Pretend the order was validated and saved here.)
        // Then send the receipt using the sender we hard-wired above. Because that
        // sender is fixed to email, this line can NEVER do anything but email - the
        // caller has no say, and a test has no way to substitute a harmless fake.
        return receiptSender.send(orderId, customerEmail);
    }
}
