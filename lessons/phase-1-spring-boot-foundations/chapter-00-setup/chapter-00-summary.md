# Chapter 0 Summary — Setup and First Run

---

## SECTION 1 — WHAT WE BUILT

No application code yet — Chapter 0 built the *workshop*, and the repository's living documents:

| File / tool | Purpose |
|---|---|
| **JDK 21 (Eclipse Temurin)** — installed & verified | The compiler + JVM: nothing compiles or runs without it. Verified with `java -version`. |
| **Maven** — installed & verified | The build tool that fetches dependencies, compiles, tests, and packages with one command. Verified with `mvn -version`. |
| **IntelliJ IDEA Community** — installed | The IDE: a code editor that understands Java. Verified by reaching the welcome screen. |
| **Git + GitHub** — installed, configured, connected | Permanent local history + off-machine copy. Verified with `git --version` and `git log --oneline`. |
| `lessons/.../lesson-00-getting-your-machine-ready.md` | The full written lesson. |
| `README.md` | The repository's front page — grows with every capability. |
| `glossary.md` | Every defined term with its lesson of origin — 28 terms and counting. |
| `troubleshooting.md` | Setup errors decoded: PATH problems, wrong Java, JAVA_HOME, winget, git identity and push auth. |
| `.env.example` | The secrets-by-name-only template, in place from day one so no secret can ever reach Git history. |
| `.gitignore` | Written before the first commit: local-only course files, build output, IDE folders, and real secrets can never be staged. |

## SECTION 2 — SYSTEM STATE DIAGRAM

```text
                    YOUR MACHINE (Windows 11)
   ┌───────────────────────────────────────────────────────┐
   │                                                       │
   │   PowerShell ──finds via PATH──► java.exe  (JDK 21)   │
   │        │                         mvn.cmd  (Maven)     │
   │        │                         git.exe  (Git)       │
   │        │                                              │
   │   IntelliJ IDEA Community (ready, no project yet)     │
   │                                                       │
   │   d:\Projects\spring-boot-learning\   [Git repository]│
   │      ├─ lessons/            (curriculum archive)      │
   │      ├─ README.md  glossary.md  troubleshooting.md    │
   │      ├─ .env.example  .gitignore                      │
   │      └─ (application code: none yet)                  │
   └──────────────────────────┬────────────────────────────┘
                              │ git push / git pull
                              ▼
                    GITHUB (remote "origin")
              synchronized copy of the repository
```

## SECTION 3 — THE STORY SO FAR

Every real system's story starts before its first line of code, in decisions about tools and versions that will quietly shape everything after. Ours began by refusing the classic beginner trap — "download whatever, verify nothing" — and instead installing four tools deliberately: the JDK that makes Java possible, Maven that will assemble every build for the next hundred-plus lessons, an IDE to write in, and Git to make every step reversible.

Two decisions matter most in hindsight. First, **everything was verified with a command whose output was predicted in advance** — the habit of "never trust, always check against reality" that this entire course runs on. Second, **`.gitignore` existed before the first commit**, which means the repository was born structurally incapable of leaking secrets or committing junk. Neither decision produced anything demo-able today; both will pay rent in every chapter that follows.

The versions are pinned — Java 21, Spring Boot 4.1.x, Maven — so from here on, when a lesson predicts an output, your machine can confirm or refute it on equal terms. The kitchen is built. Next, we find out what we're cooking and why the recipe (Spring Boot) exists at all.

## SECTION 4 — ALL LESSONS AT A GLANCE

| Lesson | Title | Core concept | Key tool / command | Output demonstrated |
|---|---|---|---|---|
| 00 | Getting Your Machine Ready | The toolchain: compile-run model, PATH, version control, version pinning | `java -version`, `mvn -version`, `git --version`, `git log` | Each tool's verified version banner (predictions confirmed by the learner's own machine) |

## SECTION 5 — HOW THE CONCEPTS CONNECT

```text
 source code (.java, text)                        version pinning
       │  compiler (javac) ── inside the ──┐      (Java 21 + Boot 4.1.x)
       ▼                        JDK        │            │ makes every
   bytecode (.class)                       │            ▼ prediction checkable
       │  JVM (java) ──────────────────────┘      predicted output ⇄ real output
       ▼                                                   ▲
   running program                                         │ produced by commands
                                                           │ typed into…
   Maven ──(uses JAVA_HOME to find JDK)── builds it   the TERMINAL, which finds
   IDE   ──(writes the source code)                   every tool via the PATH
   Git   ──(snapshots everything)── push ──► GitHub (off-site copy)
                  ▲
           .gitignore decides what may NEVER enter history (secrets, build output)
```

