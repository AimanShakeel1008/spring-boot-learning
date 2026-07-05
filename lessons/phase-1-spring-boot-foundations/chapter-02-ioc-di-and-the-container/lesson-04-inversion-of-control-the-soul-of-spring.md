# Lesson 04 — Inversion of Control, the Soul of Spring

**Phase 1 — Spring Boot Foundations · Chapter 2 — Inversion of Control, Dependency Injection, and the Container · Lesson 4**

**Project files created or modified in this lesson (all clearly-labelled, for-learning-only demos):**
- `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ioc/ReceiptSender.java` (new — the interface: a "what")
- `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ioc/EmailReceiptSender.java` (new — one concrete "how")
- `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ioc/TightlyCoupledOrderService.java` (new — the WRONG way)
- `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ioc/LooselyCoupledOrderService.java` (new — the RIGHT way)
- `phase-1-monolith/ecommerce/src/test/java/com/ecommerce/demos/ioc/InversionOfControlDemoTests.java` (new — proves the difference)

**Prerequisites:** Lesson 01 (framework vs library, "the framework calls your code"), Lesson 03 (the `ApplicationContext`, the one-line meaning of "bean").

---

## SECTION 1 — THE PROBLEM

Picture the E-Commerce system a few weeks from now. You have an `OrderService` that places an order and then sends the customer a receipt by email. You wrote the obvious thing: inside `OrderService`, you did `new EmailReceiptSender()` and called it. It worked. You shipped it. Everyone was happy.

Then reality arrives in three waves. **Wave one:** the business wants SMS receipts for some customers, not email. But `OrderService` is welded to `EmailReceiptSender` — the choice is buried inside it — so you have to crack open `OrderService` and edit it, even though the *ordering* logic hasn't changed at all. **Wave two:** you sit down to write a test for `OrderService.placeOrder(...)` and discover you can't, not without a real email sender firing off real emails every time the test runs, because the service builds its own sender and gives you no way to substitute a harmless fake. **Wave three:** multiply this by every service in the system — payments, notifications, inventory — each one hard-wiring the exact classes it depends on. Change one low-level class and you're editing a dozen high-level ones. The system has quietly fused into a single rigid block where nothing can move without everything moving.

That fusing has a name — **tight coupling** — and it is the disease this entire chapter cures. The cure has a name too — **Inversion of Control** — and it is so central that people call it the soul of Spring. Everything you will write from here on (every `@Service`, every `@RestController`, every repository) works because of the idea in this one lesson. So we slow all the way down and build it from nothing, in plain Java first, with no Spring magic to hide behind — because if you understand *why* this idea exists, every annotation in the rest of the course becomes obvious instead of mysterious.

---

## SECTION 2 — REAL WORLD ANALOGY

Think about hiring for a job versus doing everything yourself.

**The tightly-coupled way is a one-person shop who insists on making their own tools.** Say you run a bakery and you need flour. Instead of *ordering* flour, you decide to grow the wheat, harvest it, and mill it yourself — inside the bakery. Now your bakery is welded to one exact source of flour. Want to try a different flour? You must rebuild your milling operation. Want someone to check your bread recipe without an entire wheat farm running in the background? Impossible — the farm is baked into the bakery. Every ingredient made this way turns your small bakery into a sprawling, immovable factory where changing anything means changing everything.

**The loosely-coupled way is a bakery that simply orders what it needs.** You write on your supply list: "I need *flour* — I don't care which farm, as long as it's flour." A supplier delivers it to your door. Now you can switch farms without touching your ovens. You can hand a tester a bag of *pretend* flour to check your process safely. The bakery states *what* it needs and lets someone else decide *which* one shows up and deliver it. The bakery does far less, yet can do far more.

Notice the direction of control. In the first bakery, the bakery reaches *out* and grabs (constructs) everything itself — control points outward from the bakery. In the second, the bakery just declares its needs and an outside supplier *pushes* the right thing *in* — control has been turned around, or **inverted**. That inversion is the whole lesson. And here's the punchline that names the players: the "supplier who reads your list and delivers the right thing to your door" — in Spring, that supplier is the **container** (the `ApplicationContext` from Lesson 03). The list of what each part needs is your code. Spring reads the lists and does the deliveries.

