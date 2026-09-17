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
