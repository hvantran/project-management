# Project AI Working Guide

This workspace is a multi-repo monorepo. Prefer linked docs over duplicated guidance:

- Root overview: [README.md](README.md)
- Shared React baseline: [.github/instructions/reactjs.instructions.md](.github/instructions/reactjs.instructions.md)
- Frontend atomic/storybook/tailwind policy: [.github/instructions/frontend-atomic-storybook.instructions.md](.github/instructions/frontend-atomic-storybook.instructions.md)
- Response style (simple and concise): [.agents/skills/simple-and-concise/SKILL.md](.agents/skills/simple-and-concise/SKILL.md)
- Action Manager http-request-monitor pollers (Lazada/Hasaki factory + Mongo GraalJS): [.agents/skills/creating-http-request-monitor-poller/SKILL.md](.agents/skills/creating-http-request-monitor-poller/SKILL.md)
- Fixing SonarQube / SonarCloud issues: [.agents/skills/fixing-sonarqube-issues/SKILL.md](.agents/skills/fixing-sonarqube-issues/SKILL.md)
- Building test Docker images for submodules: [.agents/skills/building-submodule-test-image/SKILL.md](.agents/skills/building-submodule-test-image/SKILL.md)

## Response Style

- Apply [.agents/skills/simple-and-concise/SKILL.md](.agents/skills/simple-and-concise/SKILL.md) to every response.
- Keep replies brief, plain, and answer-first without filler unless the user specifies otherwise.

## Repository Boundaries

- Root repository orchestrates submodules and shared CI.
- Service UIs are developed inside each service folder:
  - `services/action-manager-app/action-manager-ui`
  - `services/external-endpoint-collector/endpoint-collector-ui`
  - `services/template-management-app/template-manager-ui`
  - `services/exam-integrity-app/exam-integrity-ui`

## Submodule PR & Notify-to-Parent Workflow

- When modifying code within submodule repositories (`services/*`, `base-platform`, `parent-pom`, etc.), create branches and open Pull Requests **only in the submodule repository**.
- **Do NOT create Pull Requests for the `project-management` repository** when changing submodules.
- Submodule repositories run an automated notify-to-parent workflow (`send-submodule-updates-to-parent-repo-ci` / `notify-parent-ci.yaml`) on push/merge to `main` that automatically syncs and commits submodule pointer updates directly to `project-management`.

## Frontend Execution Defaults

- Use Yarn for UI apps (`yarn install --frozen-lockfile`, `yarn build`, `yarn test`).
- After any UI change, run `yarn build` in each modified UI app before finishing.
- Resolve Node version in this order:
  1. app-local `.nvmrc` if present
  2. app PR workflow Node setting (`.github/workflows/pr-ci.yaml`)
- Current known versions:
  - `action-manager-ui`: `.nvmrc` -> `20.19.0`
  - `exam-integrity-ui`: `.nvmrc` -> `20`
  - `endpoint-collector-ui`: PR CI -> Node `18`
  - `template-manager-ui`: PR CI -> Node `18`

## Change Scope

- Keep edits minimal and localized to the target service.
- Do not refactor unrelated files while implementing a request.
- When adding new frontend components, follow the atomic + Storybook + Tailwind policy file above.

## UI Page Architecture & Component Encapsulation Standards

- **Zero Raw HTML in Pages**: Service UI pages (e.g., `src/pages/*.tsx`) MUST NOT contain raw HTML elements (`<div>`, `<span>`, `<button>`, `<p>`, `<input>`, etc.). All page-level markup, layout structures, and composite UI sections must be encapsulated in components provided by `@hvantran/ui-component-library` (or app templates/layouts).
- **Organism-Level Domain Components**: When extracting or creating domain-specific or app-prefixed components (such as `ExamIntegrity*` or app workspaces/cards/grids), they **MUST be defined at the `organisms/` layer** (or `templates/`) in `ui-component-library`.
- **No App-Named Components in Molecules or Atoms**:
  - `atoms/` and `molecules/` are strictly domain-agnostic, generic primitives.
  - App-specific or domain-named components (e.g., `ExamIntegrity*`) are forbidden in `src/components/molecules/` or `src/components/atoms/`.
- Every newly introduced organism component must include unit tests and Storybook stories (`*.stories.tsx`) within `base-platform/ui-component-library`.

## Backend Exception Handling Standards

Microservice backends use common advice handlers (`com.hoatv.springboot.common.advices.DefaultExceptionHandler`). When throwing exceptions from controllers, services, or managers, use standard domain exceptions so they map to correct HTTP response status codes:

- **Entity Not Found (404 NOT_FOUND)**: Throw `EntityNotFoundException` (`com.hoatv.fwk.common.exceptions.EntityNotFoundException`).
  - *Constraint*: NEVER throw `IllegalArgumentException` or `ResponseStatusException(HttpStatus.NOT_FOUND)` for missing entities or records.
- **Invalid Input / State Preconditions (400 BAD_REQUEST)**: Throw `InvalidArgumentException` (`com.hoatv.fwk.common.exceptions.InvalidArgumentException`) or rely on Jakarta validation (`@Valid`, `@NotNull`, etc.). Avoid raw `IllegalStateException` or `IllegalArgumentException`.
- **Conflict / Duplicates (409 CONFLICT)**: Throw `DuplicateResourceException` (`com.hoatv.fwk.common.exceptions.DuplicateResourceException`).
- **Internal / Business Errors (500 INTERNAL_SERVER_ERROR)**: Throw `AppException` (`com.hoatv.fwk.common.exceptions.AppException`).
- Avoid throwing `ResponseStatusException` in business/service layers; use standard framework domain exceptions.