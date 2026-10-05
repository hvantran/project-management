---
name: building-submodule-test-image
description: >-
  Use when building and publishing a test Docker image for a submodule feature branch or
  pull request before merging to main, by creating a test branch in project-management,
  updating the submodule commit pointer, and triggering the GitHub Actions CI workflow via gh CLI.
---

# Building Submodule Test Docker Images

Use this workflow to build, publish, and test Docker images for submodule feature branches or pull requests before merging changes into `main`.

---

## Why This Workflow Exists

- **Submodule Isolation**: Individual submodule repositories run pull request checks (lint, unit tests), but do not publish Docker images to Docker Hub.
- **Orchestration in Monorepo**: Docker builds and pushes are managed centrally in `project-management` via reusable GitHub Actions workflows (`reusable-ui-service-ci.yaml`, `reusable-backend-service-ci.yaml`, `reusable-base-platform-service-ci.yaml`).
- **Deploy Guard Condition**: Reusable workflows only execute the `deploy` job when `github.event_name != 'pull_request'` (i.e. on push to `main` or via manual `workflow_dispatch`).
- **Pre-Merge Validation**: By creating a temporary `test/*` branch in `project-management` pointing to the submodule's feature/PR commit, you can trigger `workflow_dispatch` to build and publish a real Docker image without modifying `main`.

---

## Service & Workflow Reference Matrix

| Submodule Path | Service Component | CI Workflow File | Docker Image Name |
|----------------|-------------------|------------------|-------------------|
| `services/template-management-app` | UI | `template-manager-ui-ci.yaml` | `template-manager-ui` |
| `services/template-management-app` | Backend | `template-manager-backend-ci.yaml` | `template-manager-backend` |
| `services/action-manager-app` | UI | `action-manager-ui-ci.yaml` | `action-manager-ui` |
| `services/action-manager-app` | Backend | `action-manager-backend-ci.yaml` | `action-manager-backend` |
| `services/external-endpoint-collector` | UI | `endpoint-collector-ui-ci.yaml` | `endpoint-collector-ui` |
| `services/external-endpoint-collector` | Backend | `endpoint-collector-backend-ci.yaml` | `endpoint-collector-backend` |
| `services/ecommerce-stats-app` | Backend | `ecommerce-stats-app-ci.yaml` | `ecommerce-stats-app` |
| `services/spring-kafka-notifier` | Backend | `spring-kafka-notifier-ci.yaml` | `spring-kafka-notifier` |
| `services/exam-integrity-app` | UI | `exam-integrity-ui-ci.yaml` | `exam-integrity-ui` |
| `services/exam-integrity-app` | Backend | `exam-integrity-backend-ci.yaml` | `exam-integrity-backend` |
| `base-platform` | Gateway | `spring-cloud-gateway-app-ci.yaml` | `spring-cloud-gateway-app` |

---

## Standard Workflow Steps

### 1. Pre-flight Checks (Submodule)

Ensure the target commit inside the submodule is **already pushed to its remote GitHub repository**:

```bash
# Check current commit inside the submodule
git -C <submodule-path> status
git -C <submodule-path> rev-parse --short HEAD

# CRITICAL: Verify the commit is pushed to remote, e.g.:
git -C <submodule-path> push origin <feature-branch>
```

> [!IMPORTANT]
> If the submodule commit is only present locally and not pushed to GitHub, GitHub Actions checkout (`submodules: recursive`) on the runner will fail with `fatal: reference is not a tree`.

---

### 2. Create Test Branch in `project-management`

From the `project-management` root repository:

```bash
# Ensure starting from fresh main
git checkout main
git pull origin main

# Create test branch naming convention: test/<service-name>-pr-<pr_number> or test/<service-name>-<feature>
git checkout -b test/<service-name>-pr-<pr_number>
```

---

### 3. Stage & Commit Updated Submodule Pointer

Stage **only** the submodule directory whose pointer was updated:

```bash
# Stage the submodule commit pointer
git add <submodule-path>

# Commit with a clear test message
git commit -m "test: point <submodule-path> to PR #<pr_number>"
```

