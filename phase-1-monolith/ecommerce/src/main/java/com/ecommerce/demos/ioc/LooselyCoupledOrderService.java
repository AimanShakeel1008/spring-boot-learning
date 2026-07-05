// LooselyCoupledOrderService.java
// FOR LEARNING ONLY: this class shows the RIGHT WAY - the same service, but no longer
// welded to one sender. It does NOT build its dependency; it RECEIVES one. This is
// Dependency Injection done by hand, in plain Java, with zero Spring involved yet.
// Seeing it work without Spring first is the point: DI is a principle, and Spring is
// just a machine that automates it (Lesson 05 onward).

package com.ecommerce.demos.ioc;

public class LooselyCoupledOrderService {

    // The dependency, stored but NOT constructed here. Two changes from the wrong way,
    // both crucial:
    //   1. The type is the interface ReceiptSender (a WHAT), not a concrete class (a HOW).
    //      This service will accept ANY sender that keeps the promise - email, SMS, a fake.
    //   2. `final` means it is set exactly once, in the constructor below, and never
    //      swapped afterwards - so once wired, this service's collaborator can't change
    //      out from under it. Safe and predictable.
    private final ReceiptSender receiptSender;

    // THE CURE, expressed as a constructor. Instead of the service choosing its own
    // sender, whoever creates the service must HAND IN a sender here. Control over
    // "which sender" has been INVERTED: it moved OUT of the service and UP to the caller.
    // That inversion is the whole idea named "Inversion of Control". The act of passing
    // the dependency in through the constructor is "Dependency Injection".
    public LooselyCoupledOrderService(ReceiptSender receiptSender) {
        // Keep the handed-in sender for later use. We depend on WHAT was given, trusting
        // only that it keeps the ReceiptSender promise - we neither know nor care which
        // concrete class it is. That not-caring is exactly what makes this code flexible.
        this.receiptSender = receiptSender;
    }

    // The identical business action as the tightly-coupled version - but now the sender
    // it uses was decided by the caller, not by this class. Same code, radically more
    // flexible, because the coupling to a specific HOW is gone.
    public String placeOrder(String orderId, String customerEmail) {
        // (Pretend the order was validated and saved here.)
        return receiptSender.send(orderId, customerEmail);
    }
}
