# Lesson 02 — Project Setup and Structure

| | |
|---|---|
| **Phase** | Phase 1 — Spring Boot Foundations |
| **Chapter** | Chapter 1 — Getting Started |
| **Lesson** | 02 (Lesson 2 of 3 in this chapter) |
| **Project files created** | `phase-1-monolith/ecommerce/pom.xml` · `src/main/java/com/ecommerce/EcommerceApplication.java` · `src/main/resources/application.yml` · `src/test/java/com/ecommerce/EcommerceApplicationTests.java` · `api.http` |
| **Project files modified** | `README.md` (how-to-run added), `troubleshooting.md` (build & first-run errors) |
| **Prerequisites** | Lesson 00 (JDK, Maven, terminal, Git), Lesson 01 (starters, auto-configuration, embedded server) |

**Versions verified for this lesson (2026-07-05):** Spring Boot **4.1.0** confirmed as the current latest release (no 4.1.x patches yet) against spring.io and the Spring Boot GitHub releases page; starter names confirmed against the actual `pom.xml` that start.spring.io generates today.

---

## SECTION 1 — THE PROBLEM

You understand what Spring Boot is. Time to build. You create a folder, open it… and freeze. What files does a project need? Where does Java code go — anywhere? Where do settings live? What connects this folder to Maven? You could invent answers — code in `code/`, settings in `stuff.txt` — and you would create a project no tool can build, no tutorial matches, and no other developer can navigate. Project layout is not a place for creativity; it is a place for **convention** — the shared standard everyone and everything already understands.

There is a second freeze waiting behind the first. Say you somehow arrange the files. Now your code needs libraries — the web machinery, the JSON translator, embedded Tomcat. Where do libraries physically *come from*? A beginner's honest guess: search the web for each JAR, download it, put it… somewhere? And each library needs *other* libraries, which need others — the web starter alone ultimately involves dozens of JARs, each needing version-compatible companions. Hand-managing that is a part-time job (Section 4 shows exactly how it fails).

And a third, subtler trap, special to this exact month: **Spring Boot 4 renamed things.** The web starter name shown by essentially every tutorial ever written — `spring-boot-starter-web` — is deprecated as of Boot 4; today's correct name is `spring-boot-starter-webmvc`. Copy a 2024 tutorial's pom into a 4.1 project and you're building on a deprecated alias without knowing it. This lesson's cure for all three problems is the same: generate the skeleton from the **official, current source**, then understand every generated line so deeply that no file in the project remains a mystery. That's the real goal today — not just *having* a project, but being unable to be surprised by it.

---

## SECTION 2 — REAL WORLD ANALOGY

**Maven's dependency system is a postal-and-warehouse network for code.**

Every library in the Java world has a globally unique postal address made of three parts — country/city (**groupId**: who makes it), street (**artifactId**: which product), house number (**version**: which edition). When our recipe file lists addresses, Maven, playing the courier, drives to the world's central warehouse (**Maven Central**) and fetches each parcel. The courier is smart about two things. First, **it keeps a pantry**: everything fetched once goes into a cupboard in your house (the local repository, `~/.m2`), so the same parcel is never shipped twice. Second, **it reads the packing slips**: inside each parcel is a list of *other* parcels this one needs, and the courier fetches those too, recursively (transitive dependencies) — you ordered one meal kit, the courier assembled the whole supply chain.

One problem the courier can't solve alone: parcels must be *mutually compatible* — a stove from 2024 and gas fittings from 2019 may not connect. So we subscribe to a **catalog curated by the franchise** (the parent pom, `spring-boot-starter-parent`): for every product in the world, the catalog pre-prints the one edition tested to work with all the others. Our order forms stop mentioning editions entirely — we name the product, the catalog supplies the number. And the build itself? The kitchen runs a **fixed routine** in a fixed order — scrub counters (clean), prep (compile), taste-test (test), box up (package) — where you can request any stage and every earlier stage runs first automatically. That routine is the Maven lifecycle.

---

## SECTION 3 — THE CONCEPT EXPLAINED

### 3.1 Spring Initializr — the official skeleton generator

**Plain definition:** Spring Initializr is the official project generator — a form at `start.spring.io` that asks a handful of questions and produces a correct, buildable Spring Boot project skeleton.

