# Lesson 00 — Getting Your Machine Ready

| | |
|---|---|
| **Phase** | Phase 1 — Spring Boot Foundations |
| **Chapter** | Chapter 0 — Setup and First Run |
| **Lesson** | 00 (Lesson 1 of 1 in this chapter) |
| **Project files created** | `README.md`, `glossary.md`, `troubleshooting.md`, `.env.example` (repo root) |
| **Project files modified** | none — no application code exists yet |
| **Prerequisites** | none. This is the very first lesson. |

---

## SECTION 1 — THE PROBLEM

Imagine you found a great Spring Boot tutorial last night. You are excited. You open the first page, it says "run `mvn spring-boot:run`", you type it, and your computer answers:

```text
'mvn' is not recognized as an internal or external command,
operable program or batch file.
```

You search the error. The answers mention "PATH", "JAVA_HOME", "environment variables" — more words you have never seen. You try three fixes from three different forum posts. Now a *different* error appears. An hour later you have written zero lines of Java, you have four half-installed programs, and the little voice says "maybe this isn't for me."

That voice is wrong. Nothing about that evening involved programming being hard. It involved a **machine that was not prepared** and a tutorial that never said what "prepared" means. Setup is the single biggest wall between a beginner and their first running application — not because it is difficult, but because it is *invisible*: experienced developers did it years ago and forgot it exists.

There is a second, quieter problem: even people who survive setup usually install *random versions* of everything — whatever Java the first download button offered, whatever build tool a forum post mentioned. Then, weeks later, their output does not match the tutorial's output, and they cannot tell whether they made a mistake or their versions simply behave differently. That doubt is poison for learning. This lesson removes both problems at once: we install exactly the right tools, we verify each one with a command whose output we predict in advance, and we pin the versions for the entire course.

---

## SECTION 2 — REAL WORLD ANALOGY

Setting up a development machine is setting up a restaurant kitchen before opening night.

