---
description: "Frontend Storybook enforcement in CI: required story parity checks + conditional build-storybook gate + yarn build gate"
applyTo: ".github/workflows/**/*.y*ml, services/**/{package.json,.nvmrc}"
---

# Frontend Storybook Enforcement (CI)

Use this instruction when creating or modifying CI for frontend UI apps.

Primary policy reference:

- `.github/instructions/frontend-atomic-storybook.instructions.md`

## Goal

Ensure pull requests that modify UI components cannot pass CI unless:

1. Frontend build is successful (`yarn build`).
2. Storybook parity is validated for component changes.
3. Storybook static build runs when Storybook scripts are available.

## UI App Scope

- `services/action-manager-app/action-manager-ui`
- `services/external-endpoint-collector/endpoint-collector-ui`
- `services/template-management-app/template-manager-ui`
- `services/exam-integrity-app/exam-integrity-ui`

## Node and Package Manager

- Use Yarn for all UI CI steps (`yarn install --frozen-lockfile`).
- Resolve Node version in this order:
  1. app-local `.nvmrc`
  2. app PR workflow Node setting (`.github/workflows/pr-ci.yaml`)

## Required CI Gates

For each changed UI app, enforce this sequence:

1. Install dependencies
   - `yarn install --frozen-lockfile`
2. Build gate (mandatory)
   - `yarn build`
3. Story parity gate (mandatory when component files changed)
   - If `src/**/components/**/*.tsx` changes and file is not `*.stories.tsx`, require corresponding story file in same PR.
4. Storybook static build gate (mandatory when available)
   - If `package.json` has `build-storybook`, run `yarn build-storybook`.
   - If script is missing, fail only when PR includes new/changed component files under `src/components/**`.

## Enforcement Behavior

- Fail fast with clear error messages when story parity is violated.
- Do not skip `yarn build` even if Storybook checks pass.
- Do not silently downgrade Storybook checks to warnings for component changes.
- If Storybook script is absent in a UI app, either:
  1. add Storybook scripts/config as part of the same PR, or
  2. block merge for component additions/changes until Storybook support is added.

## Suggested CI Check Logic

- Detect changed files for each UI app.
- Detect component changes:
  - Include: `src/components/**/*.tsx`
  - Exclude: `src/components/**/*.stories.tsx`
- For each changed component file, verify at least one related `*.stories.tsx` file is added or updated in the same PR.

## PR Requirements

When CI enforces this policy, PR description should include:

1. Which UI app(s) changed.
2. Confirmation that `yarn build` passed per changed app.
3. Confirmation that Storybook parity check passed.
4. Confirmation that `yarn build-storybook` passed where supported.

## Non-Goals

- This instruction does not define visual design style.
- This instruction does not replace app-level testing strategy.
- This instruction complements, but does not replace, frontend atomic/storybook/tailwind policy.