**Why it exists:** the blank-folder problem. Every Spring project needs the same starting files in the same standard arrangement; generating them from the official source guarantees the layout is right *and* the versions are current — the Initializr only offers supported Spring Boot versions, which quietly protects you from starting a new project on a dead line. This "generate the standard skeleton" idea is common enough to have a name: **scaffolding** — like the temporary framework builders erect first, it gives structure to build within.

**Every option on the form, and our answer:**

| Option | Our choice | What the option means |
|---|---|---|
| **Project** | Maven | Which build tool — decides whether you get a `pom.xml` (Maven) or build scripts (Gradle, §3.2). |
| **Language** | Java | Spring also runs Kotlin and Groovy (other JVM languages); this is a Java course. |
| **Spring Boot** | **4.1.0** | The framework version — our pin, re-verified today as the actual current release. |
| **Group** | `com.ecommerce` | The maker's identity, in reverse-domain style: own `ecommerce.com` → write `com.ecommerce`. Reversing puts the most-general part first, so names sort and nest cleanly, and stay globally unique (only ecommerce.com's owner would use that prefix). Doubles as our root Java package (§3.5). |
| **Artifact** | `ecommerce` | The product's name; becomes the folder name and the built JAR's name. |
| **Name / Description** | ecommerce / one line | Cosmetic: display name in logs, a sentence for humans. |
| **Package name** | `com.ecommerce` | Group + artifact by default; the Java package our code lives in. (Group `com.ecommerce` + artifact `ecommerce` would auto-suggest the stuttering `com.ecommerce.ecommerce` — we simplify to `com.ecommerce`.) |
| **Packaging** | **Jar** | Ship as an executable JAR (embedded Tomcat, `java -jar` — Lesson 01's modern path). The alternative, War, exists for deploying into an external container — the legacy dance. |
| **Java** | 21 | Our pinned LTS. |
| **Dependencies** | **Web** | The only job we declare today. Every checkbox here becomes a starter line in the pom. We add more *only* in the lesson that teaches each one — never early. |

**How we used it:** normally you click Generate, download a zip, and unzip into your workspace. For this course the same files are written directly into the repository, each line commented — and they were generated against the Initializr's own current output (I fetched the exact pom start.spring.io produces for these options today, and ours matches it). A worthwhile five minutes anyway: visit `start.spring.io`, fill the options above, click **Explore** (preview without downloading) and compare with our files. You'll notice the Initializr also emits a few extras — empty placeholder blocks (`<licenses/>`, `<scm/>`…: slots for info public libraries publish; empty they do nothing, so we dropped them), a `HELP.md` (links file), and the *Maven wrapper* (`mvnw` + `.mvn/`): small scripts that download a project-pinned Maven automatically, so teammates don't need Maven pre-installed. Useful on teams; redundant here — you installed and verified the real Maven in Lesson 00, so our project simply uses that.

### 3.2 Maven versus Gradle — the honest one-round fight

**Gradle** is the other major Java build tool. Same job as Maven — dependencies, compile, test, package — different philosophy: Maven **describes** the build in XML (data: *what* the project is; the *how* comes from convention), Gradle **scripts** it in code (Kotlin/Groovy: shorter files, real logic allowed, generally faster on big builds through smarter caching).

So why Maven for this course, and for most Spring shops? Because a build file you can only *read* is a feature: every Maven project on earth is structured identically, there's no clever build logic to debug, and after twenty years, every error message is a solved problem online. Gradle's flexibility earns its keep on huge multi-module codebases (and it dominates Android); it also lets build files become little programs someone has to maintain. Honest summary: **both are excellent; Maven is boring; boring wins for learning and for most business backends.** Everything conceptual you learn today — coordinates, repositories, transitive dependencies, lifecycle — transfers to Gradle nearly one-to-one anyway.

### 3.3 Dependency management — the full machine

**Coordinates (GAV).** Every library is addressed by **G**roupId + **A**rtifactId + **V**ersion — e.g. `org.springframework.boot : spring-boot-starter-webmvc : (version from parent)`. A specific built product at a specific address — the actual JAR file — is called an **artifact**. Our own project has coordinates too (`com.ecommerce : ecommerce : 0.0.1-SNAPSHOT`) — in Maven's world we're not special, just another artifact. The **`-SNAPSHOT`** suffix is Maven's convention for "this edition is still being worked on, not frozen"; a real release would drop it (`1.0.0`). Ours keeps SNAPSHot until the day we'd actually publish.

**Maven Central** is the default public warehouse — a giant free server-side archive holding essentially every open-source Java library at every published version, addressed by coordinates. When the pom names a dependency, Maven translates coordinates into a download path there, fetches the JAR (plus its own little pom describing *its* dependencies), and stores it in…

**The local repository** — folder `.m2\repository` under your user folder — Maven's on-disk cache. Everything downloads exactly once per machine; every later build reads from disk. Two practical consequences you'll observe *today*: your very first build prints a waterfall of `Downloading from central:` lines and takes minutes (the pantry is empty); the second build prints none and takes seconds. And: builds after the first work offline.

**Transitive dependencies.** Each fetched library's own pom lists what *it* needs; Maven resolves recursively until the whole tree is present. Declare one starter, receive ~40 JARs — each an address the courier followed from a packing slip. See it yourself anytime with `mvn dependency:tree`, which prints the whole family tree of who-brought-whom — worth one run today just to feel the size of what one line summons.

**The parent pom — where version numbers went.** Look at our pom: neither dependency has a `<version>`. That's the `<parent>` block at work: our pom *inherits from* `spring-boot-starter-parent 4.1.0`, and that parent (through its own parent) carries a huge curated table — "for each of hundreds of libraries, use exactly *this* version" — the whole set tested **together** by the Spring team. Declaring a dependency without a version means "look it up in the inherited table." This is Lesson 01's JAR-hell cure made mechanical: changing *one* number (the parent version) upgrades the entire compatible set in lockstep, and no tutorial-copied stray version number can quietly poison the mix. The parent also carries sensible build defaults (compiler setup reading our `java.version` property, plugin versions), which is why the rest of our pom stays so short.

### 3.4 The Maven lifecycle — what `mvn <word>` actually does

**Plain definition:** Maven's build is a fixed sequence of stages called **phases**; the word after `mvn` names the phase to reach, and *every earlier phase in the sequence runs first, automatically*.

The phases that matter to us, in their fixed order:

```text
validate → compile → test → package → verify → install
             │          │        │                  │
             │          │        │                  └─ copy the built JAR into ~/.m2 (so OTHER local projects could depend on it)
             │          │        └─ produce the JAR in target/
             │          └─ run every test; ANY failure STOPS the build here
             └─ compile all source code to bytecode
```

So `mvn package` means compile **and** test **and** package — you can't package code whose tests fail; the conveyor belt halts. (`clean` — delete all previous build output — lives on a separate tiny track, which is why you say `mvn clean package`: "scrub, then run the belt through package." And now Lesson 00's mystery command `mvn clean install` decodes itself: scrub, then belt-through-install.)

