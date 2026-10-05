---
name: fixing-sonarqube-issues
description: >-
  Use when inspecting, analyzing, and fixing SonarQube / SonarCloud issues, code smells,
  bugs, vulnerabilities, security hotspots, or failed quality gates across microservices
  using SonarQube MCP tools.
---

# Fixing SonarQube / SonarCloud Issues

Use this workflow to query, investigate, resolve, and verify SonarQube/SonarCloud issues and security hotspots across this monorepo.

---

## Service to Sonar Project Key Mapping

| Service / Subdirectory | SonarCloud Project Key |
|------------------------|------------------------|
| `services/template-management-app` | `hvantran_template-management-app` |
| `services/action-manager-app` | `hvantran_action-manager-app` |
| `services/external-endpoint-collector` | `hvantran_external-endpoint-collector` |
| `services/ecommerce-stats-app` | `hvantran_ecommerce-stats-app` |
| `services/spring-kafka-notifier` | `hvantran_spring-kafka-notifier` |
| Root / Multi-service | `hvantran_project-management` |

*Note: If unsure, call `search_my_sonarqube_projects` or check `.github/workflows/*-ci.yaml`.*

---

## Workflow Steps

### 1. Identify Scope & Context
- Determine target service and branch or PR context.
- **Branch vs. Pull Request Rules**:
  - Long-lived branches (`main`, `develop`): use `branch`. Discover with `list_branches`.
  - PRs / feature branches: use `pullRequest` with the SonarQube PR key (not git branch name!). Discover with `list_pull_requests`.
  - Omit both to query default (`main`) branch analysis.
  - **Never** supply both `branch` and `pullRequest` in the same call.

### 2. Query Issues or Hotspots
- **To find open issues (bugs, vulnerabilities, code smells):**
  Use `search_sonar_issues_in_projects`:
  ```json
  {
    "projectKeys": "hvantran_template-management-app",
    "types": "BUG,CODE_SMELL,VULNERABILITY",
    "resolved": "false"
  }
  ```
  Filter by file path or severity if the list is long.
- **To find security hotspots:**
  Use `search_security_hotspots`:
  ```json
  {
    "projectKey": "hvantran_template-management-app",
    "status": "TO_REVIEW"
  }
  ```
- **To check Quality Gate status:**
  Use `get_project_quality_gate_status`.

### 3. Inspect the Rule & Root Cause
- For an issue key, check its `rule` field (e.g. `java:S1166`, `javascript:S3776`).
- Fetch the official Sonar rule explanation and recommended fix:
  Use `show_rule`:
  ```json
  {
    "key": "java:S1166"
  }
  ```
- For security hotspots, view context using `show_security_hotspot`.
- Read the affected source file around the reported line number.

### 4. Apply the Fix
- Follow the repository's coding guidelines in [AGENTS.md](../../AGENTS.md):
  - **Keep edits minimal and localized** to the target service.
  - Do not introduce unrelated refactors.
  - Maintain docstrings and comments.
  - For UI changes, follow frontend guidelines and run `yarn build`.
  - For backend changes, follow `java.instructions.md`.
- Prefer proper code refactoring over `@SuppressWarnings` or `// NOSONAR` whenever possible.

### 5. Verify the Fix
- **Backend (Java / Maven):**
  Run local tests or compilation:
  ```bash
  mvn test-compile -f <service-path>
  ```
- **Frontend (UI apps):**
  Run build and tests:
  ```bash
  yarn --cwd <service-ui-path> build
  ```
- **Ad-hoc snippet test:**
  Optionally use `analyze_code_snippet` to verify modified syntax or code fragments.

### 6. Update Sonar Status (When Applicable)
- If handling false positives or accepted risks:
  - Transition issue via `change_sonar_issue_status`:
    `status`: `"RESOLVE"` with `resolution`: `"FALSE-POSITIVE"` or `"WONTFIX"`.
  - Transition hotspot via `change_security_hotspot_status`:
    `status`: `"SAFE"` or `"ACKNOWLEDGED"`.
- For standard code fixes, pushing the committed fix triggers CI analysis and automatically resolves the issue in SonarCloud.

---

## Do / Do Not

| Do | Do Not |
|---|---|
| Use `show_rule` to understand why Sonar flagged the code. | Blindly add `// NOSONAR` or `@SuppressWarnings` without user request. |
| Verify branch vs. PR key before querying Sonar. | Pass git branch names to `pullRequest` parameter. |
| Run local test/build after fixing issues. | Edit unrelated files or apply wide cosmetic reformatting. |
| Check Quality Gate conditions with `get_project_quality_gate_status`. | Assume an issue is resolved without verifying syntax/compilation. |
