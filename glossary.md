# Glossary — Spring Boot and Microservices Course

Every term this course introduces, with its plain-language definition and the lesson where it first appeared and was explained in full. This is a lookup index — the full four-part explanation (definition, why it exists, example, name) always lives inline in the lesson itself.

| Term | Plain-language definition | First appeared |
|---|---|---|
| **404 Not Found** | HTTP's standard answer for "the server is fine, but nothing lives at that URL." Status codes get their full lesson in Lesson 11. | Lesson 02 |
| **annotation** | A label starting with `@` attached to code; it doesn't run, it's read by frameworks and tools as an instruction about the code it sits on — a sticky note on a folder. | Lesson 01 |
| **`ApplicationContext`** | The container that holds every bean Spring created and manages the connections between them — the running application's assembled "brain." `SpringApplication.run(...)` builds it and returns it. | Lesson 03 |
| **bean** | An object Spring creates and manages for you (instead of you writing `new`), then hands to whoever needs it. `@Component`/`@Service`/`@Repository`/`@Controller` are all kinds of beans. Full story ("IoC"): Chapter 2. | Lesson 03 |
| **`CommandLineRunner`** | A one-method interface; a bean that implements it has its `run(...)` called once, automatically, right after startup finishes — the official place for a "do this once on boot" action. | Lesson 03 |
| **`@ComponentScan`** | One of the three annotations inside `@SpringBootApplication`: finds bean-marked classes starting from its own package and searching **every sub-package below it only** — which is why the main class sits at the top-level package. | Lesson 03 |
| **`@Configuration`** | One of the three annotations inside `@SpringBootApplication`: marks a class as a source of setup decisions and a legitimate place to define beans. | Lesson 03 |
| **condition evaluation report** | The list printed by running with `--debug` showing every auto-configuration recipe and whether its conditions matched ("Positive/Negative matches") — `@EnableAutoConfiguration` showing its work. | Lesson 03 |
| **`DispatcherServlet`** | The central web bean that web auto-configuration creates; its presence in the context is the fingerprint that the web layer was configured. Full treatment: Chapter 3. | Lesson 03 |
| **`@EnableAutoConfiguration`** | One of the three annotations inside `@SpringBootApplication`: switches on the auto-configuration rule engine (Lesson 01) — apply every recipe whose conditions pass. | Lesson 03 |
| **argument (command)** | An extra word after a command's name telling it what exactly to do — in `java -version`, `-version` is the argument. | Lesson 00 |
| **artifact** | A specific built product at a specific Maven address — in practice, the actual JAR file identified by coordinates. Our project is itself an artifact: `com.ecommerce:ecommerce:0.0.1-SNAPSHOT`. | Lesson 02 |
| **auto-configuration** | Spring Boot's startup rule engine: ~150 candidate "recipes," each guarded by conditions (mainly "is the library on the classpath?" and "did the user define their own?"); surviving recipes configure the app. | Lesson 01 |
| **binding (a port)** | A program claiming a numbered network door from the operating system ("traffic on 8080 is mine"). One door, one owner — hence the "port already in use" error. | Lesson 02 |
| **boilerplate** | Repetitive ceremony code you must write that expresses nothing about your actual problem — and buries the lines that do. | Lesson 01 |
| **build tool** | A program that turns your source files into a running application with one command: fetches dependencies, compiles, tests, packages. Ours is Maven. | Lesson 00 |
| **bytecode** | The compact intermediate instruction format the Java compiler produces from your source code; universal across operating systems, read by the JVM. | Lesson 00 |
| **classpath** | The list of places (folders and JAR files) where the JVM looks for compiled code while running your program — PATH's twin, but for code instead of programs. Maven assembles it from your dependencies. | Lesson 01 |
| **commit** | One saved snapshot of the repository, kept forever, with a human-readable message describing it. | Lesson 00 |
| **compiler** | A translator program that turns human-written source code into a machine-usable form (for Java: into bytecode). | Lesson 00 |
| **convention over configuration** | The philosophy behind opinionated defaults: follow the convention and you configure nothing; write configuration only where you deviate. | Lesson 01 |
| **coordinates (Maven, "GAV")** | The three-part postal address of every library: groupId (who makes it) + artifactId (which product) + version (which edition). | Lesson 02 |
| **dependency** | A library your project declares that it needs; the build tool downloads it for you. | Lesson 00 |
| **dependency conflict ("JAR hell")** | The crashes and chaos when libraries on the classpath need mutually incompatible versions of other libraries; killed for us by starters' pre-tested version sets and the parent pom's version table. | Lesson 01 |
| **dependency scope** | A marker on a dependency saying *when* it's available — e.g. `test` scope: present for running tests, excluded from the shipped application JAR. | Lesson 02 |
| **deployment** | Taking a finished program and putting it where it actually runs, started and reachable by its users. | Lesson 01 |
| **distribution (JDK)** | A packaged, trustworthy build of Java from a particular organization — ours is Eclipse Temurin. The Java inside is the same standard. | Lesson 00 |
| **embedded server** | The web server (Tomcat) shipped *inside* the application as a dependency, instead of the application being deployed into an externally installed server. Enables the one-command `java -jar` run. | Lesson 01 |
| **environment variable** | A named piece of text the operating system keeps for all programs to read — e.g. `PATH` (list of program folders) and `JAVA_HOME` (where the JDK lives). | Lesson 00 |
| **executable JAR** | A single self-contained JAR holding your code, all dependencies, and the embedded server — runnable anywhere with `java -jar app.jar`. Built for us by the spring-boot-maven-plugin's repackaging. | Lesson 01 |
| **framework** | A large body of pre-written code providing an application's skeleton; it runs *your* code at the right moments (vs. a library, which your code calls). | Lesson 01 |
| **Git** | The program that keeps a permanent, versioned history of a folder's files on your machine. | Lesson 00 |
| **GitHub** | A website that hosts a synchronized online copy of a Git repository — backup, sharing, and portfolio in one. | Lesson 00 |
| **.gitignore** | A text file listing patterns of files Git must never snapshot: build output, IDE settings, and above all secrets. Written before the first commit, always. | Lesson 00 |
| **goal (Maven)** | One concrete ability a Maven plugin offers, invoked as `plugin:goal` — e.g. `spring-boot:run`. Lifecycle phases are slots that goals attach to. | Lesson 02 |
| **Gradle** | The other major Java build tool: you script the build in code instead of describing it in XML — more flexible, less uniform. We use Maven. | Lesson 02 |
| **HTTP** | The agreed message format of the web: the request a caller sends and the response a server returns — the standard business-letter layout of the internet. Full treatment: Lesson 11. | Lesson 01 |
| **IDE** | Integrated Development Environment — a code editor that understands your language: flags errors as you type, auto-completes, navigates code. Ours is IntelliJ IDEA Community. | Lesson 00 |
| **JAR** | Java Archive — a zip file of compiled bytecode and resources; Java's standard shipping box for code. | Lesson 01 |
| **JDK** | Java Development Kit — the installable bundle containing the Java compiler, the JVM, and helper tools. What developers install. | Lesson 00 |
| **JRE** | Java Runtime Environment — the JVM without the compiler; enough to run Java programs but not to develop them. | Lesson 00 |
| **JUnit** | Java's standard testing library; its `@Test` marks a method as one runnable test case. Testing gets all of Chapter 11. | Lesson 02 |
| **JVM** | Java Virtual Machine — the program that reads bytecode and drives your actual processor with it while your program runs; each operating system has its own. | Lesson 00 |
| **library** | A reusable piece of code written by someone else that your program uses instead of reinventing. | Lesson 00 |
| **lifecycle / phase (Maven)** | Maven's fixed build conveyor belt — validate → compile → test → package → verify → install; `mvn <phase>` runs every earlier phase first, and a failing test halts the belt. `clean` (delete build output) is its own small track. | Lesson 02 |
| **local repository (`~/.m2`)** | Maven's on-disk cache of every downloaded artifact — each library downloads once per machine, ever; later builds read from disk. | Lesson 02 |
| **localhost** | The standard name every machine has for itself; `http://localhost:8080/` knocks on your own machine's door 8080, never touching the internet. | Lesson 02 |
| **LTS** | Long-Term Support — a version whose maintainers promise years of fixes and patches; what businesses standardize on. Our Java 21 is LTS. | Lesson 00 |
| **manifest (MANIFEST.MF)** | The small "shipping label" text file inside every JAR stating facts about it — crucially, which class holds `main()`. | Lesson 02 |
| **Maven** | The build tool used throughout this course: coordinates, Central, local cache, transitive resolution, lifecycle, plugins. | Lesson 00 (named) / Lesson 02 (full treatment) |
| **Maven Central** | The world's default public warehouse of Java libraries, addressed by coordinates — where Maven fetches everything it doesn't already have cached. | Lesson 02 |
| **Maven wrapper (`mvnw`)** | Small committed scripts that auto-download a project-pinned Maven so teammates need no pre-installed Maven. Useful on teams; we use our Lesson-00 installed Maven instead. | Lesson 02 |
| **meta-annotation (composed annotation)** | An annotation built out of other annotations: wearing it is the same as wearing all the ones it is annotated with. `@SpringBootApplication` is one — it bundles `@Configuration` + `@ComponentScan` + `@EnableAutoConfiguration`. | Lesson 03 |
| **opinionated defaults** | For every configuration question, Spring Boot ships a pre-chosen reasonable answer that applies automatically — every one of which you can override; you write down only your disagreements. | Lesson 01 |
| **package (Java)** | Java's namespace for classes: a dotted name (`com.ecommerce`) declared at the top of each file, which must mirror the file's folder path under the source root. | Lesson 02 |
| **parent POM** | A pom your pom inherits from. Ours — `spring-boot-starter-parent` — carries the tested-together version table for hundreds of libraries, which is why our dependencies declare no versions. | Lesson 02 |
| **PATH** | The environment variable holding the ordered list of folders the terminal searches when you type a command name. | Lesson 00 |
| **plugin (Maven)** | An add-on module giving Maven a concrete ability; everything Maven does is really done by plugins. The spring-boot-maven-plugin gives us `spring-boot:run` and executable-JAR repackaging. | Lesson 02 |
| **POM / `pom.xml`** | Project Object Model — Maven's recipe file describing what the project is (coordinates), what it needs (dependencies), and how to build it (plugins). No pom, no project. | Lesson 02 |
| **port** | A numbered "door" on a machine so many programs can share one network connection; our app's default door is 8080. Full treatment with HTTP in Lesson 11. | Lesson 01 |
| **prompt (terminal)** | The blinking line (e.g. `PS C:\Users\you>`) meaning the terminal is ready for your next command. | Lesson 00 |
| **push / pull** | `git push` sends your new local commits to the online copy (GitHub); `git pull` fetches new commits down from it. | Lesson 00 |
| **remote** | The online copy of a repository that Git synchronizes with — conventionally named `origin`. | Lesson 00 |
| **repository (repo)** | A folder whose complete file history Git is tracking. | Lesson 00 |
| **request / response** | The two halves of one HTTP exchange: what the caller sends ("GET me the product list") and what the server returns (a status plus data). | Lesson 01 |
| **scaffolding** | Generating a project's standard starting skeleton instead of building from a blank folder — Spring Initializr's job. | Lesson 02 |
| **server** | A program that waits for requests and answers them — never initiates, always responds. (Loosely, also the machine such programs run on.) | Lesson 01 |
| **servlet** | Java's original unit of web work: a class that handles one HTTP request and produces one response; cannot run without a servlet container. | Lesson 01 |
| **servlet container** | A server program that hosts servlets: listens on the network, parses HTTP, routes each URL to the right servlet, ships responses back. Tomcat is the famous one. | Lesson 01 |
| **SNAPSHOT** | Maven's version-suffix convention for "still in development, not a frozen release" — e.g. `0.0.1-SNAPSHOT`. | Lesson 02 |
| **source code** | The human-readable text of a program, saved in files (for Java: `.java` files). | Lesson 00 |
| **Spring Boot** | Spring Framework pre-assembled: starters + auto-configuration + embedded server + opinionated defaults. Not a replacement for Framework — a layer on top of it. | Lesson 01 |
| **Spring Framework** | The core machinery (2004, Rod Johnson): object management, web handling, data access, transactions — the engine every Spring Boot app runs on. | Lesson 01 |
| **Spring Initializr** | The official Spring project generator at start.spring.io: answer a short form, receive a correct, current, buildable skeleton. | Lesson 02 |
| **`@SpringBootApplication`** | The label on the main class; a meta-annotation bundling three jobs — `@Configuration` (this class holds config), `@ComponentScan` (find my beans from here down), `@EnableAutoConfiguration` (auto-configure what my libraries imply). | Lesson 03 |
| **@SpringBootTest** | The annotation marking a test that starts the entire application for real inside the test run. Full treatment in Lesson 59. | Lesson 02 |
| **starter** | A dependency that names a *job* ("web application") rather than a library, pulling in the whole correct, version-compatible bundle for that job. Boot 4 names: `spring-boot-starter-webmvc` (the pre-4 name `spring-boot-starter-web` is deprecated). | Lesson 01 |
| **terminal** | A window where you type text commands and the computer answers in text. Also called command line, console, or shell. Ours is PowerShell. | Lesson 00 |
| **test (automated)** | Code that checks other code and fails loudly if behavior breaks; `mvn test` runs them all, and a failure halts the build. Our first: `contextLoads()`, asserting the app can boot. | Lesson 02 |
| **Tomcat (Apache Tomcat)** | The most widely used servlet container; formerly a separate install you deployed into, now embedded inside every Spring Boot web application. | Lesson 01 |
| **transitive dependency** | A dependency of your dependency, fetched automatically and recursively — how one starter line becomes ~40 JARs. See the whole tree with `mvn dependency:tree`. | Lesson 02 |
| **version control** | Keeping a permanent history of every change to a set of files so any past state can be recovered. Git is our version control tool. | Lesson 00 |
| **version pinning** | Choosing one exact version of each tool and staying on it deliberately, so behavior and outputs stay reproducible. | Lesson 00 |
| **WAR** | Web Application Archive — the special zip layout for deploying an application *into* a servlet container; the legacy alternative to Spring Boot's executable JAR. | Lesson 01 |
| **web application** | A program whose users interact with it over the internet (via browser or app) instead of installing it — one central copy serving everyone. | Lesson 01 |
| **Whitelabel Error Page** | Spring Boot's built-in generic ("white label" = unbranded) error page for browsers. Today it's a handshake: server up, URL simply unclaimed. | Lesson 02 |
| **winget** | Windows' built-in command-line app installer — an app store driven from the terminal. | Lesson 00 |
| **XML** | A bracket-heavy text format for structured configuration (`<like><this>`); the language of pre-Boot configuration files such as `web.xml`, still used by Maven's `pom.xml`. | Lesson 01 |
| **YAML** | An indentation-based text format for settings — nesting by 2-space indents, values after colons; the format of our `application.yml`. YAML vs `.properties` in Lesson 29. | Lesson 02 |