## SECTION 6 — CONCEPTS THAT WILL COME BACK

- **The prediction-then-verify loop** — every single lesson from here on shows predicted output that you confirm or refute by running the real thing; when they disagree, the machine wins.
- **PATH and environment variables** — return almost immediately as the mechanism for configuration and secrets (Chapter 5's profiles and the 12-factor principle stand on them).
- **The JVM and bytecode** — resurface when we unpack what `java -jar` really does at startup (Lesson 02) and again for virtual threads and native images in Phase 3.
- **Maven** — gets its full anatomy lesson in Lesson 02 and then appears in literally every lesson that builds anything.
- **The secrets-never-in-git convention** (`.gitignore` + `.env.example`) — becomes load-bearing when the database password (Chapter 4), JWT secrets (Chapter 10), and AI provider keys (Phase 4) arrive.

## SECTION 7 — SELF ASSESSMENT CHECKLIST

- [ ] I can explain the two-step journey from a `.java` text file to a running program, naming what the compiler produces and what the JVM does with it.
- [ ] I can explain why `java -version` works from any folder, and what to try first when a just-installed tool is "not recognized."
- [ ] I can explain the difference between Git and GitHub in one sentence each, and what a commit is.
- [ ] I can explain why `.gitignore` had to exist *before* the first commit, and what kind of files it protects history from.
- [ ] I can state this course's pinned versions and explain what pinning buys us.

## SECTION 8 — CHAPTER BOSS CHALLENGE

**Scenario:** your friend wants to join you on this course, on their own Windows laptop. They know basic Java syntax and nothing else. Without pointing them at any download page, **write them a setup runbook**: (a) the ordered list of what to install, one sentence per item on *why* it's needed; (b) the exact verification command for each and what to look for in its output; (c) the three mistakes most likely to bite them, with the first fix to try; (d) the one-time Git/GitHub sequence, with a one-line comment per command — and the explanation of why `.gitignore` comes when it does in that sequence.

<details>
  <summary>Click to reveal the answer</summary>

**(a) Install, in order:**
1. **JDK 21 (Eclipse Temurin)** — the compiler and JVM; without it Java neither compiles nor runs.
2. **Maven** — the build tool that fetches dependencies and assembles the app with one command.
3. **IntelliJ IDEA Community Edition** — the free IDE; flags errors while typing instead of at build time.
4. **Git** — permanent, versioned history of the project folder; makes every experiment reversible.
5. **A GitHub account + one empty repository** — the off-machine copy of that history.

**(b) Verify — new terminal window after each install:**
- `java -version` → the version line must say **21**.
- `mvn -version` → Maven **3.9.x or newer**, and its `Java version:` line must also say **21** (this checks Maven found the *right* Java).
- IntelliJ → launching to the welcome screen is the verification.
- `git --version` → any modern version prints.
- After the sequence in (d): `git log --oneline` → exactly one commit line.

**(c) Three likely mistakes:**
1. *Verifying in a terminal opened before the install* → "not recognized." First fix: close and reopen the terminal.
2. *An older Java already on the machine wins the PATH race* → wrong version prints. First fix: `where.exe java` to see the order, then remove or reorder.
3. *Downloading IntelliJ Ultimate (the paid trial shown first)* instead of Community. First fix: scroll down; get Community.

**(d) One-time sequence, from the new course folder:**

```bash
git init                                          # start the repository (Git begins watching this folder)
# create .gitignore NOW - before anything is staged
git add .                                         # stage all files EXCEPT what .gitignore excludes
git commit -m "chore: initial course setup"       # first permanent snapshot
git branch -M main                                # name the history line "main"
git remote add origin https://github.com/<user>/<repo>.git   # point at the empty GitHub repo
git push -u origin main                           # upload history; browser sign-in appears once
```

**Why `.gitignore` sits between `init` and the first `add`:** `git add .` stages everything not excluded, and history is permanent — a secret or junk file staged into commit #1 stays retrievable forever, even after deletion. The ignore rules must therefore exist *before* the first staging, so forbidden files never get their one chance to enter.

*(Also correct to add: `git config --global user.name/user.email` before the first commit — Git refuses to commit anonymously.)*

</details>

---

*Chapter 0 of Phase 1 — complete. Next: Chapter 1, Lesson 01 — What is Spring Boot and why it exists.*
