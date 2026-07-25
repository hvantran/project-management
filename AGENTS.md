# Project AI Working Guide

This workspace is a multi-repo monorepo. Prefer linked docs over duplicated guidance:

- Root overview: [README.md](README.md)
- Shared React baseline: [.github/instructions/reactjs.instructions.md](.github/instructions/reactjs.instructions.md)
- Frontend atomic/storybook/tailwind policy: [.github/instructions/frontend-atomic-storybook.instructions.md](.github/instructions/frontend-atomic-storybook.instructions.md)

## Repository Boundaries

- Root repository orchestrates submodules and shared CI.
- Service UIs are developed inside each service folder:
  - `services/action-manager-app/action-manager-ui`
  - `services/external-endpoint-collector/endpoint-collector-ui`
  - `services/template-management-app/template-manager-ui`
  - `services/exam-integrity-app/exam-integrity-ui`

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