**Plugins and goals.** Everything Maven does is performed by **plugins** — add-on modules, each contributing specific abilities. One concrete ability a plugin offers is a **goal**, written `plugin:goal`. Phases are really just slots that plugin goals attach to. Our pom registers one plugin explicitly: the **spring-boot-maven-plugin**, which does two things we care about: (1) attaches to `package` to *repackage* the plain thin JAR into Lesson 01's self-contained executable JAR — dependencies and Tomcat inside; (2) offers the goal `spring-boot:run` — compile-and-launch in one command, our daily driver for development.

### 3.5 Every file and folder — and what breaks if you delete it

The standard layout (convention — every tool and developer expects exactly this):

```text
phase-1-monolith/ecommerce/          <- project root: where pom.xml lives; ALL mvn commands run from here
├─ pom.xml                           <- Maven's recipe: identity, parent, dependencies, plugins
├─ api.http                          <- our click-to-send request collection (course convention, not Maven's)
├─ src/main/java/                    <- APPLICATION source code root
│  └─ com/ecommerce/                 <- folders mirror the package name, mandatorily
│     └─ EcommerceApplication.java   <- the front-door class (Lesson 03 dissects it)
├─ src/main/resources/               <- non-code files the app needs at runtime, copied onto the classpath
│  └─ application.yml                <- the settings file Boot reads automatically at startup
├─ src/test/java/                    <- TEST source code root - mirrors main's packages
│  └─ com/ecommerce/
│     └─ EcommerceApplicationTests.java  <- the "can the app even boot?" self-check
└─ target/                           <- GENERATED build output (bytecode, the JAR). Never edit, never commit -
                                        `mvn clean` deletes it; appears on first build
```

