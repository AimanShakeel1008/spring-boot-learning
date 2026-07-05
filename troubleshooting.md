# Troubleshooting — errors decoded

Real errors you may actually hit, each with the plain-language cause and the fix. Search this file (Ctrl+F) for a phrase from your error before panicking — most walls of red text have a two-minute cure.

---

## Setup (Lesson 00)

### `'java' is not recognized as an internal or external command` (same for `mvn` / `git`)

**Cause:** the terminal searches only the folders listed in the PATH environment variable, and the tool's folder isn't on that list — *or* it is, but your terminal window was opened before the install and still holds the old list.

**Fix, in order:**
1. Close the terminal completely and open a new one. (Fixes it 9 times out of 10.)
2. Still failing? The installer didn't update PATH. Add it by hand: Start → type "environment variables" → *Edit the system environment variables* → **Environment Variables…** → under *User variables* select `Path` → **Edit** → **New** → paste the tool's `bin` folder, e.g. `C:\Program Files\Eclipse Adoptium\jdk-21.0.7-hotspot\bin` — then open a new terminal and verify again.

### `java -version` shows the wrong version (e.g. 1.8 or 17 instead of 21)

**Cause:** more than one Java is installed and an older one sits earlier in the PATH list — the first match wins.

**Fix:** run `where.exe java` to see every `java.exe` the terminal can find, in search order. Then either uninstall the old Java (Settings → Apps) or move the Temurin 21 entry **above** the older one in the PATH editor (see previous entry — the *Move Up* button).

### `mvn -version` fails with `JAVA_HOME not found` / `JAVA_HOME is set to an invalid directory`

**Cause:** Maven locates Java through the `JAVA_HOME` environment variable — which must hold the JDK's folder (NOT its `bin` subfolder) — and it's missing or pointing at a deleted/old install.

**Fix:** in the same Environment Variables dialog, create or edit `JAVA_HOME` under *User variables*, value e.g. `C:\Program Files\Eclipse Adoptium\jdk-21.0.7-hotspot` (check the real folder name in File Explorer — the patch number in yours may differ). New terminal, verify with `mvn -version` and confirm its `Java version:` line says 21.

### `mvn -version` reports a different Java than `java -version`

**Cause:** the two tools resolve Java differently — `java` via PATH, Maven via `JAVA_HOME` — and they point at different installs.

**Fix:** make both point at Temurin 21 using the two entries above. They must agree, or the app you run and the app Maven builds will differ.

### `winget : The term 'winget' is not recognized`

**Cause:** winget arrives via the Microsoft Store's "App Installer" package, and this machine doesn't have it (common on fresh or corporate Windows).

**Fix:** open the Microsoft Store, search **App Installer**, update/install it. Or skip winget entirely — every Lesson 00 install has a manual-download alternative that works identically.

### `winget install ...` says `No package found matching input criteria`

**Cause:** package names in the winget catalog occasionally change.

**Fix:** search the catalog for the current name — `winget search temurin`, `winget search maven`, `winget search git` — and install the matching ID. Or use the manual download path from Lesson 00.

### `git commit` fails with `Author identity unknown` / `Please tell me who you are`

**Cause:** every commit records an author, and Git hasn't been told your name and email yet on this machine.

**Fix:**
```powershell
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
```
Then rerun the commit.

### `git push` opens a browser window asking me to sign in

**Not an error.** The first push must prove you're allowed to write to the GitHub repository. Sign in once in that browser window; Git Credential Manager (installed with Git for Windows) remembers you for all future pushes.

### `git push` rejected with `Authentication failed` or `Permission denied`

**Cause:** stored credentials are stale, or the remote URL points at a repository your account can't write to.

**Fix:** check the remote with `git remote -v` — the URL must be *your* repository. If it is, clear the saved credential (Start → "Credential Manager" → Windows Credentials → remove the `git:https://github.com` entry) and push again to trigger a fresh sign-in.

---

## Build and first run (Lesson 02)

### `The goal you specified requires a project to execute but there is no POM in this directory`

**Cause:** you ran `mvn` from a folder that has no `pom.xml`. Maven only works from the project root. In this repo the project root is two levels below the repo root.

**Fix:** `cd phase-1-monolith\ecommerce` first, then rerun the command.

### The first build prints hundreds of `Downloading from central:` lines and takes minutes

**Not an error.** Maven is stocking its local cache (`C:\Users\you\.m2\repository`) with the whole dependency tree — once per machine, ever. Let it finish; the next build reads from disk and takes seconds.

### `Could not resolve dependencies` / `Could not transfer artifact`

**Cause:** the network hiccupped mid-download (or a proxy/VPN blocked Maven Central), possibly leaving a half-downloaded file in the cache.

**Fix:** check your connection and rerun — Maven resumes what's missing. If the *same* artifact keeps failing, delete that artifact's folder inside `C:\Users\you\.m2\repository\...` (follow the group/artifact path in the error message) and rerun to force a fresh download.

### `Web server failed to start. Port 8080 was already in use.`

**Cause:** ports are exclusive — some other program (very often a previous, still-running copy of this very app in a forgotten terminal) already owns door 8080.

**Fix:** find and stop the owner:
```powershell
netstat -ano | findstr :8080        # last column = the owning process ID (PID)
taskkill /PID <that-number> /F      # stop it
```
Check any other open terminals first — if one is running the app, `Ctrl+C` there is the polite version.

### `error: release version 21 not supported` / `invalid target release: 21`

**Cause:** Maven is running under an older JDK than the project's pinned Java 21. Maven finds Java via `JAVA_HOME`, which can point somewhere different from what `java -version` shows (that one follows `PATH`).

**Fix:** run `mvn -version` and read its `Java version:` line. If it isn't 21, point `JAVA_HOME` at the Temurin 21 folder (see the JAVA_HOME entry in the Setup section above), open a new terminal, verify again.

### Browser shows "Whitelabel Error Page ... status=404"

**Usually not an error.** This is Spring Boot's built-in page for "the server is running, but no code claims that URL." While the app has no endpoints (or you mistyped a URL), this page is the *expected* answer. It only signals a real problem when a URL you *know* exists produces it — then check the app's startup log for errors and the URL for typos.

### The terminal "hangs" after `Started EcommerceApplication`

**Not stuck — alive.** A server is a program that deliberately never finishes; it's parked, listening for requests. Stop it with `Ctrl+C` in that terminal. Need to run other commands while it serves? Open a second terminal.

### `BUILD FAILURE` with `There are test failures`

**Cause:** the Maven conveyor belt halts when any test fails — that's the safety net doing its job, not Maven being difficult.

**Fix:** scroll up to the `[ERROR]` lines above the summary — they name the failing test class, method, and the expectation that broke. Run `mvn test` alone to reproduce faster. The failing assertion is the lesson: something the tests promise is no longer true.

### A tutorial says `spring-boot-starter-web` but our pom says `spring-boot-starter-webmvc`

**Not an error — a rename.** Spring Boot 4 renamed several starters (`web` → `webmvc`, `-test` variants likewise). The old names still work but are deprecated. Use the new names; treat old-name tutorials as carbon-dated (pre-Boot-4).
