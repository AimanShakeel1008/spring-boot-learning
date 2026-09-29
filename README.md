# E-Commerce Platform — a Spring Boot & Microservices learning build

One E-Commerce system, built from nothing and reshaped across four architectural phases:

| Phase | What it is | Folder |
|---|---|---|
| 1 | A single Spring Boot application (the monolith) — users, products, orders, payments | `phase-1-monolith/` |
| 2 | The same system split into microservices behind an API gateway | `phase-2-microservices/` |
| 3 | The same system hardened for production — containers, CI/CD, performance, reactive | `phase-3-production/` |
| 4 | An AI Support Assistant added as a sixth service, built with Spring AI | `phase-2-microservices/ai-assistant-service/` |

Every lesson that builds part of this system is archived as a standalone page under [`lessons/`](lessons/), starting from the course map at `lessons/index.html`.

## Status

The Phase 1 application exists and runs. It starts an embedded Tomcat server on port 8080
and has no endpoints yet — a request to `/` correctly returns **404**, which proves the whole
chain works and only the endpoints are missing. Endpoints arrive in Chapter 4.

## Setting up

You need **Java 21** and **Maven 3.9.16** on your `PATH` (see Lesson 00). Confirm with:

```bash
java -version
mvn -version
```

## Running it

From `phase-1-monolith/ecommerce/`:

```bash
mvn spring-boot:run                                  # development: starts straight from source
```

Or build a self-contained executable jar and run that:

```bash
mvn clean package                                    # compiles, runs tests, builds the jar
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

Either way the application listens on <http://localhost:8080>.

## Running the tests

```bash
mvn test
```

One test currently runs: `contextLoads()`, which starts the entire application and fails if
anything at all prevents startup. It is the quickest answer to "did I just break something?"

## Poking it

[`phase-1-monolith/ecommerce/api.http`](phase-1-monolith/ecommerce/api.http) holds one
runnable request per endpoint. With the **REST Client** extension in VS Code, or IntelliJ's
built-in HTTP client, a "Send Request" link appears above each one. Or use curl:

```bash
curl -i http://localhost:8080/
```

Expect `HTTP/1.1 404` with a JSON body — correct for now, since nothing is mapped yet.

## What it can do

| Capability | Since | Notes |
|---|---|---|
| Starts an embedded Tomcat server on port 8080 | Lesson 02 | No external server to install |
| Packages into one runnable file | Lesson 02 | `java -jar` — roughly 20 MB, fully self-contained |
| Self-check that the application can start | Lesson 02 | `mvn test` |

## Pinned versions

- **Java 21 (LTS)** — Spring Boot 4 requires 17 or later; we use the current LTS
- **Spring Boot 4.1.1** (on Spring Framework 7)
- **Maven 3.9.16** as the build tool (Maven 4 is still a release candidate — not used here)

These are pinned for the whole course so that predicted output stays reproducible. Verified
against Maven Central and start.spring.io on 2026-09-30: `spring-boot-starter-parent:4.1.1`
is published, and Initializr's default Boot version is 4.1.1.

**A note on Spring Boot 4.** Most Spring tutorials, books and Stack Overflow answers online are written for Spring Boot 2.x or 3.x. This course is built on 4.x, where several things were renamed — `spring-boot-starter-web` is now `spring-boot-starter-webmvc`, Jackson 2 gave way to Jackson 3, and the OAuth2 starters moved under `spring-boot-starter-security-*`. Rather than leave you to discover that the hard way, every lesson flags the difference at the moment it matters, in a "Changed in Spring Boot 4" callout, so older material stays readable instead of confusing.

## Repository layout

```text
lessons/              every lesson as a standalone HTML page, organised by phase and chapter
phase-1-monolith/
  ecommerce/
    pom.xml           the build file: our coordinates, the Boot 4.1.1 parent, two starters
    api.http          one runnable HTTP request per endpoint
    src/main/java/    application source
    src/main/resources/application.yml   settings Boot reads automatically
    src/test/java/    tests
README.md             this file
glossary.md           every term the course introduces, with a plain-language definition
architecture-doc.md   the current state of the system, updated after every chapter
troubleshooting.md    the errors you will actually hit, decoded, with fixes
.env.example          the NAMES of secrets the project expects — never real values
```

## Secrets

Real secret values (database passwords, JWT signing secrets, AI provider API keys) never
go into a committed file. They live in your local environment or a git-ignored
`application-local.yml`. The committed files reference them by name only, and
[`.env.example`](.env.example) lists the expected variable names with empty values.