Why code and tests split into parallel trees: same packages, separate roots — so tests sit "next to" what they check, while `package` ships `main` only; test code (and test-scoped dependencies, remember our `<scope>test</scope>`) never leak into the production JAR.

One new Java-language term used above, properly: a **package** is Java's namespace for classes — a dotted name (`com.ecommerce`) grouping related classes and keeping names globally unambiguous (your `Order` vs anyone else's `Order`). Two hard rules: every class declares its package on line one, and the file must physically sit in the matching folder path under the source root — `com.ecommerce` ⇔ `com/ecommerce/`. That's why the "folders mirror the package" note says *mandatorily*: mismatch = compile error.

**The deletion tour — what breaks without each piece:**

| Delete… | What happens |
|---|---|
| `pom.xml` | Everything. `mvn` anything → `there is no POM in this directory`. The project stops being a project; it's a folder of text files again. |
| the `<parent>` block only | Build fails on the first dependency: without the inherited version table, our version-less dependencies are incomplete addresses (`'dependencies.dependency.version' ... is missing`). |
| `src/main/java` | Nothing to compile; no main class → nothing to run. The body of the application is gone. |
| `EcommerceApplication.java` only | Compiles (nothing else *to* compile), but `spring-boot:run` fails: `Unable to find a single main class`. A front door was the one thing Boot required. |
| `src/main/resources/application.yml` | **Starts fine!** Every setting in it is optional — Boot falls back to pure defaults (the app just loses its `ecommerce` name in logs). Opinionated defaults, demonstrated by deletion. |
| `src/test/java` | Builds and runs, but `mvn test` finds nothing: the safety net is gone, silently — the invariant "tests stay green" becomes meaningless when there are no tests. |
| `target/` | Completely safe — it's generated output; the next build recreates it. That's exactly why it's git-ignored (Lesson 00's `.gitignore` already covers it). |

### 3.6 The application lifecycle: from `java -jar` to the first HTTP response

The curriculum promise: no black boxes. Here is the full journey, in plain steps — what actually happens between pressing Enter and a browser getting an answer.

**Stage 1 — the JVM wakes (milliseconds).** `java -jar target\ecommerce-0.0.1-SNAPSHOT.jar` starts the JVM (Lesson 00's bytecode-runner) and hands it the JAR. Inside every JAR is a **manifest** (`META-INF/MANIFEST.MF`) — a small "shipping label" text file of facts about the archive, including the crucial one: which class holds `main()`. Our executable JAR's label (written by the spring-boot-maven-plugin during repackaging) points first to Boot's tiny built-in *launcher*, which knows how to load the dependency JARs nested inside our JAR — that nesting is the executable JAR's trick — and then calls **our** `EcommerceApplication.main()`.

**Stage 2 — Spring Boot boots (a second or two).** `main()` calls `SpringApplication.run(...)` — and Lesson 01 §3.7 plays out for real: banner printed; classpath interrogated; ~150 auto-configuration recipes' conditions evaluated (web machinery present → web recipes pass; no database library → all database recipes skip silently); surviving recipes build and wire the needed objects (proper name and lifecycle: Lesson 04). The web recipe creates embedded Tomcat, which **binds** port 8080 — *binding* meaning claiming that numbered door from the operating system: "traffic knocking on 8080 is mine." (One door, one owner — a second program binding 8080 is refused, which is exactly the `Port 8080 was already in use` error in `troubleshooting.md`.) The log announces `Tomcat started on port 8080` then `Started EcommerceApplication in ...s` — and the process does *not* exit like a normal Java program: the server thread stays alive, parked, listening. That's what "running a server" *is*: a program deliberately not finishing.

**Stage 3 — the first request (milliseconds, later).** You browse to `http://localhost:8080/`. **localhost** is the standard name every machine has for *itself* — the request never touches the internet; your browser knocks on your own machine's door 8080. Tomcat accepts the connection, parses raw HTTP text into a structured request, and hands it to Spring's web machinery, which checks its routing table: *which code claimed URL `/`?* Today: nobody — we wrote no request-handling code (that's Lesson 12's feature, never bolted on early). So Spring answers with HTTP's standard code for "no such thing here" — **404 Not Found** — dressed, for browsers, in Boot's built-in plain error page, the famous **Whitelabel Error Page** ("white label" = generic/unbranded). Read properly, that page is a *handshake*, not a failure: JVM up, Spring configured, Tomcat listening, routing consulted, honest answer returned. The whole pipeline works end to end — there's simply no content yet. (For non-browser callers, the same 404 arrives as structured JSON — you'll see both today.)

