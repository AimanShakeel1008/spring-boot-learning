# Troubleshooting

A wall of red text is the number-one reason beginners quit. This file turns that panic into
a two-minute lookup: the errors you will actually hit in this course, what each one really
means in plain language, and the fix.

Search this page for a distinctive phrase from your error before doing anything else.

Entries are added as lessons introduce the things that can break.

---

## Setup and toolchain (Lesson 00)

### `'java' is not recognized as the name of a cmdlet...` (or `command not found: java`)

Also applies to `mvn`, `javac` and `git`.

**What it really means.** When you type a command, your system searches the folders listed
in the `PATH` environment variable, in order. This message means it searched every one of
them and found nothing with that name. It does *not* mean the tool is broken.

**Fixes, in order of likelihood:**

1. **You have an old terminal window open.** `PATH` is read once, when a terminal starts.
   Close *every* terminal window and open a new one. This is the cause far more often than
   people expect.
2. **The tool is not installed.** Install it (Lesson 00, section 5).
3. **It is installed but not on `PATH`.** Add its `bin` folder to `PATH`, then open a new
   terminal. On Windows: Start → "environment variables" → *Edit the system environment
   variables* → *Environment Variables…* → select `Path` → *Edit* → *New*.
4. **Still failing on Windows after all that?** Sign out and back in, which forces every
   process to pick up the new environment.

---

### `No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?`

**What it really means.** Exactly what it says, and it is unusually honest. You have a Java
**Runtime** Environment, which can run compiled programs but ships no compiler. Compiling
needs the Java Development **Kit**.

**Fix.** Install a JDK (Eclipse Temurin 21 is the course default), then confirm with:

```bash
javac -version
```

If `java -version` answers but `javac -version` does not, this is your problem.

---

### `Fatal error compiling: error: invalid target release: 21`

**What it really means.** The project asked the compiler to produce Java 21 output, and the
compiler being used is older than 21, so it has never heard of that release.

**Fix.**

1. Run `mvn -version` and read the **`Java version:`** line — that is the JDK Maven is
   actually using, which is not always the one you think.
2. If it is not 21, set `JAVA_HOME` to your Java 21 installation folder and make sure the
   Java 21 `bin` folder is on `PATH` (and any older Java `bin` is not).
3. Close all terminals, open a new one, and re-check with `mvn -version`.

---

### `java -version` and `javac -version` disagree, or `mvn -version` reports a different Java

**What it really means.** You have more than one JDK installed and `PATH` finds the wrong
one first. This is silent and confusing: some things work, others fail.

**Fix.** Pick one JDK (Java 21), set `JAVA_HOME` to it explicitly, remove the other JDK's
`bin` entry from `PATH`, restart the terminal, then verify all three commands agree:

```bash
java -version
javac -version
mvn -version
```

**Rule of thumb:** trust the `Java version:` line in `mvn -version` over what you believe is
installed. That is the JDK your builds will actually use.

---

### The IDE compiles fine but the terminal fails (or vice versa)

**What it really means.** IntelliJ and VS Code can manage their own bundled JDK, separate
from the one on your `PATH`. You are running two different Javas.

**Fix.** Point the IDE's project SDK at the same Java 21 you installed. In IntelliJ:
*File → Project Structure → Project → SDK*. In this course the **terminal is the source of
truth**, because that is where every command in the lessons is run.

---

### `git push` fails with an authentication error

**What it really means.** GitHub no longer accepts account passwords for pushes.

**Fix.** Let the browser prompt sign you in (easiest), or create a personal access token on
GitHub and use it in place of your password. On Windows, Git Credential Manager ships with
Git and usually handles this for you the first time.

---

### Files I expected to stay local showed up on GitHub

**What it really means.** They were staged before `.gitignore` excluded them, or `git add -f`
forced them in.

**Fix.** Remove them from tracking while keeping your local copy:

```bash
git rm --cached path/to/file
git commit -m "chore: stop tracking local-only file"
git push
```

