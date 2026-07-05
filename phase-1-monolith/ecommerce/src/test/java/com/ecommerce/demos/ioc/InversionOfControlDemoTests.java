// InversionOfControlDemoTests.java
// FOR LEARNING ONLY: this test does not check the E-Commerce product. It exists to
// PROVE, with assertions the machine will enforce, why loose coupling (Dependency
// Injection) beats tight coupling. If a future change breaks the point this lesson
// makes, these tests turn red. It is plain JUnit - no Spring is started here at all,
// which is itself the message: DI is a plain-Java idea, and you can feel its payoff
// with nothing but `new` and an interface.

package com.ecommerce.demos.ioc;

// JUnit's @Test marks one method as a single runnable test case (met in Lesson 02).
import org.junit.jupiter.api.Test;
// Static imports of the assertion helpers, so we can write assertEquals(...) and
// assertTrue(...) directly instead of Assertions.assertEquals(...). An assertion is a
// claim the test makes; if the claim is false, the test fails loudly.
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// The test class. Its name ends in "Tests" by convention so Maven's test runner picks
// it up automatically during `mvn test`.
class InversionOfControlDemoTests {

    // ---------------------------------------------------------------------------
    // A hand-written FAKE sender, living only inside this test file. It keeps the
    // ReceiptSender promise but does NOTHING real - it just records that it was called
    // and with what. This is called a "test double" or "stub": a stand-in used in place
    // of a real collaborator so a test stays fast, offline, and predictable. The ONLY
    // reason we can slot this in is that the loosely-coupled service accepts any
    // ReceiptSender. Tight coupling would make this fake impossible to use.
    // ---------------------------------------------------------------------------
    static class RecordingReceiptSender implements ReceiptSender {
        // Flags/fields that remember what happened, so the test can inspect them afterwards.
        boolean wasCalled = false;      // did anyone actually call send()?
        String lastOrderId = null;      // what order id were we asked to send for?
        String lastEmail = null;        // which email address did we receive?

        @Override
        public String send(String orderId, String customerEmail) {
            // Record the call instead of really sending anything.
            wasCalled = true;
            lastOrderId = orderId;
            lastEmail = customerEmail;
            // Return a canned, recognizable line so the test can assert we ran THIS fake
            // and not some real sender.
            return "FAKE receipt for order " + orderId;
        }
    }

    // TEST 1: the payoff of loose coupling - the service is testable in ISOLATION.
    // We inject our harmless fake, call placeOrder, and verify the service actually used
    // the dependency we handed it. No email class runs, no network, nothing real happens.
    @Test
    void looselyCoupledService_usesWhateverSenderYouInject() {
        // Arrange: build the fake, then hand it to the service through its constructor.
        // THIS is dependency injection - WE, the caller, decide the collaborator.
        RecordingReceiptSender fakeSender = new RecordingReceiptSender();
        LooselyCoupledOrderService service = new LooselyCoupledOrderService(fakeSender);

        // Act: run the business action.
        String result = service.placeOrder("ORD-1", "amy@example.com");

        // Assert: the service delegated to the sender WE provided (not some hidden one),
        // proving control over the dependency really lives with the caller now.
        assertTrue(fakeSender.wasCalled, "the injected sender should have been used");
        assertEquals("ORD-1", fakeSender.lastOrderId, "the order id should reach the sender");
        assertEquals("amy@example.com", fakeSender.lastEmail, "the email should reach the sender");
        // And the value that came back is our fake's canned line - so we KNOW the fake,
        // not a real EmailReceiptSender, did the work.
        assertEquals("FAKE receipt for order ORD-1", result);
    }

    // TEST 2: swap the implementation with ZERO changes to the service. Same service
    // class, a different sender handed in, and it just works. This is the flexibility
    // tight coupling cannot offer - here we pass the REAL EmailReceiptSender.
    @Test
    void looselyCoupledService_worksWithTheRealEmailSenderToo() {
        // Inject the real email sender this time - the service code is untouched.
        LooselyCoupledOrderService service =
                new LooselyCoupledOrderService(new EmailReceiptSender());

        String result = service.placeOrder("ORD-2", "ben@example.com");

        // The real email sender's line starts with "EMAIL", proving the SAME service
        // now behaves differently purely because we injected a different collaborator.
        assertEquals("EMAIL receipt for order ORD-2 sent to ben@example.com", result);
    }

    // TEST 3: the tightly-coupled service, for contrast. Notice what we CANNOT do:
    // there is no way to hand it a fake, because it builds its own EmailReceiptSender
    // inside itself. We are forced to run the real thing. The test still passes, but the
    // service is rigid - to test it without email, or to switch to SMS, you must EDIT the
    // class. That rigidity is the cost this whole lesson is about.
    @Test
    void tightlyCoupledService_isStuckWithEmail() {
        // No constructor argument possible - the dependency choice was taken away from us.
        TightlyCoupledOrderService service = new TightlyCoupledOrderService();

        String result = service.placeOrder("ORD-3", "cara@example.com");

        // It can only ever be email, because that choice is welded inside the class.
        assertEquals("EMAIL receipt for order ORD-3 sent to cara@example.com", result);
    }
}