---

## SECTION 4 — THE WRONG WAY

**WRONG APPROACH: no build tool — manage the classpath by hand.**

How Java projects actually worked before build tools, applied to our app. Download each JAR yourself into a `lib\` folder, then:

```powershell
# Compile: -cp (classpath) must list EVERY jar the code touches, by hand.
javac -cp "lib\spring-web.jar;lib\spring-core.jar;lib\spring-boot.jar" src\main\java\com\ecommerce\EcommerceApplication.java

# Run: the classpath AGAIN - now including every jar those jars need at runtime.
java -cp "lib\*;src\main\java" com.ecommerce.EcommerceApplication
```

**What goes wrong:** everything, in slow motion. You needed the *transitive* tree — the web machinery alone ultimately involves dozens of JARs — but nothing tells you the list; you discover it one crash at a time. Each downloaded JAR's *version* is your guess, so Lesson 01's JAR hell returns as a lifestyle: version pairs that don't fit, discovered only at runtime. Nothing records what you did — a teammate (or you, on a new machine) must redo the scavenger hunt, and "works on my machine" becomes your biography. And there's no lifecycle: no standard test stage guarding packaging, just hand-typed commands in hopefully-the-right order.

**If this ran, the output would be (prediction)** — the classic infinite-regress session:

```text
PS> java -cp "lib\*;src\main\java" com.ecommerce.EcommerceApplication
Error: Unable to initialize main class com.ecommerce.EcommerceApplication
Caused by: java.lang.NoClassDefFoundError: org/springframework/context/ApplicationContext

# ...so you hunt down spring-context.jar, add it, rerun:
Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/commons/logging/Log

# ...find commons-logging.jar, add it, rerun:
java.lang.NoClassDefFoundError: jakarta/servlet/ServletContext

# ...and so on, one missing class per attempt, for DOZENS of jars.
```

Each `NoClassDefFoundError` means "a class that was promised isn't on the classpath" — the JVM naming exactly one missing piece per crash, never the whole list. This is the pain that made build tools inevitable — and why `pom.xml` listing two starters, with Maven resolving the rest, isn't convenience; it's civilization.

---

## SECTION 5 — THE RIGHT WAY

**CORRECT APPROACH: the standard scaffold, understood line by line, built and run with Maven.**

The five files are in the repository, every line commented — the pom (parent, coordinates, two Boot-4-named starters, the plugin), the front-door class, the near-empty settings file, the boot-check test, and `api.http`. Rather than repeat them here, this section runs them.

**Step-by-step walkthrough — first build and run:**

1. `cd d:\Projects\spring-boot-learning\phase-1-monolith\ecommerce` — commands must run where `pom.xml` lives.
2. `mvn spring-boot:run` — Maven reads the pom, sees the parent, fetches (first time only) the whole dependency tree from Maven Central into `~\.m2`, printing a long `Downloading from central:` waterfall — **minutes of it; normal; once per machine.**
3. The belt runs: sources compile to `target\classes`, then the plugin's `run` goal launches the app.
4. Stage 1–2 of §3.6 execute: banner, recipes, Tomcat binds 8080, `Started EcommerceApplication` — and the command *doesn't return to the prompt*: the server is alive in your terminal. (Stop it later with `Ctrl+C`.)
5. Browser → `http://localhost:8080/` → §3.6 Stage 3 → Whitelabel 404, the handshake.
6. Send the `api.http` request (or `curl.exe http://localhost:8080/` in a **second** PowerShell window — the first one is busy being a server) → same 404 as JSON.
7. In that second window: `mvn test` → the boot-check test starts the whole app inside the test run and goes green.