**Important:** this stops tracking it *going forward*. The file is still visible in the older
commits, because Git history is permanent. **If the file contained a real secret, treat that
secret as leaked — rotate it (change the password / revoke the key) first**, and only then
worry about tidying history.

---

### Git reports that every line of a file changed when you changed nothing

**What it really means.** Windows and Linux end lines differently, and the two conventions
are being mixed.

**Fix.** The committed `.gitattributes` at the repository root (`* text=auto eol=lf`) settles
this. Make sure it exists and was committed *before* the files it governs.

---

## Building and running the project (Lesson 02)

### `no main manifest attribute, in target/ecommerce-0.0.1-SNAPSHOT.jar`

**What it really means.** `java -jar` opens the jar, reads `META-INF/MANIFEST.MF`, and looks
for a `Main-Class:` line telling it where to start. There isn't one. The jar is a plain
library jar, not an executable one.

**Fixes, in order of likelihood:**

1. **The `spring-boot-maven-plugin` is missing from `pom.xml`.** That plugin's `repackage`
   goal is what rewrites the plain jar into an executable one. Without it you get a jar
   containing only your own classes, no dependencies and no manifest entry. Add it back
   under `<build><plugins>`.
2. **You never ran `mvn package`.** `mvn compile` and `mvn spring-boot:run` do not build a
   jar at all, so you may be running a stale one from an older build. Run `mvn clean package`.

---

### `BUILD FAILURE` from a failing test, when you only wanted a jar

**What it really means.** Nothing is wrong. Maven's phases are cumulative: `package` runs
`test` first, and a test failure stops the build before any jar is produced.

**Fix.** Fix the test — that is the point of it. `-DskipTests` exists and works, but a jar
built that way is an artifact you have no evidence about; never deploy one.

---

### `Could not resolve dependencies` / `Could not find artifact org.springframework.boot:...`

**What it really means.** Maven turned your `groupId`, `artifactId` and `version` into a URL
and got nothing back. There is no search — a typo produces exactly this blunt failure.

**Fixes, in order of likelihood:**

1. **Check the spelling of all three coordinates.** In this course the web starter is
   `spring-boot-starter-webmvc` (not `-web`) and the web test starter is
   `spring-boot-starter-webmvc-test` (not `-test`).
2. **You are offline, or behind a proxy/firewall,** and the jar is not yet cached. The first
   build of a project needs internet; later ones do not.
3. **A previous download was interrupted,** leaving a corrupt file in your local repository.
   Delete the offending folder under `~/.m2/repository` (Windows:
   `C:\Users\YourName\.m2\repository`) and build again. Deleting the whole folder is safe —
   it costs one slow build and nothing else.

---

### `NoSuchMethodError`, `NoClassDefFoundError` or `NoSuchFieldError` at run time, when it compiled fine

**What it really means.** The class was present when you compiled and a **different version**
of it is present now. This is a version conflict, never a bug in your own code — which is why
re-reading your code will not help.

**Fixes:**

1. **Look for a `<version>` tag on a Spring dependency in `pom.xml` and delete it.** This is
   the cause almost every time. The `spring-boot-starter-parent` already pins every Spring
   version to a tested set; writing your own overrides that guarantee. Copying a dependency
   block from a tutorial is how it gets there.
2. Run `mvn dependency:tree` and look for two versions of the same library, or a version that
   does not match your pinned Boot release.

---

### `Web server failed to start. Port 8080 was already in use.`

**What it really means.** Another program is already listening on that port — very often a
copy of this same application still running in another terminal.

**Fixes:**

1. **Stop the other one.** Find the terminal running it and press `Ctrl-C`.
2. **Find what is holding the port.** Windows: `netstat -ano | findstr :8080`, then
   `taskkill /PID <the-number> /F`. macOS/Linux: `lsof -i :8080`, then `kill <pid>`.
3. **Or just use a different port** — change `server.port` in
   `src/main/resources/application.yml`, or start with `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`.

---

### The Whitelabel Error Page, or a 404 JSON body, at `http://localhost:8080/`

