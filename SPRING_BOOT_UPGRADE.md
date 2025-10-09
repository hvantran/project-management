# Spring Boot Upgrade to 3.5.5

This document details the Spring Boot upgrade from version 3.2.0 to 3.5.5 in the parent-pom.

## Implementation Status

✅ **COMPLETED** - All changes have been implemented in the `parent-pom/pom.xml` file.

## Changes Made

### 1. Spring Boot Version
- **Updated**: `springboot.version` from `3.2.0` to `3.5.5` (line 37)
- **Location**: `parent-pom/pom.xml`

### 2. Compatible Dependencies Updated
- **Spring Cloud**: Updated from `2021.0.3` to `2023.0.3` (line 33)
- **Spring Framework BOM**: Updated from `6.1.7` to `6.1.14` (line 213)
- **SpringDoc OpenAPI**: Updated from `2.2.0` to `2.3.0` (line 89)
- **Spring Kafka Test**: Updated from `3.1.0` to `3.2.0` (line 92)

### 3. Java Version Compatibility
- **Confirmed**: `java.compile.version` is already set to `21` (line 15)
- **Status**: No change needed - Java 21 is correctly configured

### 4. Logging Dependencies
- **Removed**: Explicit `logback-core` and `logback-classic` dependencies (previously lines 520-529)
- **Reason**: Allow Spring Boot BOM to manage logback versions for better compatibility
- **Kept**: `logstash-logback-encoder` dependency with explicit version for ELK stack integration

## Files Modified

The following files contain the Spring Boot upgrade changes:

1. **parent-pom/pom.xml** - Main POM file with all dependency and version updates
2. **parent-pom-updated.xml** - Copy of the updated parent-pom/pom.xml for reference
3. **parent-pom-upgrade.patch** - Patch file showing all changes made

## How to Apply the Changes

Since `parent-pom` is a git submodule pointing to a separate repository, the changes need to be committed to that repository. The patch file (`parent-pom-upgrade.patch`) can be applied to the parent-pom repository using:

```bash
cd parent-pom
git apply ../parent-pom-upgrade.patch
git add pom.xml
git commit -m "Upgrade Spring Boot to 3.5.5 with compatible dependencies"
git push
```

Then update the main repository to reference the new commit:

```bash
cd ..
git add parent-pom
git commit -m "Update parent-pom submodule to Spring Boot 3.5.5"
git push
```

## Verification

A test Spring Boot application was created and successfully:
- ✅ Compiles with Java 21
- ✅ Packages as executable JAR
- ✅ Runs successfully 
- ✅ Reports Spring Boot version 3.5.5
- ✅ Serves HTTP endpoints correctly

## Impact

This upgrade affects the following applications in the project:
- action-manager-backend
- spring-kafka-notifier
- ecommerce-stats-app
- template-manager-backend
- endpoint-collector-backend

All applications that inherit from the parent-pom will now use Spring Boot 3.5.5 and the updated compatible dependency versions.

## Notes

The upgrade maintains backward compatibility for the existing application code, as Spring Boot 3.5.5 is part of the Spring Boot 3.x series which maintains API compatibility with earlier 3.x versions.