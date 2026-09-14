---
name: commit-and-push
description: Full commit workflow for this repo — summarize changes, scan for secrets/build output, run the Maven build and tests, branch off develop, write a conventional commit message, and open a PR into develop with gh. Only runs when explicitly invoked; never triggers automatically.
disable-model-invocation: true
---

This repo's trunk branch is **develop**, not `main`. This workflow always produces a PR into `develop` from a fresh branch — it never commits or pushes directly to `develop` or `main`. It is deliberately more thorough than a quick commit-and-push: this is a solo learning project and the point is to practice production-grade habits (clean history, green build before merge, PR review of your own diff), not to take the fastest path.

Run every step in order. Stop and report back at any hard-stop condition instead of trying to route around it.

## 1. Summarize what changed

Run in parallel:
- `git status` (never `-uall`)
- `git diff` (unstaged) and `git diff --staged` (already staged)
- `git branch --show-current`

Summarize in plain language: which files changed, roughly what kind of change (new endpoint, entity field, migration, config, etc.), and whether it's staged, unstaged, or a mix.

**If there is nothing staged and nothing modified/untracked, say so and stop.** Never create an empty commit.

## 2. Scan for things that shouldn't be committed

Before staging anything, check the changed/untracked files for:
- `.env` files or anything matching `*.env*`
- Build output that shouldn't be tracked: `target/`, `__pycache__/`, `*.class`, `.idea/` (this repo's `.gitignore` already covers `target/`, `.idea/`, `*.iml`, `.DS_Store` — if any of these show up as untracked in `git status`, that means `.gitignore` isn't catching something; flag it rather than silently committing)
- Anything that looks like a secret or credential: API keys, tokens, private keys (`*.pem`, `*.key`), database passwords hardcoded outside `application.properties`'s existing placeholders, AWS/GCP credential files

If you find anything suspicious, **stop and show the user what you found before staging it.** Let them decide whether to exclude it, add it to `.gitignore`, or (if it's already been committed in a prior commit) flag that it needs history-scrubbing, which is out of scope for this skill.

Stage deliberately — add files by name, not `git add -A` / `git add .`.

## 3. Build and test (hard gate)

Run the Maven build:

```
mvn clean package
```

- **If it fails to compile or build, stop.** Show the user the error output. Do not proceed to branching, committing, or pushing.
- If it succeeds, check whether `src/test` has any test classes (`find src/test -name '*Test.java'` or similar).
  - If tests exist, they already ran as part of `mvn clean package` (via the Surefire plugin) — check the output. **If any test failed, stop and show the failures.**
  - If no test classes exist yet, that's expected early in this project (tests land in Week 3+) — just note "no test coverage yet" in your summary and proceed. This is not a failure.

## 4. Branch off develop

Check the current branch (`git branch --show-current`).

- **If currently on `develop`:** this is the expected starting point. Ask the user for a branch name, or propose one based on the diff summary from step 1 (e.g. `feat/add-class-schedule-endpoint`, `fix/booking-date-validation`). Use `git checkout -b <branch-name>` — uncommitted changes carry over automatically.
- **If currently on `main`:** stop and flag this — this repo's trunk is `develop`, working from `main` directly is unexpected. Ask the user how they want to proceed before touching anything.
- **If already on some other branch** (not `develop`, not `main`): ask the user whether they want to keep committing to that existing branch, or cut a fresh branch off `develop` instead. Don't assume.

Before branching off `develop`, it's worth running `git fetch origin` and checking whether local `develop` is behind `origin/develop`. If it's behind, mention it to the user — don't silently rebase or pull without asking, since that touches trunk.

## 5. Write the commit message

Draft a conventional commit message: `feat:`, `fix:`, `chore:`, or `refactor:` prefix, followed by a concise summary of *what* changed, with a short body if the *why* isn't obvious from the summary alone.

**Show the drafted message to the user and ask them to confirm or edit it before committing.** Don't commit on the first draft without checking in.

Once confirmed, commit via heredoc (not `-m` with embedded newlines typed inline), appending whatever attribution lines the current session's system-reminder specifies for git commits. Always a new commit — never `--amend`.

## 6. Push and open a PR into develop

First check `gh` is available and authenticated:

```
command -v gh
gh auth status
```

If `gh` isn't installed, **stop** and tell the user to install it (`brew install gh` on macOS) and run `gh auth login`, rather than getting partway through the push and failing here. If it's installed but not authenticated, stop and tell them to run `gh auth login`.

Then:
1. Push the branch: `git push -u origin <branch-name>` (first push on a new branch always needs `-u`; never `--force` / `--force-with-lease`).
2. Open the PR against `develop` explicitly (the default base may be `main`):

```
gh pr create --base develop --title "<same summary as the commit message>" --body "$(cat <<'EOF'
## Summary
<1-3 bullets: what changed and why>

## Testing
<what you ran — mvn clean package, existing tests if any, or "no automated tests yet — manual verification: ...">
EOF
)"
```

Append whatever attribution lines the current session's system-reminder specifies for pull request descriptions.

Report back the PR URL when done.

## 7. Only run when explicitly invoked

Don't run any part of this workflow on your own initiative — only when the user explicitly invokes this skill (e.g. "commit and push", "run the commit-and-push skill"). `disable-model-invocation: true` in this file's frontmatter already prevents automatic triggering; this note is a reminder not to run individual steps (like `mvn clean package` or `git commit`) proactively outside this flow either.
