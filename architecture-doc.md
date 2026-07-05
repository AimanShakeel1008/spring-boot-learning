# Architecture Document — E-Commerce Platform

The current, true state of the system. Updated at the end of every chapter.

**Last updated:** end of Chapter 1 (Getting Started) — 2026-07-05

---

## Current state: the monolith skeleton runs, with no endpoints yet

The Phase-1 monolith exists and boots. It is a single Spring Boot web application with an embedded Tomcat listening on port 8080, an `ApplicationContext` full of framework beans, and a green test suite — but it exposes no business endpoints yet, so every URL returns the Whitelabel 404 handshake.

```text
              CURRENT SYSTEM (Chapter 1 complete)
   ┌─────────────────────────────────────────────────────────┐
   │  JVM process:  mvn spring-boot:run  (or java -jar)        │
   │                                                           │
   │   SpringApplication.run(EcommerceApplication.class)       │
   │            │  reads @SpringBootApplication                │
   │            ▼                                              │
   │   ┌─────────────────────────────────────────────────┐    │
   │   │  ApplicationContext (container / "brain")         │    │
   │   │    • ecommerceApplication   (main class bean)     │    │
   │   │    • contextInspectorDemo   (labelled demo bean)  │    │
   │   │    • dispatcherServlet + ~200 framework beans     │    │
   │   └─────────────────────────────────────────────────┘    │
   │                                                           │
   │   embedded Tomcat ── listening on :8080 ─────────────────►│  browser / curl → 404
   └─────────────────────────────────────────────────────────┘

   Repository (this folder) ◄──sync──► GitHub (origin)
```

## Code that exists

| Path | Role |
|---|---|
| `phase-1-monolith/ecommerce/pom.xml` | Maven recipe: parent Boot 4.1.0, `starter-webmvc` + `starter-webmvc-test`, Boot Maven plugin |
| `.../com/ecommerce/EcommerceApplication.java` | Front-door class: `@SpringBootApplication` + `main` |
| `.../com/ecommerce/demos/ContextInspectorDemo.java` | For-learning-only demo: prints the live bean count at startup |
| `.../src/main/resources/application.yml` | Settings; only `spring.application.name` so far |
| `.../src/test/java/com/ecommerce/EcommerceApplicationTests.java` | `contextLoads()` boot-check |
| `.../src/test/java/com/ecommerce/ApplicationContextTests.java` | Asserts the `ApplicationContext` is created and populated |
| `phase-1-monolith/ecommerce/api.http` | Request collection; one `GET /` (expects 404) |

## Pinned versions

| Tool | Version |
|---|---|
| Java | 21 (LTS), Eclipse Temurin |
| Spring Boot | 4.1.0 |
| Build | Maven |

## Conventions in force from day one

- **Secrets never enter Git**: real values only in environment variables or git-ignored files; committed files carry placeholder names only (`.env.example`).
- **Prediction → verification loop**: every expected output is a prediction the learner confirms by running the real command; reality wins on any mismatch.
- **Stay-runnable invariant**: every lesson leaves the project building, running (`mvn spring-boot:run`), and passing its tests (`mvn test`, currently 4 green).
- **Top-level main class**: `EcommerceApplication` stays in the root package `com.ecommerce` so component scanning reaches every future package.

## Next architectural step

Chapter 2 fills the currently-near-empty "your beans" area of the `ApplicationContext`: Inversion of Control, Dependency Injection, the stereotype annotations (`@Component`/`@Service`/`@Repository`/`@Controller`), injection styles, bean scopes, and the bean lifecycle — the machinery every future feature is built from.