| Kitchen | Your machine | What it is for |
|---|---|---|
| The oven and stove | **JDK** (Java Development Kit) | Nothing cooks without it. Nothing runs without it. |
| The supplier + pantry organizer | **Maven** (build tool) | Fetches ingredients (other people's code) and follows one fixed recipe to assemble the dish (your app). |
| The fitted kitchen counter | **IDE** (IntelliJ IDEA) | Knives sharp, ingredients labeled, spills highlighted the moment they happen. |
| A photo album of every dish at every stage | **Git** (version control) | You can look at yesterday's version of anything, or undo a ruined sauce completely. |
| A fireproof safe in another building holding a copy of that album | **GitHub** | If the kitchen burns down (laptop dies), the album survives. |
| Speaking directly to the kitchen staff | **The terminal** | No menus, no buttons — precise spoken orders, instantly obeyed. |
| Deciding "we cook on THIS stove model all season" | **Version pinning** | Every recipe card in the book stays accurate, because the equipment never silently changes. |

Every part of the analogy maps to a real tool, and every one of those tools is explained fully in the next section.

---

## SECTION 3 — THE CONCEPT EXPLAINED

We take the tools one at a time, in dependency order — each explanation only uses words already explained.

### 3.1 The terminal — talking to the computer with text

**Plain definition:** a terminal is a window where you type a text instruction (a *command*), press Enter, and the computer answers with text.

**Why it exists:** long before screens had windows and mouse pointers, text was the only way to operate a computer. It survived because it is *precise* and *repeatable*: a command can be written down in a lesson like this one, copied exactly, and it does exactly the same thing on your machine as on mine. A sequence of mouse clicks cannot be copied and pasted; a command can. Every professional programming tool — Java, Maven, Git — is built to be driven from the terminal first, with buttons added later, if at all.

**Concrete example:** you type `java -version` and press Enter. The machine answers with three lines of text telling you which Java it has, then waits silently for your next command. That's the whole interaction model: you speak, it answers, it waits.

**The name:** this window is called a **terminal** (also: *command line*, *console*, or *shell* — people use these words almost interchangeably). On Windows 11 you already have a good one called **PowerShell**: press the Start button, type `powershell`, press Enter. A dark window with a blinking cursor appears. That blinking line, usually showing your current folder like `PS C:\Users\you>`, is called the **prompt** — the computer signaling "I'm listening."

One more term you'll meet immediately: commands often take **arguments** — extra words after the command name that tell it what exactly to do. In `java -version`, `java` is the command and `-version` is the argument meaning "just tell me your version and exit."

### 3.2 What a program is, and why Java needs a "kit"

**Plain definition:** a program is a list of instructions the computer follows. The text you write — readable by humans, saved in files — is called **source code**.

**Why source code can't run directly:** your computer's processor understands only its own numeric instruction set — raw numbers meaning "add these", "copy that". It cannot read text like `int price = 100;` any more than a microwave can read a cookbook. Something must translate.

**Java's two-step translation, concretely:**

1. You write source code in a file, say `Hello.java`.
2. A translator program called a **compiler** reads it and produces a new file, `Hello.class`, containing a compact intermediate instruction format called **bytecode**. Bytecode is not for your processor — it is for step 3.
3. A program called the **JVM** — *Java Virtual Machine* — reads the bytecode and drives your actual processor with it, live, while your program runs.

```text
Hello.java  ──(compiler: javac)──►  Hello.class  ──(JVM: java)──►  program runs
(text you wrote)                    (bytecode)                     (on YOUR processor)
```

**Why the two-step design?** Because it buys portability. Windows, Mac, and Linux processors and operating systems all differ — but each has its own JVM that speaks its local dialect. Your bytecode is *universal*; the JVM is the local interpreter. Write once on Windows, run unchanged on a Linux server. That is Java's founding promise, and it is exactly why Java rules server-side business software, where code written on laptops must run in data centers.

**What would go wrong without it:** you would compile a different program for every kind of machine, and shipping software to a server that differs from your laptop would be a gamble every single time.

**The names:**
- The whole bundle you install — compiler + JVM + helper tools — is called the **JDK**: *Java Development Kit*.
- You may also see **JRE** (*Java Runtime Environment*): the JVM *without* the compiler — enough to run Java programs but not to write them. Users need a JRE; developers need the JDK. We install the JDK.

### 3.3 Java 21 and what "LTS" means

New Java versions come out every six months, and most are maintained only until the next one arrives. But every couple of years, one version is designated **LTS** — *Long-Term Support* — meaning its maintainers promise years of bug fixes and security patches. Companies build on LTS versions because a business cannot rebuild its software every six months.

**We pin Java 21 (LTS)** for this whole course. Spring Boot 4.x requires Java 17 at minimum; Java 21 is the widely-adopted current LTS with the richest ecosystem of tutorials and answers, and it includes features (like virtual threads) we will use much later, in Phase 3.

One more term: the JDK we install is a **distribution** called **Eclipse Temurin** — Java itself is an open standard, and several organizations package trustworthy, free builds of it. Temurin (from the Adoptium project) is the most popular free one. Oracle's own build would also work; the Java inside is the same.

### 3.4 The PATH — why typing `java` anywhere works

**Plain definition:** the PATH is a list of folders your terminal searches, in order, whenever you type a command name.

**Why it exists:** when you type `java`, the terminal must find the actual file `java.exe` somewhere on your disk. Searching the entire disk every time would be absurdly slow. So the operating system keeps a short list of "folders where programs live" and searches only those.

**Concrete example:** you type `mvn -version`. The terminal walks the PATH list: folder 1 — no `mvn` here; folder 2 — no; folder 3 — found `mvn.cmd`, run it. If *no* folder on the list contains it, you get the infamous `'mvn' is not recognized...` error — which does **not** mean Maven isn't installed; it means the terminal doesn't know *where* it is.

**The name:** this list is stored in something called an **environment variable** — a named piece of text the operating system keeps for all programs to read; this particular variable is named `PATH`. Installers normally add their folder to `PATH` for you — but any terminal window that was *already open* during installation still has the old list. That's why the number-one setup fix is simply: **close the terminal and open a new one.**

A cousin you'll meet soon: `JAVA_HOME`, an environment variable that holds the folder where the JDK lives. Maven reads it to find your Java. Good installers set it; if yours doesn't, `troubleshooting.md` has the manual fix.

### 3.5 Maven — the build tool (short version, by design)

Turning a folder of source files into a running application involves many fiddly steps: fetching the exact right versions of other people's code your app uses (each such reusable piece of code is a **library**, and a library your project declares it needs is called a **dependency**), compiling everything in the right order, running checks, and packaging the result into one runnable file. A **build tool** is a program that does all of that with a single command, the same way every time, on every machine. **Maven** is the build tool we use for the entire course.

That one paragraph is genuinely all you need today. Maven is a deep tool and it gets its full treatment in Lesson 02, where we will use it for real — including what its `pom.xml` recipe file means line by line. For now: Maven = the one-command kitchen that assembles the dish.

### 3.6 The IDE — a code editor built for programming

**Plain definition:** an IDE (*Integrated Development Environment*) is a text editor that understands your programming language.

**Why it exists:** you *could* write Java in Notepad. But Notepad doesn't know Java from a shopping list. An IDE underlines errors as you type (before you ever compile), auto-completes long names so you don't memorize them, jumps you to any class's definition in one click, and runs your app with one button. The difference is cooking on a fitted counter versus cooking blindfolded.

**Concrete example:** you type `pri` and the IDE offers `println` — accept with Tab. You misspell a variable and a red underline appears *instantly*, with a tooltip explaining the problem, instead of a cryptic compiler message ten minutes later.

**Our pick:** **IntelliJ IDEA Community Edition** — free, and the strongest Java support of any editor. (VS Code with the "Extension Pack for Java" is a lighter alternative; either works for this course. Pick one and stay with it.)

### 3.7 Git — the photo album of your code

**Plain definition:** Git is a program that keeps a permanent, versioned history of every file in a folder.

**Why it exists:** without it, "saving your progress" means files named `ProjectFinal_v2_REALLYFINAL.zip`, and one bad edit can destroy a week of work with no way back. Programmers need to experiment fearlessly — try something, break things, and *always* be able to return to the last good state.

**How it works, concretely:**
1. You tell Git to watch a folder. That watched folder is called a **repository** (or "repo").
2. Whenever you reach a state worth keeping, you take a snapshot: `git add .` stages what changed, `git commit -m "message"` saves the snapshot with a human-readable label. Each snapshot is called a **commit**.
3. Every commit is kept forever. You can view any file as it was at any commit, compare versions, or roll back completely.

**What `.gitignore` is:** a plain text file listing patterns of files Git must *never* snapshot. Why it must exist: some files don't belong in history — machine-generated build output (recreatable, bulky), IDE settings (personal), and above all **secrets** like passwords and API keys, because *history is forever*: a password committed once stays visible in old commits even after you delete the file. Our `.gitignore` was written **before the first commit** precisely so nothing forbidden could ever slip in.

### 3.8 GitHub — the off-site copy

**Plain definition:** GitHub is a website that stores a copy of your Git repository online.

**Why it exists:** Git alone lives on your laptop — and laptops get stolen, die, and get coffee poured on them. GitHub holds a synchronized copy (called a **remote**) so your history survives your hardware. It's also how teams share code, and — a quiet bonus — a public GitHub repository full of real work functions as a portfolio recruiters actually look at.

**The two commands that matter:** `git push` sends your new local commits up to GitHub; `git pull` fetches down anything new from GitHub. In this course you are the only author, so you will mostly just push after every lesson.

**Git vs GitHub, one line:** Git is the tool on your machine; GitHub is a website hosting a copy. Git works fine without GitHub; GitHub is nothing without Git.

### 3.9 Version pinning — why this course never says "latest"

**Plain definition:** pinning means choosing one exact version of each tool and refusing to drift from it.

**Why it matters *here*, specifically:** every lesson in this course shows you **predicted output** — what your screen should show — *before* you run anything. You then run the real command and compare. That comparison is the engine of this whole course: it's how you verify every claim with your own machine. But a prediction is only meaningful if we both mean the same software. "Latest" changes weekly; a pin never does.

**The pins for this course** (verified against spring.io, endoflife.date, and the Spring Boot GitHub releases on 2026-07-04):

| Tool | Pinned version | Why |
|---|---|---|
| Java | **21 (LTS)** | Spring Boot 4.x needs 17+; 21 is the widely-adopted LTS |
| Spring Boot | **4.1.x** | Current stable line (4.1.0 shipped 2026-06-10); the 3.x line reached open-source end-of-life 2026-06-30 |
| Build tool | **Maven** (current 3.9.x) | The course's one build tool throughout |

We confirm the exact Spring Boot 4.1 patch number in Lesson 02, when we actually create the project.

---

## SECTION 4 — THE WRONG WAY

**WRONG APPROACH: install "whatever Java the internet offers", skip every verification, and pin nothing.**

Say you searched "download java", clicked the first result, and got Java 8 — a version from 2014 that endless old corporate tutorials still reference. You never ran `java -version` to check what landed. Weeks from now, you try to run our Spring Boot 4 project:

```text
# What was done (the wrong way):
#   1. Downloaded "Java" from the first search result  -> silently got Java 8
#   2. Never verified with java -version
#   3. Started the course anyway
mvn spring-boot:run
```

**What goes wrong:** Spring Boot 4.x requires Java 17 as an absolute minimum. Java 8 cannot even *read* class files compiled for modern Java — the bytecode format itself is newer than the JVM. The failure doesn't say "your Java is too old" in friendly words; it says this:

**If this ran, the output would be (prediction):**

```text
Error: LinkageError occurred while loading main class com.ecommerce.EcommerceApplication
        java.lang.UnsupportedClassVersionError: com/ecommerce/EcommerceApplication
        has been compiled by a more recent version of the Java Runtime
        (class file version 65.0), this version of the Java Runtime only
        recognizes class file versions up to 52.0
```

To a beginner, that is a wall of noise. (Decoder: class file version 65 = Java 21, version 52 = Java 8 — the message is literally "your Java is 7 major versions too old", encoded in numbers nobody memorizes.) The cruelest part: this error arrives **weeks after** the mistake was made, when nothing connects it to that download. This is why we verify *every* install *today*, with a command, against a predicted output.

---

## SECTION 5 — THE RIGHT WAY

**CORRECT APPROACH: install each tool deliberately, verify each with a command, compare against the predicted output.**

All commands below are typed into **PowerShell** (Start → type `powershell` → Enter). After *each* installer finishes, **close PowerShell and open a new one** before verifying — remember from 3.4: an old window still holds the old PATH list.

> Windows 11 ships with **winget**, a command that downloads and installs programs from a curated catalog — an app store you drive from the terminal. We use it because a winget command can be written here and copied exactly, unlike "click the third button on some website." Manual-download alternatives are given too; the result is identical. (Package names in a catalog can change over time — if a winget line fails, `troubleshooting.md` covers it, and the manual path always works.)

### Step 1 — Install the JDK (Eclipse Temurin, Java 21)

```powershell
# Installs the Temurin 21 JDK from the winget catalog.
# "EclipseAdoptium.Temurin.21.JDK" is the catalog's exact package name for it.
winget install EclipseAdoptium.Temurin.21.JDK
```

*Manual alternative:* go to `https://adoptium.net`, choose **Temurin 21 (LTS)** for Windows x64, download the `.msi` installer, run it, and — important — in the installer's feature screen enable **"Set JAVA_HOME variable"** and **"Add to PATH"**.

**Verify (in a NEW PowerShell window):**

```powershell
java -version
```

**Expected output (prediction — your exact patch number, the `21.0.x`, will likely differ; the `21` must match):**

```text
openjdk version "21.0.7" 2025-04-15 LTS
OpenJDK Runtime Environment Temurin-21.0.7+6 (build 21.0.7+6-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.7+6 (build 21.0.7+6-LTS, mixed mode, sharing)
```

Line by line, in plain English: line 1 — "the Java standard I implement is version 21"; line 2 — "the runtime installed is the Temurin build"; line 3 — "the JVM flavor is the 64-bit server one" (details that won't matter to us). **The only thing you check: it says `21`.**

**Run it and compare.** If your output shows a different major number or an error, stop here and check `troubleshooting.md` — every later lesson depends on this one line being right. If it differs in a way troubleshooting doesn't cover, the machine is right: tell me exactly what you saw.

### Step 2 — Install Maven

```powershell
# Installs Apache Maven from the winget catalog.
winget install Apache.Maven
```

*Manual alternative:* go to `https://maven.apache.org/download.cgi`, download the **binary zip**, extract it to a simple path like `C:\tools\apache-maven`, then add its `bin` subfolder to your PATH (steps for editing PATH by hand are in `troubleshooting.md`).

**Verify (new PowerShell window):**

```powershell
mvn -version
```

**Expected output (prediction — exact numbers and paths will differ; look for a Maven 3.9-or-newer line and a Java 21 line):**

```text
Apache Maven 3.9.10 (...)
Maven home: C:\...\apache-maven-3.9.10
Java version: 21.0.7, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-21.0.7-hotspot
Default locale: en_US, platform encoding: UTF-8
OS name: "windows 11", version: ...
```

Notice the third line: Maven reports **which Java it found**. This one command therefore verifies both tools *and* their connection. If that line shows some other Java than 21, `troubleshooting.md` → "Maven found the wrong Java."

**Run it and compare** against the prediction above.

### Step 3 — Install the IDE (IntelliJ IDEA Community Edition)

```powershell
# Installs the free Community Edition of IntelliJ IDEA.
winget install JetBrains.IntelliJIDEA.Community
```

*Manual alternative:* `https://www.jetbrains.com/idea/download` — scroll **past** the Ultimate edition to **Community Edition** (the free one; Ultimate is a paid trial we don't need).

**Verify:** launch IntelliJ IDEA from the Start menu. First-run questions: accept defaults; pick any theme. Seeing the welcome screen with "New Project" **is** the verification — no terminal command needed. Don't create a project yet; that is Lesson 02's job, done properly.

### Step 4 — Install Git

```powershell
# Installs Git for Windows.
winget install Git.Git
```

*Manual alternative:* `https://git-scm.com/download/win` — run the installer; its many option screens can all be left at their defaults.

**Verify (new PowerShell window):**

```powershell
git --version
```

**Expected output (prediction — exact number will differ):**

```text
git version 2.50.0.windows.1
```

Then introduce yourself to Git — every commit records an author, and Git refuses to commit until it knows who you are:

```powershell
# Your name as it will appear in every commit's history.
git config --global user.name "Your Name"
# The email tied to your commits - use the same one as your GitHub account.
git config --global user.email "you@example.com"
```

(`--global` means "for every repository on this machine", so this is a one-time setup. These commands print nothing when they succeed — silence is success in the terminal world.)

### Step 5 — GitHub account and the repository

1. Go to `https://github.com` → **Sign up** (free).
2. Click **+** (top right) → **New repository** → name it (e.g. `spring-boot-learning`) → leave **everything else unticked** (no README, no license — we bring our own files) → **Create repository**. An empty repository, ready to receive a push.

**On this machine, this part is already done.** This course folder is already a Git repository, connected to GitHub, with its first commit pushed. Verify it yourself — from the course folder:

```powershell
git log --oneline
```

**Expected output (prediction — the seven characters at the front, the commit's short ID, will differ):**

```text
fdacaf0 chore: initial course setup
```

That is your repository's entire history so far: one commit. For reference — because you may set this up again on another machine one day — the one-time sequence that created it was:

```bash
git init                                            # start watching this folder (creates the repository)
# write .gitignore FIRST, so forbidden files can never enter history
git add .                                           # stage everything not ignored
git commit -m "chore: initial course setup"         # snapshot it, with a label
git branch -M main                                  # name the main line of history "main"
git remote add origin https://github.com/<you>/<repo>.git   # tell Git where the online copy lives
git push -u origin main                             # send the history up to GitHub
```

(*The first `git push` opens a browser window asking you to sign in to GitHub — that's normal; a helper called Git Credential Manager, installed with Git, remembers you afterward.*)

### Step-by-step walkthrough — what happened on your machine

1. winget contacted its catalog, downloaded the Temurin 21 installer, and ran it; the installer copied the JDK into `C:\Program Files\Eclipse Adoptium\...` and appended its `bin` folder to the PATH list.
2. Opening a **new** PowerShell loaded the updated PATH; typing `java -version` made PowerShell walk that list, find `java.exe`, and run it with the argument `-version`; `java.exe` printed its identity and exited.
3. The same pattern repeated for Maven (`mvn.cmd`) and Git (`git.exe`) — install, reopen, verify, compare.
4. `git config --global ...` wrote your name and email into a small settings file in your user folder (`.gitconfig`), which every future commit will read.
5. The repository check: `git log --oneline` read the history that lives inside the hidden `.git` folder here and printed one line per commit — currently exactly one.

**How to run it:** every command above, in order, in PowerShell — closing and reopening the window after each install. **Check it matches:** compare each real output against each prediction. Where they differ, the machine is right — `troubleshooting.md` first, and if it's not covered, tell me exactly what you saw and we will work out why.

---

## SECTION 6 — APPLIED TO OUR PROJECT

This is a **pure-setup lesson**: it teaches no Spring Boot feature, so there is no application code to write — and that is stated plainly rather than forced. What it *does* contribute to the project is its opening set of living documents, all at the repository root:

| File | Purpose |
|---|---|
| `README.md` | The project's front page: what this is, what's built so far, how to run it. Grows every time the system gains a capability. |
| `glossary.md` | Every term this course defines, with its plain-language meaning and the lesson where it first appeared. Your lookup index. |
| `troubleshooting.md` | The errors a beginner actually hits, decoded: plain-language cause + exact fix. Starts today with setup errors. |
| `.env.example` | A template listing the **names** of secret values the project will need (none yet). Real values never go in Git — this convention starts on day one so a secret can never land in history later. |

**If you interacted with the repository right now:** `git status` would list these four new files (plus this lesson file and the chapter summary) as untracked; the commit command at the end of this lesson snapshots them; `git push` sends them to GitHub — where `README.md` immediately renders as your repository's front page.

**Build/test status:** there is no application yet, so there is nothing to build or run; the repository remains exactly as valid as before — plain files, cleanly tracked. (Prediction: `git status` shows only the new files above; nothing is broken because nothing executable exists yet.)

---

## SECTION 7 — GOTCHAS AND COMMON MISTAKES

- **Verifying in a terminal that was already open during the install.** The old window holds the old PATH list, so the tool "isn't recognized" even though it installed fine. Developers hit this constantly because keeping one terminal open all day feels natural. Fix: after any install, close the terminal and open a new one — make it a reflex.
- **Having two Javas and not knowing which one answers.** Old installers, other apps, and games leave Javas behind; PATH order decides which wins, and it may not be your new 21. Avoid it: `where java` (note: `where.exe java` if plain `where` misbehaves in PowerShell) lists every `java.exe` on the PATH, top one wins; `troubleshooting.md` shows how to reorder.
- **Grabbing IntelliJ IDEA *Ultimate* instead of Community.** The download page shows Ultimate first and biggest; it's a 30-day paid trial, and when it expires mid-course you'd think you lost your IDE. Community is free forever and fully sufficient here.
- **Writing `.gitignore` *after* the first `git add`.** Files staged before the ignore rule existed are already in history — including, catastrophically, secrets; deleting them later does not delete them from old commits. Ours was written first, deliberately. If a secret ever does land in history, the only safe response is to treat it as leaked: change the secret itself.
- **Skipping verification because the installer said "Success".** The installer only promises files were copied — not that the terminal can find them, not that Maven found the right Java. The version commands are ten seconds each and convert "probably fine" into "verified"; that habit — *never trust, always verify with a command* — is the single most professional reflex this course will build in you.

---

## SECTION 8 — TRADEOFFS AND WHEN NOT TO USE THIS

- **winget vs manual installers.** winget is copy-paste-able and fast, but its catalog names can change and its versions can lag a release behind. Manual downloads always exist and always work — slower, but bulletproof. We show both; use whichever succeeds first, the result is identical.
- **Pinning versions vs staying current.** Pinning buys reproducibility — every prediction in this course stays checkable — at the cost of not getting the newest features immediately. For *learning*, pinning wins outright. In *production*, teams still pin (uncontrolled drift is how systems break at 3 a.m.) but they schedule deliberate upgrades, because security patches genuinely matter. The professional stance is never "always latest" — it is "always *chosen*."
- **When NOT to pin so hard:** the moment a security advisory lands for your pinned version, you upgrade — deliberately, with testing — rather than defending the pin. A pin is a decision, not a religion.
- **IntelliJ vs VS Code.** IntelliJ understands Java most deeply but is heavier on memory and hides more behind "magic" buttons. VS Code is lighter and more transparent but its Java support is an add-on. For a Java-centric course, IntelliJ Community is the better default; either genuinely works. The real rule: pick one, learn it well, don't switch mid-course.
- **This whole lesson assumes Windows 11**, because that is your machine. On macOS or Linux the tools are identical but the install commands differ (`brew`, `apt`, `sdkman`). The *concepts* — PATH, JDK, verification — transfer unchanged.

---

## SECTION 9 — KEY TAKEAWAYS

- A Java program runs in two steps: a compiler translates your source code into bytecode, and the JVM runs that bytecode on your actual machine — which is why the same program runs anywhere and why the JDK (compiler + JVM together) is the one non-negotiable install.
- The terminal finds commands by searching the folders listed in the PATH environment variable, which is why a freshly installed tool is "not recognized" in an old window and why "close and reopen the terminal" fixes more setup problems than anything else.
- Git keeps a permanent snapshot history of the course folder and GitHub keeps a synchronized off-machine copy, so no experiment is ever dangerous and no laptop failure can erase your work.
- `.gitignore` was written before the first commit because Git history is forever: build output, IDE settings, and above all secrets must never enter it, and the only reliable way to guarantee that is to exclude them from the very first `git add`.
- This course pins Java 21 and Spring Boot 4.1.x so that every predicted output in every lesson can be checked against your real machine — and whenever prediction and reality disagree, reality wins and we investigate why.

---

## SECTION 10 — CODING CHALLENGE WITH HIDDEN ANSWER

No code exists yet, so today's challenge is a **verification drill plus two reasoning questions** — exactly the skills this lesson built.

**Part A — drill:** in a fresh PowerShell window, run all four verification commands (`java -version`, `mvn -version`, `git --version`, and `git log --oneline` from the course folder) and check each against this lesson's predictions.

**Part B — reasoning:**
1. You type `java -version` from your Desktop folder, then from `C:\`, then from the course folder — and it works identically in all three places, even though `java.exe` lives in none of them. Explain exactly *how* the terminal finds it each time.
2. Suppose `.gitignore` were deleted right now and `git add .` were run. Name two *specific* files from this folder that would get staged for history that must never be there, and explain the distinct danger of each.

<details>
  <summary>Click to reveal the answer</summary>

**Part A:** the drill *is* the answer — four commands, four comparisons. Success = Java says `21`, Maven says `3.9.x` *and* reports Java 21 on its `Java version:` line, Git prints any modern version, and `git log --oneline` shows at least the `chore: initial course setup` commit. Any mismatch → `troubleshooting.md`, then report what the machine said.

**Part B, question 1:** the terminal never searches your current folder for `java` and never searches the whole disk. It reads the PATH environment variable — an ordered list of folders — and checks each listed folder in turn until it finds `java.exe`, then runs it. The JDK installer added `C:\Program Files\Eclipse Adoptium\jdk-21...\bin` to that list, so the search succeeds identically no matter which folder you happen to be standing in. Your current folder affects only commands that *operate on* the current folder (like `git log`), never how command programs are *found*.

**Part B, question 2:** strong answers include:
- **`CLAUDE.md`, `curriculum.md`, or `progress-tracker.md`** — the course's local-only control files. Danger: they are machine-private course machinery, not course content; committed, they clutter the public repository with files that were explicitly designed to stay local.
- **`.claude/` settings** — machine-specific tool configuration. Danger: meaningless or misleading on any other machine, and noise in every future diff.
- *(Soon, and worst of all)* **`application-local.yml` / `.env`** — real secrets like database passwords. Danger: Git history is permanent, so a secret committed once remains readable in old commits even after deletion; the only real remedy is rotating (changing) the secret itself. They don't exist yet, but the ignore rules for them are already in place — that is exactly the point of writing `.gitignore` first.
- *(Later, once code exists)* **`target/`** — Maven's build output. Danger: bulky, regenerated on every build, and guaranteed to produce meaningless conflicts.

</details>

---

## SECTION 11 — WHAT IS NEXT

Your kitchen is built: stove, pantry service, counter, photo album, off-site safe — all verified with your own hands. Lesson 01 asks the question this entire course answers: **what actually is Spring Boot, and why does it exist?** We travel from the painful old world of raw Java web programming to see exactly which agonies Spring, and then Spring Boot, were invented to cure — because you can never truly understand a tool until you've seen the pain it removes.

---

*Lesson 00 of 121 — Phase 1, Chapter 0 (Setup), Lesson 1 of 1.*
