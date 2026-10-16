# Architecture Document

The living description of what the E-Commerce system currently is. Updated after every
chapter, so it always describes the system as it stands right now — not as it is planned
to be.

_Last updated: end of Chapter 1 (Lessons 01–03)._

## Current state

**One Spring Boot 4.1.1 application, running, with no features yet.** It lives in
`phase-1-monolith/ecommerce/`, runs on Java 21, and is built with Maven 3.9.16.

What it does today:

- Starts an **embedded Tomcat 11** web server on port 8080, inside its own Java process —
  there is no server to install.
- Routes every request through Spring MVC's `DispatcherServlet`. No controllers exist, so
  every path correctly answers **HTTP 404** (JSON for API callers, the Whitelabel page for
  browsers).
- Builds into one self-contained executable jar (`mvn clean package` →
  `java -jar target/ecommerce-0.0.1-SNAPSHOT.jar`).
- Shuts down gracefully on Ctrl+C: requests in progress finish before Tomcat stops.
- Guards how it starts with **11 tests** (`mvn test`).

## Components

| Piece | Where it comes from | Notes |
|---|---|---|
| `EcommerceApplication` | Our code, `com.ecommerce` | `@SpringBootApplication` + `main`. The only class we have written. Also a bean, `ecommerceApplication`. Its package, `com.ecommerce`, is the **scan boundary**: all future code goes in this package or beneath it. |
| `ApplicationContext` | Created by `SpringApplication.run` | An `AnnotationConfigServletWebServerApplicationContext`. Holds every bean (≈150, all but one from auto-configuration), the `Environment`, and the lifecycle. |
| Embedded Tomcat | `spring-boot-starter-tomcat` (via `-webmvc`) | Built by the auto-configured `tomcatServletWebServerFactory` bean during refresh. Its non-daemon `container-0` thread keeps the process alive after `main()` returns. |
| `DispatcherServlet` | `spring-boot-starter-webmvc` | Spring MVC's front door, auto-configured as the bean `dispatcherServlet`. |
| Settings | `src/main/resources/application.yml` | `spring.application.name: ecommerce`, `server.port: 8080`. Command-line arguments override it. No secrets. |
| Data | — | None yet. H2 arrives in Chapter 5, PostgreSQL later in the same chapter. |

## Build and dependencies

- Parent: `org.springframework.boot:spring-boot-starter-parent:4.1.1`, whose BOM pins every
  library version (Spring Framework 7.0.9, Tomcat 11.0.24, Jackson 3.1.5, JUnit Jupiter 6.0.3).
- Dependencies: `spring-boot-starter-webmvc` and `spring-boot-starter-webmvc-test` (test scope).
- Plugin: `spring-boot-maven-plugin` — repackages the jar into an executable fat jar with
  `JarLauncher`, and provides `mvn spring-boot:run`.

## System diagram

```text
  browser / curl / api.http
          │  GET http://localhost:8080/...
          ▼
┌──────────────────── one Java process (the fat jar) ─────────────────────┐
│                                                                          │
│   embedded Tomcat :8080 ──► DispatcherServlet ──► (no controllers) ──► 404 │
│        ▲                         ▲                                        │
│        │ built by a factory bean │ an auto-configured bean                │
│   ┌────┴─────────────────────────┴──────── ApplicationContext ─────────┐  │
│   │  ecommerceApplication (ours)  ·  ≈150 auto-configured  ·  Environment │  │
│   └──────────────────────────────────────────────────────────────────────┘  │
│                                                    ▲ application.yml + args │
└──────────────────────────────────────────────────────────────────────────┘
          ▲
  pom.xml ──► mvn clean package ──► target/ecommerce-0.0.1-SNAPSHOT.jar
```

## Tests

| Test class | Count | Guards |
|---|---|---|
| `EcommerceApplicationTests` | 1 | The whole application can start (`@SpringBootTest`). |
| `EcommerceApplicationAnnotationTests` | 3 | `@SpringBootApplication` contains its three parts; scanning starts at the main class's package. |
| `EcommerceApplicationContextTests` | 4 | Real context: main class is a bean, real Tomcat, application package is `com.ecommerce`, `close()` stops everything. |
| `demos/mainclass/AutoConfigurationSwitchesDemoTests` | 3 | For learning only: `web-application-type=none`, excluding the Tomcat recipe, stale exclusion ignored. |

## Where it is going

- **Phase 1** — one Spring Boot application containing users, products, orders and payments, backed by H2 and then PostgreSQL. Chapter 2 next: the container, beans and dependency injection.
- **Phase 2** — that application split into User, Product, Order, Payment and Notification services behind an API Gateway, communicating over HTTP and Kafka.
- **Phase 3** — the same system containerised, deployed through a CI/CD pipeline, tuned for performance, and extended with a reactive slice where it earns its place.
- **Phase 4** — an AI Support Assistant added as a sixth service, grounded in our own help articles and catalog.
