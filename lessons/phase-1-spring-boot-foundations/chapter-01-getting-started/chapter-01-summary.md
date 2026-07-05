# Chapter 1 Summary — Getting Started

**Phase 1 — Spring Boot Foundations · Chapter 1 · Complete**

Chapter 1 took us from "what even is Spring Boot?" to a real, running, tested application whose every moving part we can now name and explain.

---

## SECTION 1 — WHAT WE BUILT

Concrete additions across the chapter's three lessons:

| File | Purpose | Added in |
|---|---|---|
| `phase-1-monolith/ecommerce/pom.xml` | Maven recipe: parent Spring Boot 4.1.0, `spring-boot-starter-webmvc` + `spring-boot-starter-webmvc-test`, the Spring Boot Maven plugin | Lesson 02 |
| `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/EcommerceApplication.java` | The front-door class: `@SpringBootApplication` + `main` → `SpringApplication.run(...)` | Lesson 02 |
| `phase-1-monolith/ecommerce/src/main/resources/application.yml` | Settings file; only `spring.application.name: ecommerce` so far | Lesson 02 |
| `phase-1-monolith/ecommerce/src/test/java/com/ecommerce/EcommerceApplicationTests.java` | `contextLoads()` — the boot-check test that guards startup | Lesson 02 |
| `phase-1-monolith/ecommerce/api.http` | Request collection; one "is-it-alive" `GET /` (expects 404) | Lesson 02 |
| `phase-1-monolith/ecommerce/src/main/java/com/ecommerce/demos/ContextInspectorDemo.java` | Labelled for-learning-only demo: a `CommandLineRunner` that prints the live bean count and membership checks at startup | Lesson 03 |
| `phase-1-monolith/ecommerce/src/test/java/com/ecommerce/ApplicationContextTests.java` | Three tests asserting the `ApplicationContext` is created, holds our main bean, and is populated with many beans | Lesson 03 |

No new dependencies beyond Lesson 02's two starters; no secrets; no database yet.

---

## SECTION 2 — SYSTEM STATE DIAGRAM

```text
              CURRENT SYSTEM (end of Chapter 1)
   ┌─────────────────────────────────────────────────────────┐
   │  JVM process:  java -jar ecommerce (or mvn spring-boot:run)│
   │                                                           │
   │   SpringApplication.run(EcommerceApplication.class)       │
   │            │  reads @SpringBootApplication                │
   │            ▼                                              │
   │   ┌─────────────────────────────────────────────────┐    │
   │   │  ApplicationContext  (the container / "brain")    │    │
   │   │    • ecommerceApplication  (our main class bean)  │    │
   │   │    • ContextInspectorDemo  (our demo bean)        │    │
   │   │    • dispatcherServlet + ~200 framework beans     │    │
   │   │      created by web auto-configuration            │    │
   │   └─────────────────────────────────────────────────┘    │
   │                                                           │
   │   embedded Tomcat ── listening on :8080 ─────────────────►│  (browser / curl)
   │        every URL is currently unclaimed → 404 handshake   │
   └─────────────────────────────────────────────────────────┘
```

Externally the app still does nothing useful — every URL returns Spring Boot's Whitelabel 404. That is expected: Chapter 1 built the *skeleton and the engine*, not yet any endpoints. Chapter 3 gives it URLs to answer.

---

## SECTION 3 — THE STORY SO FAR

We began with the *why*. Lesson 01 walked the history from raw Java Servlets — where you hand-installed a server and drowned in XML configuration — through the Spring Framework, to Spring Boot, whose whole reason to exist is to erase that ceremony with **opinionated defaults**, **starters**, **auto-configuration**, and an **embedded server**. The lesson's core idea: Spring Boot isn't a replacement for the Spring Framework, it's a productivity layer on top that makes the sensible choices for you and lets you override only where you disagree.

Lesson 02 turned concept into a real project. We used Spring Initializr to scaffold the monolith, then took `pom.xml` apart line by line — coordinates, the parent POM's version table that kills "JAR hell," starters as *jobs* rather than libraries, transitive dependencies, the Maven lifecycle, and the Spring Boot plugin that repackages everything into a single executable JAR. We traced the whole path from `java -jar` to the first HTTP response, and met the Whitelabel 404 as a *handshake*: proof the server is up even though no URL is claimed yet.

Lesson 03 opened the magic box. `@SpringBootApplication` turned out to be three annotations in a trench coat — `@Configuration`, `@ComponentScan`, and `@EnableAutoConfiguration` — and understanding the scanning boundary (this package and its children only) explained the single most common beginner bug in advance. We learned that `SpringApplication.run(...)` returns the **`ApplicationContext`**, the container that holds every bean, and we proved it with our own eyes: a demo that prints the live bean count at startup, and three tests that will fail loudly if that container ever stops being built correctly. The engine is now not just running but *legible*.

---

## SECTION 4 — ALL LESSONS AT A GLANCE

| Lesson | Title | Core concept | Key annotation / class | Output demonstrated |
|---|---|---|---|---|
| 01 | What is Spring Boot and why it exists | Opinionated defaults, starters, auto-configuration, embedded server; Boot vs Framework | (conceptual) | — (concept lesson) |
| 02 | Project setup and structure | Initializr, Maven deep dive, the file tour, `java -jar` → first response | `@SpringBootApplication`, `SpringApplication.run`, `@SpringBootTest` | Startup banner + Whitelabel 404 handshake; `contextLoads()` green |
| 03 | The main application class | `@SpringBootApplication` unpacked; embedded Tomcat; the `ApplicationContext` | `@Configuration` + `@ComponentScan` + `@EnableAutoConfiguration`; `CommandLineRunner`; `ApplicationContext` | Live bean-count banner; 4 tests green; `--debug` condition report |

