# Lesson 03 — The Main Application Class

**Phase 1 — Spring Boot Foundations · Chapter 1 — Getting Started · Lesson 3 of 3**

**Project files created or modified in this lesson:**
- `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ContextInspectorDemo.java` (new — a labelled for-learning-only demo)
- `phase-1-monolith/ecommerce/src/test/java/com/ecommerce/ApplicationContextTests.java` (new — a test that proves the `ApplicationContext` is real)

**Prerequisites:** Lesson 01 (what Spring Boot is, auto-configuration, embedded server), Lesson 02 (the project, Maven, `SpringApplication.run`).

---

## SECTION 1 — THE PROBLEM

In Lesson 02 you met one tiny class, `EcommerceApplication`, with one strange label on it — `@SpringBootApplication` — and one line inside it, `SpringApplication.run(...)`. You started the app (or are about to), a wall of log lines scrolled past, and after a couple of seconds it said something like *"Started EcommerceApplication in 1.8 seconds"* and just sat there. Something is clearly alive and waiting. But **what**, exactly, came to life? And **who** built it?

Here is the problem this lesson solves. Right now that class is a magic box. You typed `@SpringBootApplication` because Spring Initializr put it there, and it "just works" — which is exactly the phrase to treat as a red flag and rip open. If you don't know what that one annotation actually does, then the moment something goes wrong — a class you wrote isn't being found, a setting isn't being read, the app starts but your code never runs — you will have no mental model to debug with. You'll be poking a black box in the dark.

The failure scenario is concrete and extremely common for beginners. Imagine six lessons from now you create a class in a package **next to** `com.ecommerce` — say `com.myshop.PaymentService` — and you cannot understand why Spring completely ignores it: your code compiles, but at runtime Spring acts as if the class doesn't exist. Or you put `@SpringBootApplication` on two classes and startup explodes. Or you wonder why there's no Tomcat installed anywhere on your machine yet a web server is clearly answering on port 8080. Every one of those mysteries dissolves the instant you understand the three jobs `@SpringBootApplication` does and the one object it produces. That object — the `ApplicationContext` — is the beating heart of every Spring application, and by the end of this lesson you'll have written code that reaches inside it and reads its contents with your own hands.

---

## SECTION 2 — REAL WORLD ANALOGY

Think of opening a large restaurant for the evening service.

Before a single customer arrives, one person — the **general manager** — walks in and runs the whole opening routine. They do three separate jobs, in order:

1. **They read the house rulebook.** "We serve dinner, not breakfast. Tables are laid this way. The specials are X and Y." These are the standing decisions about how *this* restaurant runs. (This is `@Configuration` — the class itself is allowed to hold setup decisions.)
2. **They walk every room and gather the staff.** They go through the kitchen, the bar, the floor — every room *inside the building* — and say "you're on tonight, you're on tonight." They do **not** wander next door into other buildings; only the rooms of this restaurant, from the front door inward. (This is `@ComponentScan` — find all of *our* worker classes, but only from our package downward.)
3. **They switch on all the standard equipment automatically.** The moment they see the coffee machine is plugged in, they turn it on; seeing wine in the cellar, they open the bar. They don't hand-configure each appliance — if it's present, the standard sensible setup kicks in, and they only fiddle where the owner left a specific note overriding the default. (This is `@EnableAutoConfiguration` — Lesson 01's rule engine: for every capability whose library is present, apply the standard configuration unless you said otherwise.)

When those three jobs are done, the result is a **fully staffed, fully equipped, running restaurant** — a live thing with every station manned and every machine on, waiting for orders. That live, assembled restaurant is the **`ApplicationContext`**: not the rulebook, not the manager, but the running organization the manager produced.

And the **building itself with its front door open to the street** — the part that lets customers physically walk in and be served — that's the **embedded Tomcat web server**. In the old days (Lesson 01) you rented a separate building (an external server) and moved your staff into it. Spring Boot instead ships the building *inside the box*: unpack the app and the doors are already there.

One label, `@SpringBootApplication`, hires that general manager and tells them to do all three jobs. `SpringApplication.run(...)` is you unlocking the front door and shouting "open up — go!"

| In the restaurant | In Spring Boot |
|---|---|
| The general manager who runs opening | `@SpringBootApplication` (the combined role) |
| Reading the house rulebook | `@Configuration` |
| Walking the rooms, gathering staff | `@ComponentScan` |
| Auto-switching-on standard equipment | `@EnableAutoConfiguration` |
| The fully staffed, running restaurant | the `ApplicationContext` |
| The building with its doors open to the street | embedded Tomcat |
| "Open up — go!" | `SpringApplication.run(...)` |

---

## SECTION 3 — THE CONCEPT EXPLAINED

Four things to understand: **what an annotation actually does**, **the three annotations hidden inside `@SpringBootApplication`**, **the embedded server**, and **the `ApplicationContext`**.

### 3.1 A reminder: what an annotation is (and is not)

An **annotation** is a label starting with `@` that you attach to a piece of code — a class, a method, a field. **It does nothing by itself.** It is not a command that runs. It is a *sticky note* that some other program reads later and reacts to.

Tiny concrete example: if you put a yellow sticky note reading "FRAGILE" on a box, the note doesn't protect the box. It only matters because a *human mover reads it* and decides to carry it carefully. Remove the mover, and the note is just paper. Annotations are identical: `@SpringBootApplication` is inert text until Spring Boot's startup code reads it and changes its behavior in response.

So when we say "`@SpringBootApplication` does three jobs," the honest phrasing is: *"Spring Boot's startup machinery, when it reads the `@SpringBootApplication` label, performs three jobs."* The label is the trigger; the machinery does the work. Keep that distinction — it's why beginners get confused when an annotation "isn't doing anything": usually nothing is *reading* it.

### 3.2 `@SpringBootApplication` is three annotations in a trench coat

The single most important fact of this lesson: `@SpringBootApplication` is not one special thing. It is a convenience label that **bundles three other annotations** into one, because virtually every Spring Boot app wants all three and typing three lines every time is boilerplate (Lesson 01's word — repetitive ceremony). The three are:

1. `@Configuration`
2. `@ComponentScan`
3. `@EnableAutoConfiguration`

To explain them, one prerequisite term that Chapter 2 covers in full but that we must define now, because all three annotations revolve around it: the **bean**.

> **New term — bean.** A **bean** is simply *an object that Spring creates and manages for you*, instead of you creating it yourself with `new`. In plain Java you write `PaymentService p = new PaymentService();` — *you* build the object, *you* keep track of it. In Spring, you instead let the framework build `PaymentService` once, hold onto it, and hand it to whoever needs it. That framework-managed object is called a bean. Why does this exist? Because in a big system, hundreds of objects need each other, and having every object build its own dependencies by hand becomes an unmaintainable tangle — Spring building and connecting them centrally is the cure (that whole story, "Inversion of Control," is Chapter 2). For today the one-line version is enough: **a bean is an object Spring makes and looks after.** The words "component," "service," "repository," and "controller" you'll meet later are all just *kinds of beans*.

Now the three annotations make sense:

**(1) `@Configuration`** — marks the class as *a source of setup decisions*: a place that is allowed to define beans and hold configuration. Marking `EcommerceApplication` with it (via `@SpringBootApplication`) means our front-door class itself is a legitimate place to declare beans if we ever want to. We'll write actual `@Bean` methods inside a `@Configuration` class in Lesson 07; for now, just know the marker is present.

**(2) `@ComponentScan`** — the "walk the rooms and gather the staff" job. When Spring reads this label, it goes hunting through your code for classes you've marked as beans (with labels like `@Component`, `@Service`, `@Repository`, `@Controller` — all coming in Chapter 2) and registers every one it finds. **The critical detail — and the source of that "Spring ignores my class" bug — is *where* it hunts.** By default, `@ComponentScan` searches the package of the class it sits on, **and every sub-package beneath it**, and *nothing outside*.

Our class `EcommerceApplication` lives in package `com.ecommerce`. Therefore component scanning covers `com.ecommerce` and everything under it: `com.ecommerce.controller`, `com.ecommerce.service`, `com.ecommerce.repository`, `com.ecommerce.demos`, and so on. That is **exactly why the project's folder layout puts the main application class at the top-level package**, with all other packages as children. It's not a style choice — it's what makes automatic discovery reach every class you write. A class placed in `com.myshop` (a *sibling*, not a child, of `com.ecommerce`) would sit outside the scan and be invisible to Spring, no matter how many bean labels you slap on it.

```
                com.ecommerce          <-- @SpringBootApplication lives HERE
                (scan STARTS here)
                /      |       \
   com.ecommerce.  com.ecommerce.  com.ecommerce.
     controller       service         demos          <-- all SCANNED (children)

   com.myshop.anything                                <-- NOT scanned (sibling, outside)
```

**(3) `@EnableAutoConfiguration`** — Lesson 01's auto-configuration rule engine, switched on. When Spring reads this label, it pulls in roughly 150 candidate configuration "recipes" that ship inside Spring Boot, and for each one checks its conditions — mostly *"is the relevant library on the classpath?"* and *"did the developer already configure this themselves?"* Every recipe whose conditions pass gets applied. Because our `pom.xml` put `spring-boot-starter-webmvc` on the classpath, the web recipes pass their conditions, and *that* is how an embedded Tomcat and the whole web layer get configured **without you writing a line of setup**. Remove that starter and those recipes would silently switch off — nothing to configure a web server *for*.

In one breath: **`@SpringBootApplication` = "this class holds config (`@Configuration`), find all my beans from here down (`@ComponentScan`), and auto-configure everything my libraries imply (`@EnableAutoConfiguration`)."** Three jobs, one label.

> **Why bundle them?** Because 99% of Spring Boot apps want precisely this trio, and forcing every developer to write three annotations on every main class would be pure ceremony. The bundle is convenience only — you could delete `@SpringBootApplication` and write the three separately and it would behave identically. (There's a name for an annotation built out of other annotations: a **meta-annotation** / composed annotation. `@SpringBootApplication` is annotated *with* those three, so wearing it is the same as wearing all three.)

### 3.3 The embedded server: why there's no Tomcat to install

In Lesson 01 you learned the old world: you installed a **servlet container** (a server program that hosts your web code — Tomcat being the famous one) as a separate piece of software, then deployed your application *into* it as a WAR file. Two things to install, two things to manage, and your app couldn't run on its own.

Spring Boot flips this. Tomcat is not installed on your machine as a separate program. It arrived as an **ordinary dependency** — a JAR pulled in transitively by `spring-boot-starter-webmvc` (recall Lesson 02: one starter line becomes ~40 JARs; Tomcat's JARs are among them). It sits in your `~/.m2` cache like any other library. Then, at startup, the auto-configuration recipe for web applications sees those Tomcat classes on the classpath, and its condition passes, so it **creates a Tomcat instance in memory, inside your running process, and tells it to start listening on port 8080.** The server is now part of *your* program rather than a house your program was moved into. This is the **embedded server** (Lesson 01), and it's the thing that makes `java -jar app.jar` able to serve real web traffic with nothing else installed.

Concretely, when you run the app and see a log line like `Tomcat started on port 8080 (http)`, that is this exact mechanism firing: a recipe, a passed condition, an in-process server booted for you. No `install Tomcat` step ever happened because none is needed.

### 3.4 The `ApplicationContext`: the brain of the whole application

`SpringApplication.run(...)` does an enormous amount of work at startup — reads your annotations, scans for beans, runs auto-configuration, creates every bean, connects them to each other, starts Tomcat. **The single object that holds the result of all that work is the `ApplicationContext`.**

> **New term — `ApplicationContext`.** The **`ApplicationContext`** is the *container* that holds every bean Spring created and manages the connections between them. "Container" here means exactly what it sounds like: a big managed box of ready-to-use objects. It is the assembled, running organization — the fully-staffed restaurant from our analogy. When any part of your app needs another part (a controller needs a service, a service needs a repository), it doesn't build it — it asks the `ApplicationContext`, which hands over the already-built, already-connected bean. Why does it exist? Because *something* has to be the central registry that knows about every managed object and how they fit together; without it, "let the framework build and connect your objects" would have nowhere to keep them. This is why it's called the brain, or the heart, of a Spring application: it *is* the running application's assembled state.

Two facts make it concrete:

- **`SpringApplication.run(...)` returns the `ApplicationContext`.** That single line you wrote in Lesson 02 doesn't just have a side-effect of starting things — it hands back the finished container as its return value. We've been ignoring that return value. Today's demo catches it and looks inside.
- **Everything you'll build lives inside it.** Every controller, service, and repository you write in the coming chapters becomes a bean *in this context*. When you hear "Spring wires your beans together," the wiring happens here, in this object, at startup.

```
   your JVM process
   ┌───────────────────────────────────────────────┐
   │  ApplicationContext  (the container / "brain")  │
   │   ┌─────────┐  ┌─────────┐  ┌───────────────┐   │
   │   │ your    │  │ your    │  │ ~200 framework│   │
   │   │ beans   │  │ config  │  │ beans Spring  │   │
   │   │ (soon)  │  │ beans   │  │ made for you  │   │
   │   └─────────┘  └─────────┘  └───────────────┘   │
   │                                                 │
   │   embedded Tomcat  ── listening on :8080 ──────►│  (to the outside world)
   └───────────────────────────────────────────────┘
```

Right now, in *our* app, the "your beans" box is nearly empty (we haven't written any yet), but the container is already holding around two hundred beans that Spring and its auto-configuration created for the web layer. In the demo below, you'll print that exact number.

---

## SECTION 4 — THE WRONG WAY

The mistake: putting your main application class in a package that does **not** sit above your other code.

**WRONG APPROACH:**

```java
// File: src/main/java/com/ecommerce/app/EcommerceApplication.java
package com.ecommerce.app;                 // <-- main class buried in a SUB-package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication                     // scan STARTS at com.ecommerce.app
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
```

```java
// File: src/main/java/com/ecommerce/controller/ProductController.java
package com.ecommerce.controller;          // this is a SIBLING of ...app, not a child

@RestController                            // a bean marker (Chapter 3)
public class ProductController { /* ... */ }
```

**What goes wrong.** Because `@SpringBootApplication` sits in `com.ecommerce.app`, component scanning begins at `com.ecommerce.app` and only searches *downward*. But `ProductController` lives in `com.ecommerce.controller`, which is **not** underneath `com.ecommerce.app` — it's a sibling one level up and over. So the scan never visits it. The class compiles fine, the app starts fine, and Tomcat serves fine — but your controller was never registered as a bean, so its web address simply doesn't exist. You hit the URL and get a 404, and nothing in the logs screams why, because from Spring's point of view you never asked it to manage that class.

**If this ran, the output would be** (a prediction):

```
Started EcommerceApplication in 1.7 seconds     <-- looks totally healthy!

# ... then you send a request to your controller's URL and get:

HTTP/1.1 404 Not Found
{ "status": 404, "error": "Not Found", "path": "/products" }
```

The app *lies about being healthy* — it started perfectly. The bug is invisible until you hit the missing endpoint. This exact "my controller returns 404 and I don't know why" confusion fills beginner forums, and most of the time the root cause is a main class that isn't sitting above the code it's supposed to scan. The cure is trivial: keep `EcommerceApplication` in the **top-level** package (`com.ecommerce`), with everything else as a child — which is precisely how our project is already laid out.

---

## SECTION 5 — THE RIGHT WAY

The "right way" for the main class is exactly what Lesson 02 already generated and what sits in our project today — `EcommerceApplication` in the top-level `com.ecommerce` package. We won't rewrite it; instead, the *right way to understand it* is to prove, with running code, the two claims this lesson makes: **(a)** `SpringApplication.run(...)` returns a real `ApplicationContext`, and **(b)** that context is already full of beans. We'll do that with a small, clearly-labelled demo.

To peek inside the context at startup, we use a **`CommandLineRunner`**:

> **New term — `CommandLineRunner`.** A `CommandLineRunner` is a tiny hook that lets you run **your own code once, automatically, right after the application has finished starting up**. You make a bean that implements the `CommandLineRunner` interface (an interface with one method, `run`), and Spring promises to call that `run` method a single time at the very end of startup, after every bean exists and Tomcat is listening. Why does it exist? Because you often want a "do this once on boot" action — print a banner, load some seed data, or, as here, inspect the freshly-built context — and this is the framework's clean, official place to put it.

```java
// ContextInspectorDemo.java
// FOR LEARNING ONLY: this class exists purely to demonstrate that the
// ApplicationContext from Lesson 03 is a real, inspectable object full of beans.
// The E-Commerce product does not need it; it just prints facts at startup so you
// can SEE the container. Delete it any time and the app behaves the same.

package com.ecommerce.demos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ContextInspectorDemo implements CommandLineRunner {

    private final ApplicationContext context;

    public ContextInspectorDemo(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) {
        int beanCount = context.getBeanDefinitionCount();
        System.out.println("==================================================");
        System.out.println("  [Lesson 03 demo] ApplicationContext is alive.");
        System.out.println("  Beans currently managed by the context: " + beanCount);
        System.out.println("  Is 'ecommerceApplication' a bean? "
                + context.containsBean("ecommerceApplication"));
        System.out.println("  Is the web 'dispatcherServlet' bean present? "
                + context.containsBean("dispatcherServlet"));
        System.out.println("==================================================");
    }
}
```

*(The saved project file carries a full why-comment on every single line; the version above is trimmed for the lesson's readability.)*

**Step-by-step walkthrough of what happens when you run the app:**

1. You run `mvn spring-boot:run`. The JVM starts and calls `main`, which calls `SpringApplication.run(EcommerceApplication.class, args)`.
2. Spring reads `@SpringBootApplication` on `EcommerceApplication` and does its three jobs: treats the class as config, starts component scanning at `com.ecommerce`, and runs auto-configuration.
3. Component scanning walks `com.ecommerce` downward. It reaches `com.ecommerce.demos` and finds our `ContextInspectorDemo` carrying `@Component` — so it registers it as a bean.
4. Auto-configuration sees `spring-boot-starter-webmvc` on the classpath, so its web recipes pass their conditions: it creates the `dispatcherServlet` bean and an embedded Tomcat, and starts Tomcat listening on port 8080.
5. To *build* our `ContextInspectorDemo` bean, Spring calls its constructor, which asks for an `ApplicationContext`. Spring passes in the real, live context (the container can hand you a reference to itself). The bean now holds that handle.
6. Startup finishes. Because our bean is a `CommandLineRunner`, Spring now calls its `run(...)` method exactly once.
7. `run(...)` asks the context for its bean count and two membership checks, and prints the banner. Then the app carries on running normally, Tomcat still listening.

**Expected output** (a prediction — the exact bean count varies by Spring Boot version, so treat the number as approximate):

```
Tomcat started on port 8080 (http) with context path '/'
Started EcommerceApplication in 1.8 seconds (process running for 2.1)
==================================================
  [Lesson 03 demo] ApplicationContext is alive.
  Beans currently managed by the context: 187
  Is 'ecommerceApplication' a bean? true
  Is the web 'dispatcherServlet' bean present? true
==================================================
```

**How to run it:**

```bash
cd phase-1-monolith/ecommerce
mvn spring-boot:run
```

**Check it matches.** Watch the startup logs for the banner. You should see a bean count somewhere in the low hundreds (the exact number depends on your Spring Boot 4.1.0 build — don't worry about matching `187` precisely; what matters is that it's *many*, proving the container is full even though you've written zero beans), and both membership checks printing `true`. If the banner never appears, the machine is right and something's off — tell me what you saw and we'll work out why. To stop the app, press `Ctrl+C`.

**The condition evaluation report** (promised in Lessons 01/02). Run:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--debug
```

The `--debug` argument tells Spring Boot to print a long report of **every auto-configuration recipe, and for each one whether its conditions matched or not**. You'll see entries under "Positive matches" (recipes that fired — e.g. the web/Tomcat ones, *because* `spring-boot-starter-webmvc` is on the classpath) and "Negative matches" (recipes that were skipped — e.g. anything about databases, *because* we have no database library yet). That report **is** `@EnableAutoConfiguration` showing you its work: the literal list of conditions being checked from Section 3.2.

---

## SECTION 6 — APPLIED TO OUR PROJECT

This lesson teaches concepts that are *already* implemented in the project (the main class and its annotation were generated in Lesson 02), plus the `ApplicationContext`, which isn't a thing you "add" — it's produced by the `run(...)` call that already exists. So the concrete, runnable homes for today's material are: **(1)** the labelled demo above, which makes the container visible, and **(2)** a JUnit test that asserts the context is real and populated.

**Saved:** `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ContextInspectorDemo.java`
**Saved:** `phase-1-monolith/ecommerce/src/test/java/com/ecommerce/ApplicationContextTests.java`

The test asserts three claims: the context is not null, our `ecommerceApplication` main class is a managed bean, and the context holds more than 50 beans (a generous lower bound, since the exact count shifts between Spring Boot versions — pinning an exact number would make the test brittle for no benefit).

**What this adds to our system.** Nothing about *behavior* — the app serves exactly what it did before (still just the 404 handshake from Lesson 02). What it adds is **understanding made executable**: a demo you can watch print the container's contents at boot, and three tests that guard, forever, the claim that the `ApplicationContext` gets built and populated. If some future refactor moves the main class out of the top-level package or breaks the scan, `ourMainClassIsManagedAsABean` turns red and points straight at it.

**Does the project still build, run, and pass tests?** Tracing by hand: `ContextInspectorDemo` is a valid `@Component` in a scanned child package; it only reads the context and prints, so it can't fail startup. The three new tests hold for our current project. The existing `contextLoads()` test still passes; the demo's `run(...)` also fires during that test's startup and prints the banner into the test logs — harmless.

Predicted result (a prediction): `mvn test` runs **4 tests, all green**.

```bash
cd phase-1-monolith/ecommerce
mvn test
```

```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Check it matches.** Run the command and confirm 4 tests run and `BUILD SUCCESS`. If any test fails, the machine wins — send me the red output and we'll trace it. No new endpoint (so `api.http` unchanged), no new dependency (so `pom.xml` unchanged), no new secret (so `.env.example` unchanged).

---

## SECTION 7 — GOTCHAS AND COMMON MISTAKES

- **Main class in the wrong package (the big one).** Putting `@SpringBootApplication` in a sub-package or a sibling of your code means component scanning silently misses classes → inexplicable 404s or "no such bean" errors. *Avoid it:* always keep the main class in the **top-level** package, with every other package as a child.
- **Thinking `@SpringBootApplication` is one indivisible magic thing.** It's just three annotations bundled. *Avoid it:* remember the trio — `@Configuration`, `@ComponentScan`, `@EnableAutoConfiguration` — so you can ask "is it config, scanning, or auto-config?"
- **Two `@SpringBootApplication` classes in one app.** Copy-pasting a second main class gives Spring two front doors; startup can fail or behave unpredictably. *Avoid it:* exactly one per application.
- **Expecting an exact bean count.** The count shifts between versions and as you add starters. *Avoid it:* assert "many" (a lower bound), never an exact figure.
- **Assuming an annotation "does something" on its own.** If nothing *reads* it (e.g. it's outside the scan), nothing happens. *Avoid it:* always ask "what code reads this annotation, and does it reach this class?"

---

## SECTION 8 — TRADEOFFS AND WHEN NOT TO USE THIS

- **When you want finer control, split it back into three.** Occasionally you need to customize one job — tell scanning to *also* search a package outside your main class's tree (`scanBasePackages`), or exclude an auto-configuration recipe (`exclude`). You can do that with attributes on `@SpringBootApplication`, or write the three annotations separately. You lose nothing — the bundle is *only* convenience.
- **Broad component scanning has a small startup cost.** Irrelevant for our monolith; in huge codebases people sometimes narrow the scan deliberately. Don't optimize until you've measured a real problem — premature narrowing reintroduces the "class not found" bug.
- **Auto-configuration can configure something you didn't want.** Because it acts on "library is present," adding a dependency can quietly switch on behavior. The cure is to read the `--debug` report and exclude specific recipes — not to abandon auto-configuration, the biggest reason to use Spring Boot at all.
- **When would you not use `@SpringBootApplication`?** Only in non-standard setups: a library module with no "application" to start, or a very custom bootstrap. For any normal app — certainly our E-Commerce system — the bundle is exactly right.

---

## SECTION 9 — KEY TAKEAWAYS

- `@SpringBootApplication` is not one magic annotation but a convenience bundle of three: `@Configuration`, `@ComponentScan`, and `@EnableAutoConfiguration`.
- Component scanning searches the main class's package **and its sub-packages only**, which is exactly why the main application class must live in the top-level package with all other code beneath it.
- There is no separately installed Tomcat: the embedded server arrives as an ordinary dependency, and web auto-configuration boots it inside your own process because `spring-boot-starter-webmvc` is on the classpath.
- The `ApplicationContext` is the container that holds and connects every bean; `SpringApplication.run(...)` builds it and returns it, and it is the running application's assembled brain — already holding ~200 beans before we've written one.
- You can prove all of this yourself: the demo prints the live bean count, the `--debug` flag prints auto-configuration's condition report, and the new tests assert the context is real and populated so any future breakage fails loudly.

---

## SECTION 10 — CODING CHALLENGE WITH HIDDEN ANSWER

**Challenge.** A teammate moves the main class into a new sub-package so the file becomes:

```java
package com.ecommerce.bootstrap;   // was: package com.ecommerce;

@SpringBootApplication
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
```

…while all the other packages (`com.ecommerce.controller`, `com.ecommerce.demos`, etc.) stay directly under `com.ecommerce`. Without running anything: **(a)** Will the application still start? **(b)** Will the `ContextInspectorDemo` banner still print? **(c)** What is the single smallest change that keeps the main class in `com.ecommerce.bootstrap` *and* restores full scanning of the other packages?

<details>
  <summary>Click to reveal the answer</summary>

**(a) Yes, it still starts.** Nothing about the package move breaks startup itself. `@SpringBootApplication` still does its three jobs and Tomcat still boots. Startup success is not affected — which is what makes this bug sneaky.

**(b) No, the banner will NOT print.** Component scanning now begins at `com.ecommerce.bootstrap` and only searches *downward*. `ContextInspectorDemo` lives in `com.ecommerce.demos`, a **sibling** of `com.ecommerce.bootstrap`, not a child — so the scan never reaches it. The `@Component` marker is never read, the class never becomes a bean, and its `CommandLineRunner.run(...)` is never called. No bean, no banner. (Likewise any controllers under `com.ecommerce.controller` would be invisible and 404 — the Section 4 failure exactly.)

**(c) Smallest fix: tell the scan where to start.**

```java
package com.ecommerce.bootstrap;

@SpringBootApplication(scanBasePackages = "com.ecommerce")   // scan from the parent down
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
```

Now scanning starts at `com.ecommerce` and covers all its children again — `bootstrap`, `demos`, `controller`, everything — and the banner prints once more. (The even simpler real-world fix: don't move the main class out of the top-level package in the first place.)

**Predicted banner after the fix** (a prediction):

```
==================================================
  [Lesson 03 demo] ApplicationContext is alive.
  Beans currently managed by the context: 188
  Is 'ecommerceApplication' a bean? true
  Is the web 'dispatcherServlet' bean present? true
==================================================
```

To check it yourself: make the move, run, watch the banner vanish; add `scanBasePackages`, run again, watch it come back.

</details>

---

## SECTION 11 — WHAT IS NEXT

That completes Chapter 1. We now know *what* Spring Boot is (Lesson 01), *how* the project is put together (Lesson 02), and *what the main class and its container actually do* (this lesson). Next comes the **Chapter 1 summary** to tie it together, and then **Chapter 2** opens the container's lid for real: **Inversion of Control and Dependency Injection** — the "soul of Spring," where beans stop being an abstract word and become the thing you write every day.

---

*Lesson 03 of the curriculum — Phase 1, Chapter 1, Lesson 3 of 3. Chapter 1 complete.*
