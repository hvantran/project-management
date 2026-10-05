# Reusable GitHub Actions Workflows

This directory contains reusable workflow templates that eliminate duplication across service CI/CD pipelines.

## Overview

Instead of duplicating the same build/test/deploy logic across 9+ service workflows, we've created 3 reusable templates that can be called with service-specific parameters.

## Available Templates

### 1. `reusable-backend-service-ci.yaml`

**Purpose**: CI/CD for backend Java/Maven services with testing, SonarCloud analysis, and Docker deployment.

**Includes**:
- Unified `build` job consolidating build, test, SonarCloud, and deploy on a single runner
- Single recursive checkout and JDK setup per pipeline run
- Parent POM and base platform built once and reused across subsequent steps
- JUnit tests with `test-reporter`
- Optional JaCoCo coverage reports and badge generation
- SonarCloud quality gate analysis reusing compiled classes (**runs only on main branch**)
- Conditional Docker image build and push to DockerHub (**runs only on push to main branch**)
- Optional native image build support

**Important**: Sonar analysis only runs on pushes to `main` branch to avoid PR decoration conflicts with submodules.

**Parameters**:
- `service-name`: Display name for the service
- `service-path`: Path to service directory
- `sonar-project-key`: SonarCloud project key
- `docker-image-name`: Docker Hub image name
- `jacoco-enabled`: Enable JaCoCo coverage (default: false)
- `build-native`: Build native image with Spring Boot (default: false)

**Used by**:
- action-manager-backend
- endpoint-collector-backend
- ecommerce-stats-app
- spring-kafka-notifier
- template-manager-backend
- exam-integrity-backend

### 2. `reusable-ui-service-ci.yaml`

**Purpose**: CI/CD for UI services with Maven and Docker deployment.

**Includes**:
- Unified `build` job combining Maven build and conditional Docker deployment on a single runner
- Single recursive checkout and npm registry authentication per pipeline run
- Conditional Docker image build and push to DockerHub (**runs only on push to main branch**)

**Parameters**:
- `service-name`: Display name for the service
- `service-path`: Path to service directory
- `docker-image-name`: Docker Hub image name
- `docker-context`: Docker build context path
- `dockerfile-path`: Path to Dockerfile

**Used by**:
- action-manager-ui
- endpoint-collector-ui
- template-manager-ui
- exam-integrity-ui

### 3. `reusable-base-platform-service-ci.yaml`

**Purpose**: CI/CD for services within the base-platform module.

**Includes**:
- Unified `build` job combining base platform build, service tests, and conditional Docker deployment
- Single recursive checkout per pipeline run
- JUnit tests for specific service with `test-reporter`
- Conditional Docker image build and push to DockerHub (**runs only on push to main branch**)

**Parameters**:
- `service-name`: Display name for the service
- `service-path`: Path to service within base-platform
- `docker-image-name`: Docker Hub image name
- `docker-context`: Docker build context path
- `dockerfile-path`: Path to Dockerfile

**Used by**:
- spring-cloud-gateway-app

## How to Use

### Example: Backend Service

```yaml
name: my-backend-service ci

on:
  push:
    branches: [ "main" ]
    paths:
      - ".github/workflows/actions/action.yaml"
      - ".github/workflows/my-backend-service-ci.yaml"
      - ".github/workflows/reusable-backend-service-ci.yaml"
      - "services/my-service/**"
      - "parent-pom/**"
      - "base-platform/**"

concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true

permissions:
  contents: read
  pull-requests: write
  checks: write

jobs:
  ci:
    uses: ./.github/workflows/reusable-backend-service-ci.yaml
    with:
      service-name: "my-backend-service"
      service-path: "services/my-service"
      sonar-project-key: "hvantran_my-service"
      docker-image-name: "my-backend-service"
      jacoco-enabled: true
      build-native: false
    secrets:
      ACCESS_TOKEN: ${{ secrets.ACCESS_TOKEN }}
      SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
      DOCKERHUB_USERNAME: ${{ secrets.DOCKERHUB_USERNAME }}
      DOCKERHUB_TOKEN: ${{ secrets.DOCKERHUB_TOKEN }}
```

### Example: UI Service

```yaml
name: my-ui-service ci

on:
  push:
    branches: [ "main" ]
    paths:
      - ".github/workflows/actions/action.yaml"
      - ".github/workflows/my-ui-service-ci.yaml"
      - ".github/workflows/reusable-ui-service-ci.yaml"
      - "services/my-ui/**"

concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true

permissions:
  contents: read
  pull-requests: write
  checks: write

jobs:
  ci:
    uses: ./.github/workflows/reusable-ui-service-ci.yaml
    with:
      service-name: "my-ui-service"
      service-path: "services/my-ui"
      docker-image-name: "my-ui-service"
      docker-context: "./services/my-ui"
      dockerfile-path: "./services/my-ui/Dockerfile"
    secrets:
      ACCESS_TOKEN: ${{ secrets.ACCESS_TOKEN }}
      DOCKERHUB_USERNAME: ${{ secrets.DOCKERHUB_USERNAME }}
      DOCKERHUB_TOKEN: ${{ secrets.DOCKERHUB_TOKEN }}
```

