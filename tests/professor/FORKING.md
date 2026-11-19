# Forking this repository to test your own project

This repository is the course's automatic test runner. It clones **every**
project in the `leic-a-po26/prj` subgroup, builds each one, runs the
input/output tests and commits the results back here. Regular users cannot
clone other students' projects, so a plain fork will fail at the very first
step.

This document explains how the runner is organised and what to change in your
fork so that it tests **only your project**. Throughout, the example project is
`000` (`leic-a-po26/prj/000`); replace it with your own group number.

## 1. How the runner is organised

### Files

| Path | Purpose |
|------|---------|
| `run.sh` | Entry point. Sets `DELIVERY` and calls the numbered stages in order. |
| `0-begin.sh` | Starts a fresh `GLOBAL-REPORT.md`. |
| `1-clone.sh` | Clones the projects listed in `REPO_RANGE` from `BASE_URL` into `checked-out/`, copies them to `checked-out-ref/` and rewinds the copy to the reference date of the delivery. **This is the script that needs changing.** |
| `2-clean.sh` | Removes extraneous material from a checkout: IDE files, `*.class`, `*.jar`, `Makefile`s, `tests/`, `examples/`, `*.in`, `*.out`, plus every file that is supplied by the course (`po-uilib`, canonical `erp-app` and `erp-core` files). |
| `3-prepare.sh` | Copies `sane/` (the canonical `Makefile`s and course-supplied sources) on top of every checkout. Your own `Makefile`s are **not** used: they are deleted in step 2 and replaced here. |
| `4-test.sh` | Runs `test-group.sh` on every directory in `checked-out/` and commits the per-group logs. |
| `5-summarize.sh` | Parses each group's `.res.html` and appends a one-line verdict to `GLOBAL-REPORT.md`. |
| `6-end.sh` | Closes the report, commits it and **pushes**. |
| `test-group.sh` | Tests one project: checks for a real delivery (diff against `checked-out-ref`), checks the UML files (`e0` only), checks the required files (`files-list-*.txt`), builds `erp-core` and `erp-app`, then runs every `auto-tests/*.in` and diffs against `auto-tests/expected/*.out`. |
| `files-list-core.txt`, `files-list-app.txt`, `files-list-uml.txt` | Files that must exist in the project. |
| `auto-tests/` | Test inputs (`*.in`, optional `*.import`) and `expected/*.out`. |
| `sane/` | Canonical materials copied over every project before building. |
| `logs/<delivery>/` | Per-group results: `NNN.res.html` (summary) and `NNN.log.html` (full build/test log). |
| `GLOBAL-REPORT.md` | Summary of the last run. |
| `.gitlab-ci.yml` | Runs `run.sh` in the course's container image, on a schedule or manual trigger. |

### Work directories (git-ignored)

* `checked-out/NNN` -- the project as it is now (`master`).
* `checked-out-ref/NNN` -- the same project rewound to the reference date of
  the delivery (see `checkout_reference_at_date` in `1-clone.sh`). If the two
  are identical after cleaning, the project is reported as **NO DELIVERY**.

### Flow of one run

```
run.sh
 ├─ 0-begin.sh   e0 TIMESTAMP     -> new GLOBAL-REPORT.md
 ├─ 1-clone.sh   e0               -> checked-out/, checked-out-ref/
 ├─ 2-clean.sh   checked-out-ref  -> strip non-student material
 ├─ 2-clean.sh   checked-out
 ├─ 3-prepare.sh                  -> copy sane/ over both trees
 ├─ 4-test.sh    e0               -> test-group.sh e0 NNN, for each NNN; git commit
 ├─ 5-summarize.sh e0             -> verdict lines in GLOBAL-REPORT.md
 └─ 6-end.sh     TIMESTAMP        -> git commit; git push
```

Every stage after `1-clone.sh` simply iterates over whatever is in
`checked-out/`, so once cloning is restricted to your project nothing else in
the pipeline needs to know about it.

## 2. Changes to make in your fork

### 2.1 `1-clone.sh` -- clone only your project (required)

Replace the `REPO_RANGE` line with your group number:

```sh
REPO_RANGE=( 000 )
```

`BASE_URL` (`git@gitlab.rnl.tecnico.ulisboa.pt:leic-a-po26/prj`) can stay as it
is for local runs, because you already have SSH access to your own project. For
GitLab CI see section 3.

### 2.2 `run.sh` -- pick the delivery

`DELIVERY` selects the reference date in `1-clone.sh`, the checks made by
`test-group.sh` and the wording of the report:

* `e0` -- template/UML delivery: structure, UML PDFs and build are checked; tests are not run.
* `ei` -- intermediate delivery: build + input/output tests.
* `ef` -- final delivery: build + input/output tests.

Set the one you want to check against, e.g. `DELIVERY=ei`.

### 2.3 `6-end.sh` and `4-test.sh` -- committing and pushing results

`4-test.sh` commits the logs and `6-end.sh` commits the report and runs
`git push`. Inside your fork this is harmless (results are pushed to your
fork), but:

* for local runs you may prefer to comment out the `git push` line in
  `6-end.sh` and push by hand;
* in CI, pushing only works if the job has write access to the fork (see 3.2).
  If it does not, comment out `git push` and download the results as job
  artifacts instead (see 3.4).