| In the bakery | In Spring |
|---|---|
| Growing/milling your own flour inside the bakery | `new EmailReceiptSender()` inside a service (tight coupling) |
| A supply list: "I need *flour*, any farm" | depending on an interface (a "what"), not a concrete class |
| The supplier who reads the list and delivers | the container / `ApplicationContext` |
| The delivery arriving at your door | dependency injection (the object handed in) |
| Handing a tester pretend flour | injecting a fake in a test |
| Control turned around: needs declared, goods pushed in | Inversion of Control |

---

## SECTION 3 — THE CONCEPT EXPLAINED

We build the idea in five small steps: **coupling**, then **tight coupling and why it hurts**, then **depending on a "what" instead of a "how"**, then **Inversion of Control and Dependency Injection**, and finally **what a bean really is** and how the container ties it all together.

### 3.1 What "coupling" means

**Coupling** is simply *how much one piece of code depends on the exact details of another piece*. Two classes are **tightly coupled** when one of them knows and relies on the specific, concrete details of the other — so you cannot change or replace one without disturbing the other. They are **loosely coupled** when each depends only on a minimal, stable promise, so either can change freely as long as the promise holds.

A tiny everyday example: a lamp with its power cord *soldered directly into the wall wiring* is tightly coupled to that wall — move house and you're cutting wires. A lamp with a *plug* is loosely coupled — the plug is a stable promise ("two prongs, this voltage"), and any socket that honours it will do. The plug didn't remove the dependency (the lamp still needs power); it made the dependency *swappable*. Loose coupling is never about needing nothing — it's about depending on a stable promise instead of a fixed, specific thing.

### 3.2 Tight coupling, concretely, and the three things it breaks

Here is tight coupling in one line of Java, the line every beginner writes:

```java
public class OrderService {
    private final EmailReceiptSender sender = new EmailReceiptSender();  // <-- welded
}
```

That `new EmailReceiptSender()` looks innocent, but it silently does three harmful things:

1. **It hard-wires one exact class.** `OrderService` now depends on `EmailReceiptSender` *specifically* — not on "something that can send receipts", but on that one class. Swapping to SMS means editing `OrderService`, even though ordering logic is unchanged. A high-level policy class (ordering) is now chained to a low-level detail class (email).
2. **It makes the class untestable in isolation.** To test `placeOrder(...)` you must run a *real* `EmailReceiptSender`. There is no seam to slip a harmless fake into, because the service creates its collaborator itself, privately, out of your reach. Tests become slow, flaky, and dependent on real email working.
3. **It gives the class a second job it shouldn't have.** `OrderService` should place orders. Deciding *which* receipt sender exists and *building* it is a different responsibility entirely. By newing its own dependency, the service has quietly taken on the job of a factory and a wiring diagram, on top of its real job.

The root cause of all three is the same: **the object is constructing its own dependencies.** Every cure below flows from taking that one job away from it.

### 3.3 Depend on a "what", not a "how" (program to an interface)

Step one of the cure: stop depending on the concrete class, and depend on a **promise** instead.

> **New term — interface (as a dependency).** An **interface** in Java is a named list of abilities with no bodies — a pure promise of *what* can be done, saying nothing about *how*. `ReceiptSender` with a single `send(...)` method is an interface: it promises "you can send a receipt" without committing to email, SMS, or anything else. Why does this help? Because if your service depends on the interface `ReceiptSender` rather than the class `EmailReceiptSender`, it depends only on the stable *what*. Any class that keeps the promise — `EmailReceiptSender`, `SmsReceiptSender`, a fake test sender — can stand in, and the service neither knows nor cares which. This is called **programming to an interface**: point your dependencies at the promise (the "what"), not the implementation (the "how").

So we split one class into two ideas:

```
   ReceiptSender  (interface, the "what")   <-- the service depends on THIS
        ▲
        │ implements
   ┌────┴──────────┐
EmailReceiptSender  SmsReceiptSender   (the "how"s, interchangeable)
```

The service will now say "I need a `ReceiptSender`" and be satisfied by *any* of them. But one question remains, and it's the important one: **if the service no longer builds its own sender, who does, and how does the service get one?**

### 3.4 Inversion of Control and Dependency Injection

Step two of the cure answers that question — and this is the heart of the lesson.

Look at where the *control* lives in the tightly-coupled version. `OrderService` decides which sender exists (`new EmailReceiptSender()`), when it's built, and holds it. Control over the dependency points *outward* from the service. Now we turn that around: we make the service *stop deciding*, and instead *receive* its sender from outside, through its constructor:

```java
public class OrderService {
    private final ReceiptSender sender;                 // a "what", not a "how"
    public OrderService(ReceiptSender sender) {         // hand me one; I won't build it
        this.sender = sender;
    }
}
```