> [!WARNING]
> Do not stage unrelated workspace changes or modified files outside the submodule.

---

### 4. Push Test Branch to Remote

```bash
git push origin test/<service-name>-pr-<pr_number>
```

---

### 5. Trigger GitHub Actions CI Workflow

Trigger the corresponding service CI workflow on the test branch using the GitHub CLI:

```bash
gh workflow run <workflow-ci.yaml> \
  --repo hvantran/project-management \
  --ref test/<service-name>-pr-<pr_number>
```

---

### 6. Monitor Execution & Retrieve Docker Image Tag

Watch the workflow run in terminal:

```bash
# List recent runs for the workflow
gh run list --workflow=<workflow-ci.yaml> --repo hvantran/project-management --limit 3

# Watch live logs of the run
gh run watch <run-id> --repo hvantran/project-management
```

#### Image Tagging Output
When the workflow completes successfully, Docker images are pushed to Docker Hub with:
- `${DOCKERHUB_USERNAME}/<docker-image-name>:latest`
- `${DOCKERHUB_USERNAME}/<docker-image-name>:1.0.<run_number>`

> [!TIP]
> For deployments in testing environments (e.g. Kubernetes, Docker Compose, or Helm charts), always use the explicit build tag `1.0.<run_number>` instead of `latest` to avoid Docker image caching collisions.

---

### 7. Post-Testing Teardown

Once testing or verification in the test environment is complete, clean up the temporary test branch:

```bash
# Delete remote test branch
git push origin --delete test/<service-name>-pr-<pr_number>

# Switch back and remove local test branch
git checkout -
git branch -D test/<service-name>-pr-<pr_number>
```

---

## End-to-End Examples

### Example A: Testing `template-manager-ui` (PR #9)

```bash
# 1. Create test branch in project-management
git checkout main && git pull origin main
git checkout -b test/template-manager-ui-pr-9

# 2. Stage updated submodule pointer (pointing to commit 1601dfe)
git add services/template-management-app
git commit -m "test: point template-management-app to PR #9"

# 3. Push test branch to origin
git push origin test/template-manager-ui-pr-9

# 4. Trigger workflow run
gh workflow run template-manager-ui-ci.yaml \
  --repo hvantran/project-management \
  --ref test/template-manager-ui-pr-9

# 5. Monitor run
gh run list --workflow=template-manager-ui-ci.yaml --repo hvantran/project-management --limit 1
```

### Example B: Testing `action-manager-backend` (PR #14)

```bash
# 1. Create test branch in project-management
git checkout main && git pull origin main
git checkout -b test/action-manager-backend-pr-14

# 2. Stage updated submodule pointer
git add services/action-manager-app
git commit -m "test: point action-manager-app to PR #14"

# 3. Push test branch to origin
git push origin test/action-manager-backend-pr-14

# 4. Trigger workflow run
gh workflow run action-manager-backend-ci.yaml \
  --repo hvantran/project-management \
  --ref test/action-manager-backend-pr-14

# 5. Monitor run
gh run list --workflow=action-manager-backend-ci.yaml --repo hvantran/project-management --limit 1
```

---

## Common Pitfalls & Troubleshooting

| Issue | Cause | Fix |
|-------|-------|-----|
| Runner fails at `actions/checkout@v5`: `fatal: reference is not a tree` | Submodule commit exists locally but was never pushed to remote submodule repo | Run `git -C <submodule-path> push origin <branch>` before triggering the workflow |
| `deploy` job skipped in GitHub Actions | Triggered via `pull_request` instead of `workflow_dispatch` | Trigger via `gh workflow run ... --ref test/...` (sets `event_name` to `workflow_dispatch`) |
| Wrong service image built | Workflow filename mismatch (e.g. triggered UI instead of Backend) | Refer to the [Service Reference Matrix](#service--workflow-reference-matrix) |
| Merge conflict or dirty branch | Branch created from outdated or dirty local branch | Always checkout a clean `main` before creating `test/*` branch |

