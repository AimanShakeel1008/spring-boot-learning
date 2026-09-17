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
