# Creating a Pull Request in parent-pom Repository

## Overview

A branch has been created in the `parent-pom` submodule with all the Spring Boot 3.5.5 upgrade changes. This guide explains how to push that branch and create a PR in the parent-pom repository.

## Branch Details

- **Repository**: https://github.com/hvantran/parent-pom
- **Branch Name**: `copilot/upgrade-spring-boot-3.5.5`
- **Commit Hash**: `4dc0461`
- **Commit Message**: "Upgrade Spring Boot to 3.5.5 with compatible dependencies"

## Changes in the Branch

The branch contains the following changes to `pom.xml`:

### Version Updates
| Dependency | Old Version | New Version |
|------------|-------------|-------------|
| Spring Boot | 3.2.0 | 3.5.5 |
| Spring Cloud | 2021.0.3 | 2023.0.3 |
| Spring Framework BOM | 6.1.7 | 6.1.14 |
| SpringDoc OpenAPI | 2.2.0 | 2.3.0 |
| Spring Kafka Test | 3.1.0 | 3.2.0 |

### Dependency Removals
- Removed explicit `logback-core` dependency
- Removed explicit `logback-classic` dependency
- Spring Boot now manages these versions automatically
- Kept `logstash-logback-encoder` for ELK stack integration

### Configuration
- Java version: 21 (already configured, no change)

## How to Create the PR

### Step 1: Navigate to the submodule

```bash
cd /path/to/project-management/parent-pom
```

### Step 2: Verify the branch exists

```bash
git branch
# Should show: * copilot/upgrade-spring-boot-3.5.5

git log --oneline -1
# Should show: 4dc0461 Upgrade Spring Boot to 3.5.5 with compatible dependencies
```

### Step 3: Push the branch

```bash
# Configure HTTPS if using SSH-based remote
git config url."https://github.com/".insteadOf git@github.com:

# Push the branch (you'll be prompted for GitHub credentials)
git push -u origin copilot/upgrade-spring-boot-3.5.5
```

**Note**: You'll need GitHub authentication. Options:
- Personal Access Token (recommended)
- SSH key
- GitHub CLI (`gh auth login`)

### Step 4: Create the PR

After pushing, you have several options:

#### Option A: GitHub Web UI (Easiest)

1. Go to https://github.com/hvantran/parent-pom
2. You should see a banner: "copilot/upgrade-spring-boot-3.5.5 had recent pushes"
3. Click **"Compare & pull request"**
4. Review the changes
5. Add the following PR description:

```markdown
# Upgrade Spring Boot to 3.5.5

This PR upgrades Spring Boot from version 3.2.0 to 3.5.5, along with compatible dependency updates.

## Changes Made

### Version Updates
- **Spring Boot**: 3.2.0 → 3.5.5
- **Spring Cloud**: 2021.0.3 → 2023.0.3
- **Spring Framework BOM**: 6.1.7 → 6.1.14
- **SpringDoc OpenAPI**: 2.2.0 → 2.3.0
- **Spring Kafka Test**: 3.1.0 → 3.2.0

### Dependency Management
- Removed explicit `logback-core` and `logback-classic` dependencies
- Spring Boot now manages logback versions automatically for better compatibility
- Retained `logstash-logback-encoder` with explicit version for ELK stack integration

### Configuration
- Java version: 21 (already correctly configured)

## Validation

All changes have been validated using Maven:
- ✅ Spring Boot version: 3.5.5
- ✅ Spring Cloud version: 2023.0.3
- ✅ POM validates successfully
- ✅ All dependency versions compatible

## Impact

This upgrade affects all applications that depend on parent-pom:
- action-manager-backend
- spring-kafka-notifier
- ecommerce-stats-app
- template-manager-backend
- endpoint-collector-backend

## Compatibility

Spring Boot 3.5.5 maintains API compatibility with earlier 3.x versions. No application code changes should be required.

## Related PR

This change is part of: hvantran/project-management#[PR_NUMBER]
```

6. Click **"Create pull request"**

#### Option B: GitHub CLI

If you have `gh` CLI installed and authenticated:

```bash
cd parent-pom

gh pr create \
  --repo hvantran/parent-pom \
  --base main \
  --head copilot/upgrade-spring-boot-3.5.5 \
  --title "Upgrade Spring Boot to 3.5.5" \
  --body-file ../parent-pom-pr-description.md
```

#### Option C: Direct URL

Go directly to: https://github.com/hvantran/parent-pom/compare/main...copilot/upgrade-spring-boot-3.5.5

## After PR is Created

1. **Review the PR**: Ensure all changes are correct
2. **Run CI/CD**: Wait for automated tests/builds to complete
3. **Get approval**: Request review if needed
4. **Merge**: Once approved, merge the PR
5. **Update main repository**: Update the project-management repository to reference the merged commit

## Troubleshooting

### Cannot push to repository

**Error**: `Permission denied` or `Authentication required`

**Solution**: 
- Use a Personal Access Token instead of password
- Generate token at: https://github.com/settings/tokens
- Use token as password when prompted
- Or configure SSH keys

### Branch already exists on remote

**Error**: `Branch already exists`

**Solution**:
```bash
# Fetch to see remote branches
git fetch origin

# If branch exists, you can force push (use with caution)
git push -f origin copilot/upgrade-spring-boot-3.5.5
```

### Wrong base branch

If you need to change the base branch:
- Edit the PR on GitHub
- Change the base branch in the PR settings

## Verification After Merge

After the PR is merged in parent-pom, verify in applications:

```bash
# In any application using parent-pom
mvn help:evaluate -Dexpression=springboot.version -q -DforceStdout
# Should output: 3.5.5

mvn help:evaluate -Dexpression=spring-cloud.version -q -DforceStdout  
# Should output: 2023.0.3
```

## Support

For issues with this upgrade:
- Check validation logs in SPRING_BOOT_UPGRADE.md
- Review compatibility notes in UPGRADE_SUMMARY.md
- Consult Spring Boot 3.5.5 release notes