---

## SECTION 5 — HOW THE CONCEPTS CONNECT

```text
   Spring Framework  ──(pre-assembled by)──►  Spring Boot
                                                 │
        ┌────────────────────────────────────────┼───────────────────────────┐
        ▼                     ▼                    ▼                           ▼
   opinionated          starters             embedded server         auto-configuration
   defaults          (a "job" =         (Tomcat ships INSIDE       (~150 recipes, each
   (override only    correct bundle       the app as a JAR)          guarded by conditions)
    where you        of libraries)             │                          │
    disagree)             │                     │                          │
        │                 ▼                     │                          │
        │           pom.xml declares            │                          │
        │           starter-webmvc  ────────────┴──────────────────────────┤
        │                 │              (its JARs on the classpath make    │
        │                 │               the web recipes' conditions pass) │
        ▼                 ▼                                                  ▼
   application.yml   Maven builds the                       @SpringBootApplication reads them all
   (your             executable JAR                                         │
    disagreements)        │                                                 ▼
                          ▼                          SpringApplication.run() builds the
                   java -jar / mvn spring-boot:run ──────►  ApplicationContext (holds every bean)
                                                                            │
                                                                            ▼
                                                            Tomcat listens on :8080
```

---

## SECTION 6 — CONCEPTS THAT WILL COME BACK

- **The `ApplicationContext`** is the stage on which all of Chapter 2 plays out — every bean you register and every dependency you inject lives here.
- **Component scanning's package boundary** silently governs whether *any* class you write in future chapters is seen by Spring; get it wrong and controllers, services, and repositories vanish.
- **Auto-configuration and its condition report** return every time you add a starter (a database, security, Kafka) and wonder what it switched on — `--debug` is your permanent window into that.
- **The `bean` concept** introduced in passing here becomes the single most-used word in the entire course, formalized in Lesson 04.
- **Starters and the parent POM's version management** are the pattern for every future dependency: add a job, never a version, and let the tested-together table decide.

---

## SECTION 7 — SELF ASSESSMENT CHECKLIST

- [ ] I can explain why Spring Boot exists and how it differs from the Spring Framework, in plain language.
- [ ] I can read a `pom.xml` and say what the parent, each starter, and the Spring Boot plugin do.
- [ ] I can name the three annotations bundled inside `@SpringBootApplication` and say what each one does.
- [ ] I can explain why the main application class must live in the top-level package, and predict what breaks if it doesn't.
- [ ] I can describe what the `ApplicationContext` is and how `SpringApplication.run(...)` produces it.

---

## SECTION 8 — CHAPTER BOSS CHALLENGE

**Scenario.** A colleague hands you a Spring Boot project that "starts fine but none of the endpoints work — everything is 404." You look and find: the main class is `com.shop.boot.ShopApplication` (with `@SpringBootApplication`), and all the controllers are in `com.shop.web.controller`. They also mention that when they run with `--debug`, they don't see the web auto-configuration in the "Positive matches" list at all.

**What to write / answer:** (1) Name the two independent problems here. (2) Give the smallest fix for each. (3) Predict what the condition report shows before and after fixing the second problem.

<details>
  <summary>Click to reveal the answer</summary>

**Problem 1 — the scanning boundary.** The main class sits in `com.shop.boot`, so component scanning starts there and only searches downward. The controllers in `com.shop.web.controller` are a *sibling* branch, not children of `com.shop.boot`, so they're never discovered as beans → their URLs don't exist → 404 for everything.

*Smallest fix:* either move `ShopApplication` up to the top-level package `com.shop` (so the scan covers `boot`, `web`, everything), **or** add `@SpringBootApplication(scanBasePackages = "com.shop")`.

**Problem 2 — web auto-configuration not firing.** If the web recipes aren't even in the "Positive matches" list, their conditions didn't pass — which almost always means the web starter isn't on the classpath. Without `spring-boot-starter-webmvc`, there's no Tomcat and no web layer at all, so no endpoint could ever work regardless of scanning.

*Smallest fix:* add the starter to `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

then rebuild with `mvn clean install`.

**Condition report prediction.**

- *Before fixing Problem 2:* under **Negative matches** (or simply absent) you'd see the web-related auto-configurations skipped — their `@ConditionalOnClass` for the servlet/web classes did not match, because those classes aren't on the classpath.
- *After adding the starter:* those same web auto-configurations move into **Positive matches**, the `dispatcherServlet` and embedded Tomcat beans get created, and the startup logs gain a `Tomcat started on port 8080 (http)` line.

Only with **both** fixes in place do the endpoints actually respond — Problem 1 makes the controllers visible, Problem 2 makes a web server exist to route to them. To confirm, the colleague should run `mvn spring-boot:run`, watch for the Tomcat line, and hit one of the controller URLs; if it still 404s, the machine is right and there's a third issue to chase.

</details>

---

*Chapter 1 complete. Next: Chapter 2 — Inversion of Control, Dependency Injection, and the Container.*