**What it really means.** In this course, right now: **nothing at all.** This is the expected,
correct result. The server started, accepted your connection, parsed the request, and
correctly reported that nothing is mapped to that path — because we have not written any
endpoints yet.

Two shapes of the same 404: a browser asks for HTML and gets the Whitelabel page; curl and
`api.http` ask for JSON and get `{"timestamp":...,"status":404,...}`.

**Later in the course**, when you *have* written an endpoint and still see this, the cause is
usually one of:

1. The URL does not match the mapping (check the path and the HTTP method).
2. Your controller class sits outside the package Spring scans — see
   "My endpoint returns 404 and startup printed no error" below.

---

### `mapping values are not allowed here` or a `ScannerException` from `application.yml`

**What it really means.** Invalid YAML. Almost always one of two things.

**Fixes:**

1. **A tab character.** YAML forbids tabs for indentation, full stop. Use two spaces per
   level. Turn on "render whitespace" in your editor to see them.
2. **Inconsistent indentation.** Every key at the same level must start in the same column.

---

### The build re-downloads everything, or the first build takes minutes

**What it really means.** Nothing is wrong. Your two declared dependencies expand to roughly
forty jars, and the first build fetches all of them from Maven Central into
`~/.m2/repository`. Every later build finds them already there and runs in seconds.

---

## The main class and startup (Lesson 03)

### `Web application could not be started as there was no org.springframework.boot.web.server.servlet.ServletWebServerFactory bean defined in the context.`

Usually preceded by `APPLICATION FAILED TO START` and, in the log,
`ApplicationContextException: Unable to start web server`.

**What it really means.** Boot decided this is a servlet web application (because Spring MVC
is on the classpath), so it went to build a web server, and found *zero* objects that know how
to build one. The Tomcat jar may well be right there. Nothing used it.

**The message says "check your dependencies", but check these first, in order:**

1. **`@SpringBootApplication` is intact on the main class.** If someone replaced it with
   `@Configuration` + `@ComponentScan`, `@EnableAutoConfiguration` is missing and no recipe
   runs at all — including the one that builds Tomcat.
2. **`spring.autoconfigure.exclude` is empty** (in `application.yml` and on the command line),
   and there is no `exclude = …` on `@SpringBootApplication`. Excluding
   `TomcatServletWebServerAutoConfiguration` gives exactly this error.
3. **`spring.boot.enableautoconfiguration` is not set to `false`.**
4. **Only then the dependencies.** If Tomcat was excluded from the webmvc starter in
   `pom.xml`, add another server (such as Jetty) or put Tomcat back.

---

### `Unable to start ServletWebServerApplicationContext due to multiple ServletWebServerFactory beans`

**What it really means.** The opposite of the error above: two objects both offer to build the
web server, and Spring will not guess which one you meant. Usually you defined your own
factory bean *and* left a second server (Jetty, say) on the classpath with its recipe active.

**Fix.** Keep exactly one: remove the extra server dependency, or the extra bean.

---

### `Unable to find a @SpringBootConfiguration by searching packages upwards from the test.`

**What it really means.** `@SpringBootTest` was not told which application to start, so it
searched for the one class marked `@SpringBootConfiguration` (which `@SpringBootApplication`
carries). It looked in the test's own package and below, then moved up one package at a time,
and never found one.

**Fixes, in order of likelihood:**

1. **The test's `package` line is wrong**, or the test sits in a package tree that does not
   contain the main class. Mirror the main class's package under `src/test/java`.
2. **The main class lost its `@SpringBootApplication`.**
3. If the test genuinely lives elsewhere, name the class: `@SpringBootTest(classes = EcommerceApplication.class)`.

---

### `Found multiple @SpringBootConfiguration annotated classes`

(Wording from memory and not re-checked against 4.1.1 — search for the phrase
"multiple @SpringBootConfiguration".)

