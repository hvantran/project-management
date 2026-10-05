---
description: "Frontend UI policy: atomic design + Storybook coverage + Tailwind-first components + yarn build verification"
applyTo: "services/**/{action-manager-ui,endpoint-collector-ui,template-manager-ui,exam-integrity-ui}/src/**/*.{js,jsx,ts,tsx,css,scss}"
---

# Frontend Atomic + Storybook + Tailwind Policy

Use this instruction for all web UI component work under service UI apps.

## Required Architecture

- Organize and compose components with atomic design layers when applicable:
  - `atoms`
  - `molecules`
  - `organisms`
  - `templates`
- For composition patterns, use `services/exam-integrity-app/exam-integrity-ui/src/components` as reference.

## Storybook Coverage Rule

- Every new UI component must include a Storybook story in the same feature folder.
- When changing a component API or behavior, update the corresponding `*.stories.tsx` file in the same change.
- Story title should match layer naming (`Atoms/...`, `Molecules/...`, `Organisms/...`, `Templates/...`) consistently with the target app conventions.
- If a component cannot reasonably be visualized in Storybook, document the reason in the PR description.

## Tailwind-First Rule

- Prefer Tailwind utility classes for styling and layout.
- Prefer semantic HTML primitives + Tailwind for new UI elements before introducing or extending base component libraries.
- Reuse existing app design tokens and Tailwind configuration where available.
- Do not introduce ad-hoc inline styles when a Tailwind class or tokenized utility can express the same intent.

## MUI Avoidance Rule

- Do not introduce new `@mui/material` components for atoms/molecules when equivalent semantic HTML + Tailwind is feasible.
- Do not use MUI `sx` props in newly added or refactored UI components.
- For reusable atoms (for example: `Button`, `Chip`, `Badge`), implementation must be framework-agnostic React + Tailwind and must not wrap MUI primitives.
- If legacy MUI components are touched during feature work, prefer incremental migration to app atoms rather than adding more MUI usage.

## Build Validation Rule (Mandatory)

- After frontend changes, run `yarn build` in each modified UI app and resolve build failures before finishing.
- Use `yarn install --frozen-lockfile` before build when dependencies are not installed.

## Node Version Resolution

Resolve Node version for each UI app in this priority order:

1. Use app-local `.nvmrc` when present.
2. Otherwise use Node version configured in that app's `.github/workflows/pr-ci.yaml`.

Current references:

- `services/action-manager-app/action-manager-ui/.nvmrc` -> `20.19.0`
- `services/exam-integrity-app/exam-integrity-ui/.nvmrc` -> `20`
- `services/external-endpoint-collector/.github/workflows/pr-ci.yaml` -> `18`
- `services/template-management-app/.github/workflows/pr-ci.yaml` -> `18`

## Commands Reference

Run from each UI app directory:

```bash
yarn install --frozen-lockfile
yarn build
```

For exam-integrity UI component documentation:

```bash
yarn storybook
yarn build-storybook
```