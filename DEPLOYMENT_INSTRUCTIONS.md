# Spring Boot 3.5.5 Upgrade - Deployment Instructions

## Current Status

✅ **All code changes have been completed and validated**

The Spring Boot upgrade from 3.2.0 to 3.5.5 has been successfully implemented in the `parent-pom/pom.xml` file. All dependency versions have been updated and validated using Maven.

## Submodule Status

The `parent-pom` directory is a Git submodule that points to a separate repository (`git@github.com:hvantran/parent-pom.git`). The changes made to `parent-pom/pom.xml` are currently in the working directory of this submodule but have not been committed to the parent-pom repository.

## Deployment Options

You have two options to deploy these changes:

### Option 1: Apply the Patch File (Recommended)

The `parent-pom-upgrade.patch` file contains all the changes. You can apply it to the parent-pom repository:

```bash
# Navigate to the parent-pom repository (separate clone)
cd /path/to/parent-pom-repository

# Apply the patch
git apply /path/to/project-management/parent-pom-upgrade.patch

# Review the changes
git diff

# Commit and push
git add pom.xml
git commit -m "Upgrade Spring Boot to 3.5.5 with compatible dependencies"
git push origin main  # or your target branch
```

Then update the project-management repository to reference the new commit:

```bash
cd /path/to/project-management

# Update submodule reference
cd parent-pom
git pull origin main  # or your target branch
cd ..

# Commit the submodule update
git add parent-pom
git commit -m "Update parent-pom submodule to Spring Boot 3.5.5"
git push
```

### Option 2: Commit Changes Directly in Submodule

If you have direct access to commit to the parent-pom submodule:

```bash
cd parent-pom

# Create a branch
git checkout -b upgrade/spring-boot-3.5.5

# Add and commit changes
git add pom.xml
git commit -m "Upgrade Spring Boot to 3.5.5 with compatible dependencies

- Update Spring Boot from 3.2.0 to 3.5.5
- Update Spring Cloud from 2021.0.3 to 2023.0.3
- Update Spring Framework BOM from 6.1.7 to 6.1.14
- Update SpringDoc OpenAPI from 2.2.0 to 2.3.0
- Update Spring Kafka Test from 3.1.0 to 3.2.0
- Remove explicit logback dependencies
- Let Spring Boot manage logback versions"

# Push to parent-pom repository
git push origin upgrade/spring-boot-3.5.5

# Create PR or merge as needed
```

Then update the main repository:

```bash
cd ..
git add parent-pom
git commit -m "Update parent-pom submodule to Spring Boot 3.5.5"
git push
```

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