**What it really means.** There are two classes carrying `@SpringBootApplication` or
`@SpringBootConfiguration` where `@SpringBootTest`'s search can see them, so it refuses to
choose. Usually a copied sample application or a "quick experiment" class. Every
`@SpringBootTest` in the project fails at once.

**Fix.** Keep exactly one per application. Experiments go in a separate project.

---

### My endpoint returns 404 and startup printed no error

**What it really means.** Very often, Spring never found your controller. Component scanning
starts at the main class's package and covers only that package and the packages *beneath*
it. A class anywhere else is silently never created — no error, no warning.

**Check:**

1. **Is `EcommerceApplication` still in `com.ecommerce`** — the root package? If someone moved
   it into `com.ecommerce.app`, everything in `com.ecommerce.product`, `com.ecommerce.order` and
   so on is now a sibling, not a child, and invisible. Move it back with the IDE's move
   refactoring. Our test `theApplicationPackageIsTheMainClassPackage` fails when this happens.
2. **Is the controller's package really beneath `com.ecommerce`?** `com.ecommerceadmin` and
   `com.ecommerce2` look similar but are *different* packages, not children.
3. **Did someone set `scanBasePackages`?** It *replaces* the default starting point rather
   than adding to it.

---

### `** WARNING ** : Your ApplicationContext is unlikely to start due to a @ComponentScan of the default package.`

**What it really means.** The main class has no `package` line, so the scan's starting point
is "everything": every class in every jar. Startup usually fails soon afterwards.

**Fix.** Give the class a package (`package com.ecommerce;`) and put the file in the matching
folder (`src/main/java/com/ecommerce/`). Never leave any class in the default package.

---

### An auto-configuration exclusion copied from a tutorial does nothing

**What it really means.** The exclusion is written as *text* (in `spring.autoconfigure.exclude`
or `excludeName`) and names a class that does not exist in Spring Boot 4. Text names for
classes that are not on the classpath are **silently ignored**. Most recipes moved package in
Boot 4 — for example `org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`
is now `org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration`.

**Fix.** Look up the class's current package and correct the name — or better, use
`@SpringBootApplication(exclude = SomeAutoConfiguration.class)`, where a wrong name is a
compile error instead of silence. Confirm with `--debug`, but do not trust the `Exclusions:`
list for this. It shows every name you asked to exclude, valid or not (from memory of
Boot's source, not re-checked for 4.1.1). Look for the recipe itself: if it still appears
under `Positive matches`, the exclusion did nothing.

---

### `package org.springframework.boot.web.embedded.tomcat does not exist` (or `...autoconfigure.jdbc`, `...web.servlet.context`)

**What it really means.** Code copied from a Spring Boot 3.x tutorial imports a class from
its old package. Spring Boot 4 moved the web-server and auto-configuration classes into
per-technology modules.

**Fix.** Use the 4.x package. The ones you will meet first:

| Class | Spring Boot 4 package |
|---|---|
| `TomcatWebServer` | `org.springframework.boot.tomcat` |
| `TomcatServletWebServerFactory` | `org.springframework.boot.tomcat.servlet` |
| `ServletWebServerApplicationContext` | `org.springframework.boot.web.server.servlet.context` |
| `WebServerApplicationContext` | `org.springframework.boot.web.server.context` |
| `ServletWebServerFactory` | `org.springframework.boot.web.server.servlet` |
| `DataSourceAutoConfiguration` | `org.springframework.boot.jdbc.autoconfigure` |

---

### `mvn test` prints `APPLICATION FAILED TO START`, but the build succeeds

**What it really means.** Nothing is wrong. The demo test
`excludingTheTomcatRecipeMeansTheWebApplicationCannotStart` *wants* startup to fail, and it
passes because it did. The only line that decides anything is
`Tests run: 11, Failures: 0, Errors: 0`.

---

### The app started with `spring.main.web-application-type=none` and immediately exited

**What it really means.** Not a crash. With no web server there is no Tomcat thread keeping
the Java process alive, so once `main()` finished there was no work left and the program
ended normally. Remove the setting to get the web server back.
