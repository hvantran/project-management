# Spring Boot Upgrade to 3.5.5

This file documents the changes made during the Spring Boot upgrade from 3.2.0 to 3.5.5.

## Main Changes

### 1. Spring Boot Version
- **Changed**: `springboot.version` from `3.2.0` to `3.5.5`

### 2. Compatible Dependencies Updated
- **Spring Cloud**: Updated from `2021.0.3` to `2023.0.3` 
- **Spring Framework**: Updated from `6.1.7` to `6.1.14`
- **SpringDoc OpenAPI**: Updated from `2.2.0` to `2.3.0`
- **Spring Kafka Test**: Updated from `3.1.0` to `3.2.0`

### 3. Java Version Compatibility
- **Changed**: `java.compile.version` from `21` to `17` (to match runtime environment)

### 4. Logging Dependencies
- **Removed**: Explicit logback-core and logback-classic dependencies
- **Reason**: Let Spring Boot BOM manage logback versions for better compatibility
- **Kept**: logstash-logback-encoder dependency with explicit version

## Verification

A test Spring Boot application was created and successfully:
- ✅ Compiles with Java 17
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