### 2.4 `0-begin.sh` (optional)

`COURSE_NAME` and the links only affect the report header. No change needed.

## 3. Running in GitLab CI

The provided `.gitlab-ci.yml` assumes the course's SSH key pair and pushes to
the course repository. Adapt it as follows.

### 3.1 Runners and image

The job runs in `registry.opensuse.org/home/d4vid/po26/images/poo:latest`,
which already contains Java, `po-uilib.jar` (in `/usr/share/java`), `make`,
`zsh`, `bc`, etc. Keep the `image:` line. Make sure your fork has a runner
available (*Settings > CI/CD > Runners*: enable shared runners).

### 3.2 Credentials

The `before_script` expects two CI/CD variables of type **File**:
`SSH_PRIVATE_KEY` and `SSH_PUBLIC_KEY`. In your fork:

1. Generate a dedicated key pair (do **not** reuse your personal key):
   `ssh-keygen -t ed25519 -f po26-runner -N ''`
2. Add `po26-runner.pub` as a **deploy key** to:
   * `leic-a-po26/prj/000` (read-only is enough), and
   * your fork of this repository, with **write access** enabled, if you want
     the results pushed back.
   (*Settings > Repository > Deploy keys*.)
3. In your fork, *Settings > CI/CD > Variables*, add:
   * `SSH_PRIVATE_KEY` -- type *File*, contents of `po26-runner`, protected/masked as you see fit;
   * `SSH_PUBLIC_KEY` -- type *File*, contents of `po26-runner.pub`.

### 3.3 `.gitlab-ci.yml` edits

In `test_all_projects.before_script`:

```yaml
    - git remote set-url origin git@gitlab.rnl.tecnico.ulisboa.pt:<your-namespace>/po26.git
    - git config --global user.email "<your e-mail>"
    - git config --global user.name  "<your name>"
```

(`<your-namespace>/po26` is the path of your fork.) Everything else in
`before_script` can stay.

The `workflow.rules` block never runs pipelines on `push`; pipelines are
started from the web UI (*Build > Pipelines > Run pipeline*) or from a
schedule (*Build > Pipeline schedules*). If you would rather run on every
push to the fork, delete the first rule (`$CI_PIPELINE_SOURCE == "push"`).

### 3.4 Alternative: no SSH keys, no push

If you do not want to manage deploy keys, use the job token over HTTPS and
keep the results as artifacts instead of committing them:

1. In `leic-a-po26/prj/000`, *Settings > CI/CD > Job token permissions*, add
   your fork to the allowlist.
2. In `1-clone.sh`:
   ```sh
   BASE_URL="https://gitlab-ci-token:${CI_JOB_TOKEN}@gitlab.rnl.tecnico.ulisboa.pt/leic-a-po26/prj"
   ```
3. In `6-end.sh`, comment out `git push`.
4. In `.gitlab-ci.yml`, remove the SSH lines from `before_script` (keep the
   two `git config` lines so the local commits made by `4-test.sh` and
   `6-end.sh` succeed) and add:
   ```yaml
     artifacts:
       when: always
       paths:
         - logs/
         - GLOBAL-REPORT.md
   ```

## 4. Running locally

You need `zsh`, a JDK, `make`, `bc`, `awk`, `file`, `prlimit` (util-linux)
and `po-uilib.jar` installed as `/usr/share/java/po-uilib.jar` (`JAVADIR` in
`test-group.sh`). The simplest way to get all of that is the course image:

```sh
podman run --rm -it \
  -v "$PWD":/work -w /work \
  -v "$HOME/.ssh":/root/.ssh:ro \
  registry.opensuse.org/home/d4vid/po26/images/poo:latest \
  ./run.sh
```

(`docker` works the same way.) Outside a container, after the changes in
section 2, just run `./run.sh` from the repository root.

Results:

* `logs/<delivery>/000.res.html` -- summary (structure checks, build result, one line per test, total);
* `logs/<delivery>/000.log.html` -- full compiler output and diffs for failed tests;
* `GLOBAL-REPORT.md` -- one-line verdict.

To re-test without re-cloning, run the individual stages by hand:

```sh
./test-group.sh ei 000        # rebuild + test checked-out/000 only
./5-summarize.sh ei           # refresh the verdict line
```

## 5. Things that commonly surprise people

* **Your `Makefile`s are replaced.** `2-clean.sh` deletes them and
  `3-prepare.sh` copies the ones in `sane/`. If your code only builds with your
  own `Makefile`, it will fail here.
* **Course-supplied files are replaced too.** Anything listed in `2-clean.sh`
  (`po-uilib` sources, `erp/app/App.java`, the `Menu`/`Label`/`Message`/`Prompt`
  classes, the core exceptions, ...) is deleted and restored from `sane/`.
  Local modifications to those files are ignored.
* **`tests/`, `examples/`, `*.in`, `*.import`, `*.out` and `*.jar` are deleted**
  from the checkout before building.
* **NO DELIVERY** means that, after cleaning, your `master` is identical to
  its state at the reference date of the delivery (`1-clone.sh`), i.e. nothing
  of yours has been committed since.
* Limits during test execution: 5 s CPU time and 1 MB output per test
  (`ulimit` in `test-group.sh`).
* The runner always tests `master`. Work on other branches is invisible to it.