Two distinct ideas just happened, and they have two distinct names — people mix them up constantly, so hold them apart:

> **New term — Inversion of Control (IoC).** **Inversion of Control** is the *principle* that a component should not decide and fetch its own collaborators; that decision is taken away from it and handed to something outside. The "control" being inverted is control over *which concrete things this object depends on and when they're created*. In the tight version, the service held that control; now an outside party holds it. Why does this exist? Because when high-level objects stop choosing their own low-level details, you can rearrange, swap, and test the system from the outside without surgery on the inside. It is the same principle you met in Lesson 01 as the difference between a *library* (your code calls it) and a *framework* (it calls your code) — the framework is in control, hence the **Hollywood Principle: "Don't call us, we'll call you."** The object stops calling out for what it needs; the outside world calls *into* it, handing it what it needs.

> **New term — Dependency Injection (DI).** **Dependency Injection** is the concrete *technique* that carries out that principle: the dependencies an object needs are *given to it* ("injected"), typically through its constructor, rather than created inside it. "Dependency" = a thing this object needs to do its job (the sender). "Injection" = handing it in from outside. So IoC is the *idea* ("don't fetch your own collaborators"); DI is the *mechanism* ("they're passed into your constructor"). DI is the most common way to achieve IoC, which is why the two terms travel together.

Crucial point for building real intuition: **none of this needs Spring.** In the demo for this lesson we do the injecting *by hand* — plain Java, we write the `new` in the test and pass it in. That already gives us swap-ability and testability. So what does Spring actually add? Spring is the **tireless automatic supplier**: in a real app you have hundreds of objects, each needing several others, forming a huge web. Wiring that web by hand — newing everything in the right order and passing each into the next — is enormous, error-prone boilerplate. Spring reads which objects need which, builds them all once, and injects each into the others *for* you. **Spring doesn't invent DI; it automates DI at scale.** Understanding that the principle stands on its own, and Spring is "merely" the machine that automates it, is exactly the mental model that stops Spring from feeling like magic.

### 3.5 What a bean really is, and the container that makes them

In Lesson 03 we gave "bean" a one-line meaning so we could keep moving. Now we can give it the full, honest definition, because you finally have the idea it rests on.

> **New term (full version) — bean.** A **bean** is an object whose entire lifecycle — *creating it, giving it the collaborators it depends on, holding onto it, and eventually destroying it* — is managed by Spring instead of by you. You don't write `new` for a bean and you don't hand it its dependencies; you *declare* that it's a bean and *declare* what it needs, and the container does the constructing and the injecting. Why does this exist? Because that is precisely the automation from 3.4: once Spring is in charge of creating your objects, it's also the natural place to inject each object's dependencies. A "bean" is just "an object enrolled in that management." The words `@Component`, `@Service`, `@Repository`, `@Controller` you'll meet next lesson are all just different labels that mean "enroll this class as a bean."

And the thing doing the managing is the **container**, which you already met:

> **The `ApplicationContext` is the container (Lesson 03, revisited in depth).** The container is the "tireless supplier" from the analogy made real. At startup it (1) finds every class you declared as a bean, (2) works out which beans each one needs, (3) creates them all in an order where every dependency exists before whoever needs it, and (4) injects each bean's dependencies into it. The finished result — every bean built and wired together — is the `ApplicationContext`. So Lesson 03's "container full of beans" and this lesson's "automatic dependency injection" are two views of the *same* object: the context is *where* IoC happens. When you heard "Spring wires your beans," this is the wiring: the container performing dependency injection across the whole graph of objects at startup.