## Benefits

1. **Reduced Duplication**: From ~1000 lines of duplicated code to ~300 lines across 9 workflows
2. **Single Source of Truth**: Update CI logic once, applies to all services
3. **Consistency**: All services follow the same build/test/deploy process
4. **Easier Maintenance**: Fix a bug or add a feature in one place
5. **Configuration-Only Workflows**: Service workflows are just ~45 lines of parameters
6. **Type Safety**: workflow_call inputs are strongly typed and validated

## Key Features

- **Concurrency Control**: Cancel outdated workflow runs automatically
- **Explicit Permissions**: Minimal permissions (contents: read, pull-requests: write, checks: write)
- **Timeout Protection**: All jobs have timeout-minutes configured
- **Optimized Checkout**: Uses shallow clones (fetch-depth: 1) except for Sonar
- **Artifact Management**: Uploads build artifacts for debugging
- **Test Reporting**: Automatic test result reporting on failures
- **Docker Versioning**: Tags with both `latest` and `1.0.${{github.run_number}}`

## Environment Variables

All workflows set `CI: false` to avoid strict mode warnings during builds.

## Secrets Required

- `ACCESS_TOKEN`: GitHub token for submodule access
- `SONAR_TOKEN`: SonarCloud authentication token (backend services only)
- `DOCKERHUB_USERNAME`: Docker Hub username
- `DOCKERHUB_TOKEN`: Docker Hub access token

## Job Workflow

Workflows use a single consolidated runner job (`build`) to eliminate redundant checkouts, repeated JDK/Node configurations, and duplicate Maven compilations across pipeline stages.

### Backend Services

All stages run sequentially on a single runner VM:
1. **Checkout & Setup**: Single checkout with submodules (`fetch-depth: 0`) and Java 21 / Maven cache configuration.
2. **Compile Parent & Base Platform**: Builds `parent-pom` and `base-platform` once.
3. **Run Tests**: Tests service with `-P test`, reusing the installed dependencies.
4. **Publish Test Results**: Reports JUnit results via `dorny/test-reporter` and generates JaCoCo artifacts if enabled.
5. **SonarCloud Analysis (main branch only)**: Reuses existing compiled classes and surefire reports; no clean or recompile.
6. **Docker Build & Push (main push only)**: Reuses pre-built artifacts to build and publish Docker image to Docker Hub.

> **Note**: 
> - Sonar is skipped on PRs to prevent PR decoration conflicts when workflows run in parent repositories with submodule services.
> - Deploy is skipped on PRs - Docker images are only built and pushed on main branch.

### UI Services

1. **Checkout & Setup**: Single checkout with submodules (`fetch-depth: 1`), Java 21, and GitHub Packages npm registry authentication.
2. **Build**: Builds `parent-pom`, `base-platform`, and UI service once.
3. **Docker Build & Push (main push only)**: Builds and publishes Docker image to Docker Hub without re-checking out or rebuilding.

## Maintenance

When updating the CI/CD process:

1. **Modify the reusable workflow** (not individual service workflows)
2. **Test with one service** to verify changes
3. **All services automatically inherit the changes** on next run

## Path Filters

Each service workflow includes its reusable template in path filters:
- Ensures workflow runs when the template changes
- Allows testing template changes via any service workflow

## Troubleshooting

**Issue**: Workflow not triggering
- Check path filters include both the service path and reusable workflow path
- Verify the reusable workflow exists and is in the same repository

**Issue**: Job failing in reusable workflow
- Check the workflow run in GitHub Actions UI
- Parameters may need adjustment in the calling workflow
- Secrets must be explicitly passed (not automatically inherited)

**Issue**: Docker build failing
- Verify docker-context and dockerfile-path are correct
- Check that the Dockerfile exists at the specified path
- Ensure Docker Hub credentials are valid

## Migration Guide

To convert an existing service workflow to use a reusable template:

1. **Identify the template**: Backend, UI, or Base Platform?
2. **Extract parameters**: Service name, paths, Docker details
3. **Replace jobs section** with a single `ci` job that calls the template
4. **Add template path to path filters** for proper triggering
5. **Pass all required secrets** explicitly
6. **Test**: Push and verify the workflow runs correctly
