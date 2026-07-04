# Glossary — Spring Boot and Microservices Course

Every term this course introduces, with its plain-language definition and the lesson where it first appeared and was explained in full. This is a lookup index — the full four-part explanation (definition, why it exists, example, name) always lives inline in the lesson itself.

| Term | Plain-language definition | First appeared |
|---|---|---|
| **argument (command)** | An extra word after a command's name telling it what exactly to do — in `java -version`, `-version` is the argument. | Lesson 00 |
| **build tool** | A program that turns your source files into a running application with one command: fetches dependencies, compiles, tests, packages. Ours is Maven. | Lesson 00 |
| **bytecode** | The compact intermediate instruction format the Java compiler produces from your source code; universal across operating systems, read by the JVM. | Lesson 00 |
| **commit** | One saved snapshot of the repository, kept forever, with a human-readable message describing it. | Lesson 00 |
| **compiler** | A translator program that turns human-written source code into a machine-usable form (for Java: into bytecode). | Lesson 00 |
| **dependency** | A library your project declares that it needs; the build tool downloads it for you. | Lesson 00 |
| **distribution (JDK)** | A packaged, trustworthy build of Java from a particular organization — ours is Eclipse Temurin. The Java inside is the same standard. | Lesson 00 |
| **environment variable** | A named piece of text the operating system keeps for all programs to read — e.g. `PATH` (list of program folders) and `JAVA_HOME` (where the JDK lives). | Lesson 00 |
| **Git** | The program that keeps a permanent, versioned history of a folder's files on your machine. | Lesson 00 |
| **GitHub** | A website that hosts a synchronized online copy of a Git repository — backup, sharing, and portfolio in one. | Lesson 00 |
| **.gitignore** | A text file listing patterns of files Git must never snapshot: build output, IDE settings, and above all secrets. Written before the first commit, always. | Lesson 00 |
| **IDE** | Integrated Development Environment — a code editor that understands your language: flags errors as you type, auto-completes, navigates code. Ours is IntelliJ IDEA Community. | Lesson 00 |
| **JDK** | Java Development Kit — the installable bundle containing the Java compiler, the JVM, and helper tools. What developers install. | Lesson 00 |
| **JRE** | Java Runtime Environment — the JVM without the compiler; enough to run Java programs but not to develop them. | Lesson 00 |
| **JVM** | Java Virtual Machine — the program that reads bytecode and drives your actual processor with it while your program runs; each operating system has its own. | Lesson 00 |
| **library** | A reusable piece of code written by someone else that your program uses instead of reinventing. | Lesson 00 |
| **LTS** | Long-Term Support — a version whose maintainers promise years of fixes and patches; what businesses standardize on. Our Java 21 is LTS. | Lesson 00 |
| **Maven** | The build tool used throughout this course. Full treatment in Lesson 02. | Lesson 00 |
| **PATH** | The environment variable holding the ordered list of folders the terminal searches when you type a command name. | Lesson 00 |
| **prompt (terminal)** | The blinking line (e.g. `PS C:\Users\you>`) meaning the terminal is ready for your next command. | Lesson 00 |
| **push / pull** | `git push` sends your new local commits to the online copy (GitHub); `git pull` fetches new commits down from it. | Lesson 00 |
| **remote** | The online copy of a repository that Git synchronizes with — conventionally named `origin`. | Lesson 00 |
| **repository (repo)** | A folder whose complete file history Git is tracking. | Lesson 00 |
| **source code** | The human-readable text of a program, saved in files (for Java: `.java` files). | Lesson 00 |
| **terminal** | A window where you type text commands and the computer answers in text. Also called command line, console, or shell. Ours is PowerShell. | Lesson 00 |
| **version control** | Keeping a permanent history of every change to a set of files so any past state can be recovered. Git is our version control tool. | Lesson 00 |
| **version pinning** | Choosing one exact version of each tool and staying on it deliberately, so behavior and outputs stay reproducible. | Lesson 00 |
| **winget** | Windows' built-in command-line app installer — an app store driven from the terminal. | Lesson 00 |