Putting the whole chapter's spine in one breath: **you declare beans and what they need → the container creates them and injects their dependencies (that's DI) → so no object fetches its own collaborators (that's IoC) → so nothing is tightly coupled → so the system stays swappable and testable.** Every concept after this is a detail hanging off that spine.

```
   You write:                    The container does at startup:
   ┌──────────────────┐          ┌───────────────────────────────────────┐
   │ "OrderService is  │          │ 1. see OrderService needs a ReceiptSender│
   │  a bean, it needs │  ─────►  │ 2. build the ReceiptSender bean first   │
   │  a ReceiptSender" │          │ 3. build OrderService, INJECT the sender │
   └──────────────────┘          │ 4. keep both in the ApplicationContext  │
                                  └───────────────────────────────────────┘
```

---

## SECTION 4 — THE WRONG WAY

**WRONG APPROACH — a service that builds its own dependency:**

```java
public class TightlyCoupledOrderService {

    // The service reaches out and constructs its own collaborator, welding itself
    // to one exact class and one exact behaviour (email).
    private final EmailReceiptSender receiptSender = new EmailReceiptSender();

    public String placeOrder(String orderId, String customerEmail) {
        // ...pretend we validated and saved the order...
        return receiptSender.send(orderId, customerEmail);   // can ONLY ever be email
    }
}
```

**What goes wrong.** The code compiles and even runs correctly *today* — that's what makes the trap so easy to fall into. The damage shows up the moment anything needs to change or be tested. Suppose you now want to unit-test `placeOrder(...)` without sending real email. You cannot: there is no way in, because the sender is created privately inside the service. Suppose you want SMS for some customers. You cannot, without editing this file and probably adding an `if`. The service has fused ordering logic to email delivery, and the two can never be separated again without surgery.

**If you tried to test it in isolation, the experience would be** (a prediction):

```
// You want to write:  "when I place an order, verify the RIGHT receipt was sent" -
// but you have no way to pass in a fake sender to observe. Your only option is:

TightlyCoupledOrderService service = new TightlyCoupledOrderService();
service.placeOrder("ORD-1", "amy@example.com");
// -> a REAL EmailReceiptSender runs. In a real app this hits a real email provider.
//    Console shows:
[EmailReceiptSender] EMAIL receipt for order ORD-1 sent to amy@example.com
// The test is now slow, online, and you STILL can't cleanly assert which sender ran,
// because the choice was hidden inside the class. You are stuck with email, forever,
// unless you EDIT the service itself.
```

The service *works*, but it is rigid and hard to test — and rigidity that only bites you later is the most expensive kind, because by then a dozen classes are built the same way.

---

## SECTION 5 — THE RIGHT WAY

**CORRECT APPROACH — the same service, receiving its dependency instead of building it:**

```java
// Depend on the interface (a "what"), not a concrete class (a "how").
public class LooselyCoupledOrderService {

    private final ReceiptSender receiptSender;                 // set once, never rebuilt here

    // The dependency is HANDED IN. Control over "which sender" now lives with the
    // caller (Inversion of Control); passing it through the constructor is the
    // technique (Dependency Injection).
    public LooselyCoupledOrderService(ReceiptSender receiptSender) {
        this.receiptSender = receiptSender;
    }

    public String placeOrder(String orderId, String customerEmail) {
        // ...pretend we validated and saved the order...
        return receiptSender.send(orderId, customerEmail);     // whichever sender you injected
    }
}
```

**Step-by-step walkthrough of what happens when the test runs it:**

1. The test creates a sender — either a harmless fake (`RecordingReceiptSender`) or the real `EmailReceiptSender`. *The test* is in control of that choice now, not the service.
2. The test passes that sender into `new LooselyCoupledOrderService(sender)`. The dependency is injected through the constructor. The service stores it, without knowing or caring which concrete class it is.
3. The test calls `service.placeOrder("ORD-1", "amy@example.com")`.
4. Inside, `placeOrder` calls `receiptSender.send(...)` — on whatever sender was injected. With the fake, nothing real happens; the fake just records the call and returns a canned line. With the real email sender, an email line is produced.
5. The test inspects the fake (was it called? with what?) and asserts. Because a seam existed to inject the fake, the service is testable in complete isolation — fast, offline, deterministic.

**Expected output** — running the demo's tests (a prediction):

```
[EmailReceiptSender] EMAIL receipt for order ORD-2 sent to ben@example.com
[EmailReceiptSender] EMAIL receipt for order ORD-3 sent to cara@example.com

[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

(The two `EMAIL...` lines come from Test 2 and Test 3, which use the real email sender. Test 1 uses the fake, so it prints nothing — proving it ran fully offline.)

**How to run it:**

```bash
cd phase-1-monolith/ecommerce
mvn test
```

**Check it matches.** You should see `Tests run: 7` and `BUILD SUCCESS` (the 4 tests from Chapter 1 plus the 3 new ones here). If the count or result differs, the machine is right — send me what you saw and we'll trace it.

Notice what we did *not* need for any of this: Spring. The injection was done by hand in the test. That's deliberate — it proves the principle stands on its own. Starting next lesson, Spring takes over the hand-wiring and does it automatically for the whole app.

---

## SECTION 6 — APPLIED TO OUR PROJECT (A LABELLED DEMO)

This lesson teaches a *principle* (IoC/DI), not yet a Spring feature with an annotation — the annotations that let Spring perform the injection arrive in Lesson 05, in dependency order. But the principle can absolutely be shown as runnable code, so per our rules it gets a clearly-labelled, for-learning-only demo rather than being left as talk. The E-Commerce product does not use these classes yet; they exist purely to let you see tight coupling and its cure with your own hands.

> **Disclaimer (also at the top of every demo file):** *This `demos/ioc` package is included purely to demonstrate Inversion of Control and Dependency Injection as plain-Java ideas. The E-Commerce product does not need it here.* None of these classes carry a bean annotation, so component scanning ignores them and the running application is completely unchanged.

**Saved:** `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ioc/ReceiptSender.java`
**Saved:** `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ioc/EmailReceiptSender.java`
**Saved:** `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ioc/TightlyCoupledOrderService.java`
**Saved:** `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ioc/LooselyCoupledOrderService.java`
**Saved:** `phase-1-monolith/ecommerce/src/test/java/com/ecommerce/demos/ioc/InversionOfControlDemoTests.java`

**What this adds to our system.** No behavior change to the app at all — it still boots the same and serves the same 404 handshake from Lesson 02. What it adds is the chapter's foundational idea made executable: a side-by-side of the rigid way and the flexible way, plus three tests that lock in *why* the flexible way wins (isolation, swap-ability, and the rigidity of the alternative). When Lesson 05 introduces `@Service` and `@Autowired`, you'll recognise them instantly as "Spring doing, automatically, the exact hand-injection we just did here."

**If a user interacted with this right now:** they couldn't — there's no endpoint. These are internal demo classes exercised only by the test. That's expected for a principle-first lesson; the wiring becomes user-visible once controllers arrive in Chapter 3.

**Does the project still build, run, and pass tests?** Tracing by hand: the four demo classes carry no Spring annotations, so component scanning skips them — the `ApplicationContext` and its bean count are unchanged, and startup is unaffected. The new test uses only plain `new`, starts no Spring context, and its assertions all hold against the code as written. The 4 Chapter-1 tests are untouched. So `mvn test` should now run **7 tests, all green** (4 old + 3 new). No new endpoint (`api.http` unchanged), no new dependency (`pom.xml` unchanged), no new secret (`.env.example` unchanged).

---

## SECTION 7 — GOTCHAS AND COMMON MISTAKES

- **Confusing IoC with DI.** People use them interchangeably; they're not the same. IoC is the *principle* (an object doesn't fetch its own collaborators); DI is the *technique* that delivers it (dependencies passed in). *Avoid the muddle:* say "IoC is the why, DI is the how." DI is one way — the common way — to achieve IoC.
- **Thinking DI requires Spring.** DI is plain Java: pass dependencies into constructors. Spring only *automates* it. *Avoid it:* remember this lesson's demo injects by hand with zero Spring — the payoff (testability, swap-ability) is already there before any framework.
- **Depending on the concrete class "just this once."** Typing `new EmailReceiptSender()` inside a service feels harmless and saves a keystroke today. It is exactly the weld that costs you later. *Avoid it:* depend on the interface and let it be injected, from the start.
- **Injecting a concrete type instead of an interface.** Even with constructor injection, if you inject `EmailReceiptSender` instead of `ReceiptSender`, you've kept half the coupling — you still can't swap the implementation. *Avoid it:* the injected type should be the promise (interface), not the implementation, whenever more than one implementation is plausible.
- **Assuming loose coupling means "no dependencies."** It doesn't — the service still needs a sender. Loose coupling means depending on a *stable promise* you can satisfy many ways, not depending on *nothing*. *Avoid it:* aim to make dependencies swappable, not to eliminate them.

---

## SECTION 8 — TRADEOFFS AND WHEN NOT TO USE THIS

- **Indirection has a cost.** Every interface-plus-injection adds a layer: to see what actually runs, you follow the wiring rather than reading one class top to bottom. For a huge system that price buys flexibility and testability many times over; for a five-line throwaway script it's pure ceremony. Don't wrap a trivial helper in an interface it will never have a second implementation of.
- **Not every object should be a bean.** Simple value objects (an `Order`, a `Money` amount, a DTO) are *data*, created with `new` all day long — they have no collaborators to inject and no lifecycle to manage. IoC/DI is for *collaborators* (services, repositories, senders), not for plain data. Forcing everything into the container is a classic over-correction.
- **One implementation, forever?** If a dependency genuinely has exactly one implementation and always will, the interface earns less. Many teams still inject it (for testability alone); others inject the concrete class directly. It's a judgement call — the testability seam is usually worth the interface even then.
- **The failure mode of over-abstraction.** It's possible to bury simple logic under so many interfaces and injected layers that the code becomes hard to follow — "where does anything actually *happen*?" Use IoC where things genuinely vary or need isolating in tests; don't cargo-cult an interface onto every last class.

---

## SECTION 9 — KEY TAKEAWAYS

- Tight coupling — an object building its own dependencies with `new` — welds high-level logic to low-level details, making the system rigid to change and impossible to test in isolation.
- The cure is to depend on a stable *promise* (an interface, the "what") instead of a concrete class (the "how"), so any implementation that keeps the promise can be substituted freely.
- Inversion of Control is the principle that an object should not choose and fetch its own collaborators; that control is handed to something outside — the same "don't call us, we'll call you" idea as a framework calling your code.
- Dependency Injection is the technique that carries out IoC by passing an object's dependencies into it (usually through its constructor), and it is plain Java — Spring's job is to *automate* it across a whole application, not to invent it.
- A bean is an object whose creation, dependency-wiring, and lifecycle Spring manages inside the `ApplicationContext`; that container is exactly *where* dependency injection happens at startup, tying Lesson 03's "container of beans" to this lesson's "automatic injection."

---

## SECTION 10 — CODING CHALLENGE WITH HIDDEN ANSWER

**Challenge.** The business now wants an `SmsReceiptSender` — receipts by text message. Using *only* the plain-Java demo classes from this lesson (still no Spring):

**(a)** What is the complete list of files you must create or edit to make `LooselyCoupledOrderService` send SMS instead of email for a given call? **(b)** What would that same change require if you were forced to work with `TightlyCoupledOrderService` instead? **(c)** Write the `SmsReceiptSender` class and a short snippet that makes `LooselyCoupledOrderService` use it, and predict the returned line.

<details>
  <summary>Click to reveal the answer</summary>

**(a) For the loosely-coupled service: add one new file, edit the service ZERO times.**
You create `SmsReceiptSender implements ReceiptSender`. That's it. `LooselyCoupledOrderService` already depends on the `ReceiptSender` interface and receives its sender from outside, so it needs *no* change — you just inject the new sender at the call site. This is the payoff: new behaviour, and the high-level service is untouched.

**(b) For the tightly-coupled service: you must EDIT the service itself.**
`TightlyCoupledOrderService` hard-codes `new EmailReceiptSender()` inside it. To send SMS you'd have to open that file and change the field (or add branching logic), risking the ordering code every time the delivery method changes — the exact rigidity the lesson warns about.

**(c) The new class and the wiring:**

```java
// New file: SmsReceiptSender.java - keeps the same promise a different way.
package com.ecommerce.demos.ioc;

public class SmsReceiptSender implements ReceiptSender {
    @Override
    public String send(String orderId, String customerEmail) {
        // (In a real app this would go to a phone number; we reuse the parameter
        //  for simplicity in the demo.)
        String receipt = "SMS receipt for order " + orderId;
        System.out.println("[SmsReceiptSender] " + receipt);
        return receipt;
    }
}
```

```java
// Wiring - note LooselyCoupledOrderService is NOT edited, only what we inject changes:
LooselyCoupledOrderService service =
        new LooselyCoupledOrderService(new SmsReceiptSender());   // inject SMS instead
String result = service.placeOrder("ORD-9", "dan@example.com");
```

**Predicted output** (a prediction):

```
[SmsReceiptSender] SMS receipt for order ORD-9
// result == "SMS receipt for order ORD-9"
```

To check it yourself: add `SmsReceiptSender.java` to the `demos/ioc` package, add a quick test that injects it and asserts the returned line starts with `"SMS receipt"`, and run `mvn test`. Same service class, new behaviour, no surgery — that's Inversion of Control earning its keep.

</details>

---

## SECTION 11 — WHAT IS NEXT

You now have the principle by hand: depend on promises, let dependencies be injected, keep control outside the object. Next, **Lesson 05 — Stereotype annotations and component scanning** hands the hand-wiring to Spring: you'll mark classes with `@Component`, `@Service`, `@Repository`, and `@Controller` to enrol them as beans, and use `@Autowired` so the container performs exactly the injection we just did ourselves — automatically, across the whole application.

---

*Lesson 04 of the curriculum — Phase 1, Chapter 2, Lesson 1 of 7 in the chapter.*