**Expected output (prediction — run it and compare):**

`mvn spring-boot:run`, after the (first-time-only) download waterfall:

```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.1.0)

... INFO ... : Starting EcommerceApplication using Java 21.0.x with PID ...
... INFO ... : No active profile set, falling back to 1 default profile: "default"
... INFO ... : Tomcat initialized with port 8080 (http)
... INFO ... : Starting service [Tomcat]
... INFO ... : Root WebApplicationContext: initialization completed in ... ms
... INFO ... : Tomcat started on port 8080 (http) with context path '/'
... INFO ... : Started EcommerceApplication in 2.1 seconds (process running for 2.6)
```

The reading list: the banner's `v4.1.0` (our pin, on your screen); `using Java 21.0.x` (Lesson 00's install, doing its job); `Tomcat started on port 8080` (Lesson 01's embedded server, real); `Started EcommerceApplication` + no prompt returned (a living server). The `No active profile` line is Boot narrating a default — profiles arrive in Lesson 30.

Browser at `http://localhost:8080/` (prediction):

```text
Whitelabel Error Page

This application has no explicit mapping for /error, so you are seeing this as a fallback.

[timestamp]
There was an unexpected error (type=Not Found, status=404).
```

The `api.http` request / `curl.exe` (prediction):

```json
{"timestamp":"2026-07-05T...","status":404,"error":"Not Found","path":"/"}
```

`mvn test` in the second window, at the tail of its output (prediction — a full app startup, banner included, happens *inside* the test):

```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
...
[INFO] BUILD SUCCESS
```

**How to run it:** the three commands above — `mvn spring-boot:run` from the project folder; the browser/`api.http`/`curl.exe` poke; `mvn test`. **Check it matches:** banner version, Java line, Tomcat line, both 404 shapes, and the green test, each against its prediction. Any difference — `troubleshooting.md` has today's likely suspects (port in use, wrong-JDK, wrong-folder), and where it's not covered, the machine is right: tell me exactly what you saw.

Optional third act, worth the two minutes — the full packaging path:

```powershell
mvn clean package      # scrub, then belt through package: compile -> TEST -> jar (watch the test run inside the build!)
java -jar target\ecommerce-0.0.1-SNAPSHOT.jar    # Lesson 01's promise, literally: the whole system, one file, one command
```

Prediction: identical startup banner from the JAR; `Ctrl+C` stops it. You have now run your application both ways the industry does: developer-style (`spring-boot:run`) and production-style (`java -jar`).

---

## SECTION 6 — APPLIED TO OUR PROJECT (OR A LABELLED DEMO)

Today's feature — the project scaffold itself — *is* applied to the project; nothing demo-labelled needed.

**Created** (paths from repo root, every line why-commented):
- `phase-1-monolith/ecommerce/pom.xml` — identity `com.ecommerce:ecommerce:0.0.1-SNAPSHOT`, parent `spring-boot-starter-parent:4.1.0`, dependencies `spring-boot-starter-webmvc` + `spring-boot-starter-webmvc-test` (test scope), `spring-boot-maven-plugin`
- `.../src/main/java/com/ecommerce/EcommerceApplication.java` — the front door
- `.../src/main/resources/application.yml` — settings; sole content `spring.application.name: ecommerce`
- `.../src/test/java/com/ecommerce/EcommerceApplicationTests.java` — the matching JUnit test this lesson requires: `contextLoads()` asserts the application can boot, guarding every future lesson
- `.../api.http` — request collection, opening with the is-it-alive `GET /`

**What this adds to our system:** existence. The E-Commerce platform is now a real, buildable, runnable, self-checking application — an engine idling with no features yet, onto which every subsequent lesson bolts capability.

**If a user interacted with it right now:** (1) browser asks `localhost:8080/` → (2) Tomcat accepts, parses → (3) routing table consulted: URL unclaimed → (4) honest 404, Whitelabel for browsers / JSON for tools → (5) log line records the handled request-cycle. Correct behavior for a system whose catalog of features is, as of today, empty.

**Build/run/test status (prediction):** `mvn spring-boot:run` starts clean to `Started EcommerceApplication`; `mvn test` → `Tests run: 1 ... BUILD SUCCESS`. The stay-green invariant begins its watch today.

---

## SECTION 7 — GOTCHAS AND COMMON MISTAKES

- **Running Maven from the wrong folder.** `mvn` works relative to the current folder and needs `pom.xml` right there; from anywhere else → `there is no POM in this directory`. It bites constantly because our *repo* root isn't the *project* root — the project is two levels down at `phase-1-monolith\ecommerce`. Habit: `mvn` fails → check where you are first.
- **Panicking at the first-build download waterfall.** Hundreds of `Downloading from central:` lines look like an explosion. It's §3.3's pantry being stocked — once per machine, ever. Broken network mid-stock can leave a corrupted download (`troubleshooting.md` covers the cure); otherwise, let it finish.
- **Copying old-tutorial starter names into a Boot 4 pom.** `spring-boot-starter-web` (and `-test`) from any pre-2026 tutorial are deprecated aliases now — they still *work*, which makes the mistake invisible until they're removed in some future release. Current names: `spring-boot-starter-webmvc`, `spring-boot-starter-webmvc-test`. Lesson 01's carbon-dating rule, now with teeth.
- **Adding version numbers to Boot-managed dependencies.** Tutorials show `<version>` lines; adding one *overrides the parent's tested table* for that library — hand-reopening the JAR-hell door. Rule: Boot-family and parent-managed dependencies get **no** version line; if Maven demands one, first ask why the parent doesn't know it, not which number to paste.
- **Treating the Whitelabel 404 as failure and force-stopping everything.** Today, that page is the success handshake (§3.6). Related reflex: the terminal "hanging" after `Started ...` is the server *being alive*, not Maven being stuck — `Ctrl+C` is the off switch, a second terminal is where other commands go.

---

## SECTION 8 — TRADEOFFS AND WHEN NOT TO USE THIS

- **Maven's rigidity vs Gradle's power, resolved honestly:** for a learning course and typical business backends, Maven's everyone-reads-it uniformity wins; at Google/Netflix scale, or on Android, Gradle's configurable, faster machinery earns its complexity. Signs you've outgrown Maven: build times dominating your day across many modules, or genuinely needing build-time logic. Not today, likely not this course, possibly never.
- **Initializr generates a *starting* skeleton, not a finished architecture.** It can't know our layered folders-to-come (`controller/`, `service/`, …) or course conventions (`api.http`, secrets discipline). Scaffolding ≠ architecture; we grow the rest deliberately, lesson by lesson.
- **`0.0.1-SNAPSHOT` forever is fine here, wrong elsewhere.** Real teams publishing artifacts must manage versions meaningfully (consumers depend on the numbers). Nobody consumes our JAR, so ceremony would be noise — a tradeoff chosen, not ignored.
- **We dropped the Maven wrapper — right here, debatable on a team.** Our Lesson-00 pinned Maven makes the wrapper redundant, and fewer mystery files beats completeness while learning. On a real team, *keep the wrapper*: it pins Maven itself per-project, one less "works on my machine."
- **When would you skip Initializr entirely?** Adding a service to a company with its own internal templates (use theirs); or generating projects programmatically (the Initializr has an API for that — today's pom was verified through it). For a fresh human-started Spring project: no good reason.

---

## SECTION 9 — KEY TAKEAWAYS

- Spring Initializr generates the standard project skeleton from the official source, and every option on its form — build tool, coordinates, packaging, Java version, dependencies — is a decision you can now explain rather than accept on faith.
- Maven locates every library by its three-part coordinates, fetches it and its whole transitive tree from Maven Central once into the `~/.m2` local cache, and our pom's parent (`spring-boot-starter-parent 4.1.0`) supplies every version number from one tested-together table — which is why our dependencies declare none.
- `mvn <phase>` runs a fixed conveyor belt (compile → test → package → …) where every earlier stage runs first and a failing test halts everything; plugins provide the actual abilities, and the spring-boot-maven-plugin adds both `spring-boot:run` and the executable-JAR repackaging.
- The standard layout is a contract: code under `src/main/java` in folders that must mirror the package name, runtime files under `src/main/resources`, tests in a parallel tree that never ships, generated output in a `target/` that's always safe to delete and never committed.
- From `java -jar` to first response: manifest → Boot launcher → `main()` → recipes and conditions → Tomcat binds 8080 → the process stays alive listening; and today's Whitelabel 404 at `/` is the pipeline's proof-of-life, not an error — the server answered honestly that no code claims that URL yet.

---

## SECTION 10 — CODING CHALLENGE WITH HIDDEN ANSWER

**Part A — hands-on: break it, read it, heal it (the safe way).** With the app stopped: (1) run `mvn clean` and look at the project folder — what vanished? (2) Run `mvn package` and watch the belt: find the moment tests run, then list what's inside `target\` (note *two* JARs — one has a name ending `.jar.original`; hypothesize before revealing). (3) Temporarily rename `application.yml` to `application.yml.off`, run `mvn spring-boot:run`, and observe what changes (hint: it's almost nothing — *which* almost-nothing?). Rename it back.

**Part B — reasoning:** (1) Your friend's pom has `spring-boot-starter-webmvc` *with* `<version>4.0.3</version>` pasted from a blog, under a `4.1.0` parent. Nothing crashes at build. What's wrong, why is "nothing crashes" the scary part, and what's the fix? (2) `mvn test` passed on your machine; the same commit fails on a friend's laptop with `release version 21 not supported`. Neither the code nor the pom differs. Which lesson-00-and-02 concept pinpoints the cause, and which two commands prove it?

<details>
  <summary>Click to reveal the answer</summary>

**Part A(1):** `target\` vanished — clean's entire job. Nothing else may be touched: everything else is source, and source is sacred.

**Part A(2):** the belt prints compile, then the test phase — banner and all, a full app boot inside the build, then `Tests run: 1` — then `jar` then the plugin's `repackage`. Inside `target\`: `classes\` (bytecode), test output folders, `ecommerce-0.0.1-SNAPSHOT.jar` (~20+ MB), and `ecommerce-0.0.1-SNAPSHOT.jar.original` (tiny). Answer to the hypothesis: `.original` is the *thin* JAR Maven's normal packaging produced (our few classes only, kilobytes); the plugin then built the *executable* JAR — our code **plus** every dependency **plus** Tomcat, with the manifest pointing at Boot's launcher — and kept the thin original under the suffix. The size gap between the two files *is* the embedded server made visible.

**Part A(3):** starts perfectly — every yml setting is optional (deletion tour, §3.5). The almost-nothing: the startup log stops calling the app `ecommerce` (the `spring.application.name` deviation is gone; watch the log's application-name mentions). Opinionated defaults, felt in the fingers.

**Part B(1):** the explicit `<version>` overrides the parent's tested table, silently mixing one 4.0.3 module into an otherwise-4.1.0 set. Scary *because* it builds: version mismatches don't fail at compile time; they fail at runtime, later, as missing-method/`NoClassDefFoundError` weirdness far from the cause (Section 4's disease, reintroduced by one pasted line). Fix: delete the `<version>` line; the parent supplies 4.1.0. General rule: a version line on a Boot-managed dependency is a red flag to investigate, not a detail to tolerate.

**Part B(2):** it's not the project — it's the *machine*: the friend's Maven is running under an older JDK, so the compiler can't target release 21. Proof: `mvn -version` on both machines (read its `Java version:` line — Lesson 00 called this the line that checks the *connection*, and `java -version` alone doesn't rule it out: Maven follows `JAVA_HOME`, the terminal follows `PATH`, and they can disagree — the `troubleshooting.md` "Maven found the wrong Java" entry). Fix on the friend's machine: point `JAVA_HOME` at a JDK 21.

</details>

---

## SECTION 11 — WHAT IS NEXT

You now own a running system and can account for every file in it — except one line we've been treating as a sealed unit: `@SpringBootApplication`. Lesson 03 breaks the seal — the three annotations fused inside it, what component scanning actually scans, and the `ApplicationContext`: the brain that every later lesson (and all of Chapter 2) revolves around.

---

*Lesson 02 of 121 — Phase 1, Chapter 1 (Getting Started), Lesson 2 of 3.*
