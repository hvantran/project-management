# Spring Boot 3.5.5 Upgrade - Deployment Instructions

## Current Status

✅ **All code changes have been completed and validated**
✅ **Branch created in parent-pom submodule: `copilot/upgrade-spring-boot-3.5.5`**

The Spring Boot upgrade from 3.2.0 to 3.5.5 has been successfully implemented in the `parent-pom/pom.xml` file. All dependency versions have been updated and validated using Maven.

## Submodule Status

The `parent-pom` directory is a Git submodule that points to a separate repository (`https://github.com/hvantran/parent-pom.git`). 

### Branch Details
- **Branch name**: `copilot/upgrade-spring-boot-3.5.5`
- **Commit**: `4dc0461` - "Upgrade Spring Boot to 3.5.5 with compatible dependencies"
- **Status**: Branch created locally, needs to be pushed to create a PR

## Creating the Pull Request

To create a PR in the parent-pom repository, you need to:

1. Push the branch from the submodule (requires authentication)
2. Create a PR on GitHub for the parent-pom repository

### Option 1: Push from Local Submodule (Recommended)

Navigate to the parent-pom submodule and push the branch:

```bash
cd parent-pom

# Configure git if needed (using HTTPS)
git config url."https://github.com/".insteadOf git@github.com:

# Push the branch (will require GitHub authentication)
git push -u origin copilot/upgrade-spring-boot-3.5.5
```

Then create a PR on GitHub:
- Go to https://github.com/hvantran/parent-pom
- You should see a prompt to create a PR for the new branch
- Click "Compare & pull request"
- Review and create the PR

### Option 2: Use GitHub CLI

If you have GitHub CLI (`gh`) installed and authenticated:

```bash
cd parent-pom

# Create PR using GitHub CLI
gh pr create --repo hvantran/parent-pom \
  --base main \
  --head copilot/upgrade-spring-boot-3.5.5 \
  --title "Upgrade Spring Boot to 3.5.5" \
  --body "This PR upgrades Spring Boot from 3.2.0 to 3.5.5 along with compatible dependency updates.

## Changes Made
- Update Spring Boot from 3.2.0 to 3.5.5
- Update Spring Cloud from 2021.0.3 to 2023.0.3
- Update Spring Framework BOM from 6.1.7 to 6.1.14
- Update SpringDoc OpenAPI from 2.2.0 to 2.3.0
- Update Spring Kafka Test from 3.1.0 to 3.2.0
- Remove explicit logback-core and logback-classic dependencies
- Let Spring Boot manage logback versions automatically

## Validation
All changes have been validated with Maven. Java version is already set to 21."
```

### Option 3: Manual PR Creation via Web Interface

1. First, push the branch (you'll need to authenticate):
   ```bash
   cd parent-pom
   git push -u origin copilot/upgrade-spring-boot-3.5.5
   ```

2. Go to https://github.com/hvantran/parent-pom/pulls
3. Click "New pull request"
4. Select base: `main` and compare: `copilot/upgrade-spring-boot-3.5.5`
5. Create the PR with appropriate title and description

## After PR is Merged

Once the parent-pom PR is merged, update the project-management repository:


```bash
cd /path/to/project-management

# Update submodule to point to merged changes
cd parent-pom
git checkout main
git pull origin main
cd ..

# Commit the submodule update
git add parent-pom
git commit -m "Update parent-pom submodule to Spring Boot 3.5.5"
git push
```

## Branch Information

The branch `copilot/upgrade-spring-boot-3.5.5` in the parent-pom submodule contains:

**Commit**: `4dc0461`
**Message**: "Upgrade Spring Boot to 3.5.5 with compatible dependencies"

**Files Changed**: 
- `pom.xml` (6 insertions, 15 deletions)

**Changes Summary**:
- Spring Boot: 3.2.0 → 3.5.5
- Spring Cloud: 2021.0.3 → 2023.0.3  
- Spring Framework BOM: 6.1.7 → 6.1.14
- SpringDoc OpenAPI: 2.2.0 → 2.3.0
- Spring Kafka Test: 3.1.0 → 3.2.0
- Removed explicit logback dependencies
- Java version: already 21 ✅

## Alternative: Using the Patch File

If you prefer not to push the branch, you can still use the patch file approach. The `parent-pom-upgrade.patch` file is available in this repository and can be applied to a fresh clone of the parent-pom repository.

## Reference Files

- **parent-pom-updated.xml**: Complete updated POM file for reference
- **parent-pom-upgrade.patch**: Patch file with all changes
- **SPRING_BOOT_UPGRADE.md**: Detailed documentation of changes
- **UPGRADE_SUMMARY.md**: Quick reference summary

## Validation

After deployment, verify the versions in your applications:

```bash
# In any application that uses parent-pom
mvn help:evaluate -Dexpression=springboot.version -q -DforceStdout
# Should output: 3.5.5

mvn help:evaluate -Dexpression=spring-cloud.version -q -DforceStdout
# Should output: 2023.0.3
```

## Testing

After deployment, test your applications:

1. Build all applications: `mvn clean install`
2. Run application tests: `mvn test`
3. Start applications and verify functionality
4. Check logs for any deprecation warnings

## Rollback Plan

If issues arise, you can rollback by:

1. Reverting the parent-pom commit
2. Updating the submodule reference in project-management back to the previous commit
3. Rebuilding and redeploying applications

## Support

For questions or issues:
- Review SPRING_BOOT_UPGRADE.md for detailed change information
- Check UPGRADE_SUMMARY.md for version compatibility
- Consult Spring Boot 3.5.5 release notes: https://github.com/spring-projects/spring-boot/releases/tag/v3.5.5
