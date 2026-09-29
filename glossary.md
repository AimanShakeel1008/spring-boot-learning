# Glossary

Every term this course introduces, with the plain-language definition and the lesson where
it first appeared. This is an index for looking things up later — it never replaces the
full explanation given inline in the lesson itself.

Terms are added as they are introduced, so this file grows lesson by lesson.

| Term | Plain-language meaning | First appears |
|---|---|---|
| **Source code** | The human-readable text a programmer writes, before any translation. | Lesson 00 |
| **Machine code** | The raw numeric instructions a physical processor executes directly. Not readable by people. | Lesson 00 |
| **Compiler** | A program that translates source code written by a human into a form a machine can execute. Java's is `javac`. | Lesson 00 |
| **Bytecode** | The intermediate instruction format Java compiles to — not human text, not machine code for any specific processor. Stored in `.class` files. | Lesson 00 |
| **JVM** (Java Virtual Machine) | The program that reads bytecode and executes it on your real hardware. Each operating system has its own, which is why compiled Java is portable. | Lesson 00 |
| **JRE** (Java Runtime Environment) | The JVM plus Java's standard library. Enough to *run* Java programs, not enough to write them. | Lesson 00 |
| **JDK** (Java Development Kit) | The JRE plus the developer tools, above all the `javac` compiler. What you need in order to write Java. | Lesson 00 |
| **LTS** (Long Term Support) | A release that receives security and bug fixes for years rather than months. Production systems are built on LTS releases; we pin Java 21. | Lesson 00 |
| **Terminal** | A window where you type commands as text and the computer answers as text, instead of clicking buttons. | Lesson 00 |
| **Environment variable** | A named setting belonging to your whole computing session rather than to one program. | Lesson 00 |
| **PATH** | The environment variable holding the list of folders the system searches when you type a command name. "Command not recognized" means it searched them all and found nothing. | Lesson 00 |
| **Library** | A bundle of pre-written, reusable code that someone else wrote and published, which your project can call. | Lesson 00 |
| **JAR** (Java ARchive) | A single zip-like file containing compiled Java classes — the standard way Java code is packaged and shared. | Lesson 00 |
| **Build tool** | A program that automates compiling, testing and packaging a project, and fetches the libraries it depends on. | Lesson 00 |
| **Maven** | The build tool used throughout this course. Reads `pom.xml`, downloads dependencies, compiles, tests, packages. | Lesson 00 |
| **`pom.xml`** | Maven's project file — the written list of what a project is and what it depends on. | Lesson 00 |
| **Dependency** | A library your project needs in order to compile or run. | Lesson 00 |
| **Transitive dependency resolution** | Maven automatically fetching not just your dependencies but *their* dependencies, all the way down. The feature that ended "JAR hell". | Lesson 00 |
| **Maven Central** | The large public warehouse of published Java libraries that Maven downloads from by default. | Lesson 00 |
| **IDE** (Integrated Development Environment) | A text editor built specifically for writing code, with error-checking, navigation and refactoring built in. | Lesson 00 |
| **Git** | A tool that records a permanent, versioned history of every change to your files, stored on your own machine. | Lesson 00 |
| **GitHub** | A website that hosts copies of Git repositories online, for backup and sharing. Git is the tool; GitHub is one place to keep a copy. | Lesson 00 |
| **Repository** ("repo") | A folder whose contents Git is tracking, along with its full history. | Lesson 00 |
| **Commit** | One saved snapshot of the whole project at an instant, with a message saying what changed and why. | Lesson 00 |
| **Remote** | A copy of the repository living somewhere else, usually on GitHub. Conventionally nicknamed `origin`. | Lesson 00 |
| **Push** | Sending your new commits from your machine up to the remote. | Lesson 00 |
| **`.gitignore`** | A file listing what Git must never track — build output, IDE settings, and above all real secrets. Written *before* the first `git add`. | Lesson 00 |
| **Pinning (a version)** | Fixing a tool or library at one specific version deliberately, and writing it down, instead of accepting whatever is newest. Makes predicted output reproducible. | Lesson 00 |
| **Framework** | Pre-written code that runs your application and calls *your* code at the right moments — the reverse of a library, where you do the calling. | Lesson 01 |
| **Server** | A long-running program that waits for network requests and responds to them. A program, not a machine. | Lesson 01 |
| **Port** | A numbered door on a machine, so many programs can share one network connection. Web servers commonly use 8080 in development. | Lesson 01 |
| **HTTP** | The message format of the web: a request (what the caller sends) and a response (a status code plus data). Full treatment in Lesson 14. | Lesson 01 |
| **Servlet** | A Java class that handles an HTTP request and produces a response — the low-level standard underneath Spring's web layer. | Lesson 01 |
| **Servlet container** | The program that hosts servlets, receives raw network traffic and calls them at the right moment. Tomcat is the common one. | Lesson 01 |
| **WAR** | Web ARchive — the old packaging format, a zip copied into a separately installed server. Replaced in Boot by an executable JAR. | Lesson 01 |
| **Spring Framework** | The actual framework: the container, dependency injection, the web layer, transactions. Does the work. Currently version 7. | Lesson 01 |
| **Spring Boot** | A configuration layer on top of Spring Framework — starters, auto-configuration, an embedded server and production plumbing. Makes sure the framework is already set up when your code runs. | Lesson 01 |
| **Starter** | A pre-bundled group of libraries for one job, added as a single dependency, with versions already known to work together. | Lesson 01 |
| **Embedded server** | A server that runs as a library inside your application rather than being installed separately, so the whole app is one runnable file. | Lesson 01 |
| **Auto-configuration** | Spring Boot inspecting the classpath at startup and configuring the obvious things — a rule engine over conditions, not magic. | Lesson 01 |
| **Classpath** | The list of all compiled code and libraries available to a running Java program. Boot treats it as evidence of what you intend to build. | Lesson 01 |
| **Annotation** | A marker written with an at-sign that attaches information to a class or method, which other code can read and act on. | Lesson 01 |
| **Opinionated defaults** | Configuration choices Spring Boot applies in advance because most applications want them — always overridable. | Lesson 01 |
| **Convention over configuration** | Assuming the common convention instead of making you state it, and letting you override where you differ. | Lesson 01 |
| **`@ConditionalOnClass`** | A condition applying an auto-configuration only if a given class is on the classpath. | Lesson 01 |
| **`@ConditionalOnMissingBean`** | A condition applying an auto-configuration only if you have not already defined that object yourself. The mechanism by which Boot backs off. | Lesson 01 |
| **`@SpringBootApplication`** | The single annotation marking the application's starting point, switching on auto-configuration, and telling Spring where to look for your classes. Unpacked in Lesson 03. | Lesson 01 |
| **Condition report** | The list Boot prints with `--debug`, showing every candidate auto-configuration under "Positive matches" and "Negative matches" with the reason. | Lesson 01 |
| **Build tool** | A program that turns source code into a runnable program — fetching dependencies, compiling, testing and packaging — the same way on every machine. Ours is Maven. | Lesson 02 |
| **Maven** | The build tool this course uses. You describe *what the project is*; Maven already knows what building means. | Lesson 02 |
| **`pom.xml`** | "Project Object Model" — Maven's file describing who the project is, what it depends on, and how to build it. Delete it and the folder stops being a Maven project. | Lesson 02 |
| **Bytecode** | The compact instruction format the Java runtime actually executes. `javac` turns `.java` text into `.class` bytecode. | Lesson 02 |
| **Artifact** | Any published, downloadable output of a build — in practice, a jar file. | Lesson 02 |
| **Coordinates** | The three values that uniquely name any artifact in the world: `groupId`, `artifactId`, `version`. | Lesson 02 |
| **`groupId`** | Who published an artifact, written in reverse internet-domain order (e.g. `org.springframework.boot`). | Lesson 02 |
| **`artifactId`** | Which of a publisher's artifacts this is (e.g. `spring-boot-starter-webmvc`). | Lesson 02 |
| **Repository (Maven)** | A store of published jars addressed by coordinates. Maven Central is the public one. | Lesson 02 |
| **Maven Central** | The default public repository holding essentially every open-source Java library. | Lesson 02 |
| **Local repository** | `~/.m2/repository` on your own machine — every jar Maven has ever downloaded, cached. Checked before the internet, which is why the second build is fast. | Lesson 02 |
| **Dependency** | A library your project needs in order to compile or run. | Lesson 02 |
| **Transitive dependency** | A library that your dependency needs. Maven follows the chain to the bottom automatically — two declared dependencies become ≈40 jars. | Lesson 02 |
| **Nearest wins** | Maven's default rule for version conflicts: the version fewest steps from your `pom.xml` is used. Deterministic, but arbitrary — which is why the BOM overrides it. | Lesson 02 |
| **Parent POM** | Another `pom.xml` your project inherits from, exactly like a Java superclass. Ours is `spring-boot-starter-parent`. | Lesson 02 |
| **`<dependencyManagement>`** | "If anyone asks for this, use this version." A price list, not an order — nothing is downloaded. Contrast with `<dependencies>`, which means "fetch this". | Lesson 02 |
| **BOM (Bill of Materials)** | A POM whose only job is to pin versions for a coherent, tested set of libraries. `spring-boot-dependencies` pins ≈400 of them. | Lesson 02 |
| **Jar hell** | The state where two libraries on one classpath were never built to work together, producing `NoSuchMethodError` at run time with no hint of the real cause. | Lesson 02 |
| **`NoSuchMethodError`** | A runtime error meaning the class was there when you compiled and a *different version* of it is there now. Always a version-conflict symptom, never a code bug. | Lesson 02 |
| **Scope (`test`)** | Marks a dependency as needed only for compiling and running tests, so it is kept out of the shipped jar. | Lesson 02 |
| **Build lifecycle** | Maven's fixed, ordered sequence of phases: validate → compile → test → package → verify → install → deploy. | Lesson 02 |
| **Phase** | One point in the lifecycle. Naming a phase runs that phase *and every phase before it* — which is why `mvn package` also runs your tests. | Lesson 02 |
| **Goal** | One specific action from one specific plugin, written `plugin:goal` (e.g. `spring-boot:run`). Runs on its own and drags no phases behind it. | Lesson 02 |
| **`target/`** | The folder Maven generates: compiled classes, test reports, the jar. 100% generated, git-ignored, safe to delete at any time — which is what `mvn clean` does. | Lesson 02 |
| **Fat jar (executable jar)** | One file holding your classes, every dependency jar, and a loader that can read them — runnable with `java -jar`. Produced by the `repackage` goal. | Lesson 02 |
| **`MANIFEST.MF`** | A small text file inside every jar. Its `Main-Class:` line tells `java -jar` where to start; missing it gives "no main manifest attribute". | Lesson 02 |
| **`JarLauncher`** | The class Boot puts in `Main-Class`. It installs a class loader that can read jars nested inside a jar — something standard Java cannot do — then calls your real main class, recorded as `Start-Class`. | Lesson 02 |
| **Spring Initializr** | The official project generator at start.spring.io. Adding a starter there is identical to typing a `<dependency>` block yourself. | Lesson 02 |
| **`spring-boot-maven-plugin`** | The plugin that repackages the plain jar into an executable fat jar and provides the `spring-boot:run` goal. Remove it and `java -jar` fails. | Lesson 02 |
| **`spring-boot-starter-webmvc`** | Boot 4's web starter. Pre-2026 tutorials all say `spring-boot-starter-web`, which is now deprecated. | Lesson 02 |
| **`spring-boot-starter-webmvc-test`** | Boot 4's web test starter. Contains `spring-boot-starter-test` plus MockMvc and `RestTestClient`. | Lesson 02 |
| **`spring-boot-starter-classic`** | A migration bridge pulling in all of Boot's modules at once, for upgrading a 3.x app quickly. Deliberately not for new projects. | Lesson 02 |
| **`@SpringBootTest`** | Starts the entire application before running a test class. Makes `contextLoads()` a real test: startup failure means test failure. | Lesson 02 |
| **JUnit / Jupiter** | Java's standard testing library. "Jupiter" is the name of JUnit 5's programming model. | Lesson 02 |
| **Whitelabel Error Page** | Spring Boot's default HTML error page, shown when a browser asks for HTML and nothing is mapped. The JSON equivalent appears when the caller asks for JSON. | Lesson 02 |
| **Content negotiation** | The server choosing a response format from what the caller says it accepts, via the `Accept` header. Full treatment in Chapter 4. | Lesson 02 |
| **YAML** | An indentation-based settings format Spring Boot reads. Two spaces per level, spaces only — a tab is a syntax error. | Lesson 02 |
