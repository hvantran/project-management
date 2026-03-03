---
description: 'Maven build instructions and best practices for this project'
applyTo: '**/pom.xml, **/*.java, **/*.kt'
---

# Maven Build Instructions

## Local Development Build

### Required Build Flag

**IMPORTANT**: When building Maven projects locally, always use the `-Ddockerfile.skip` flag to skip Docker image creation:

```bash
mvn clean install -Ddockerfile.skip
```

or

```bash
mvn clean package -Ddockerfile.skip
```

### Rationale

- **Performance**: Building Docker images locally is time-consuming and resource-intensive
- **Development Efficiency**: Local builds focus on compilation and testing, not containerization
- **CI/CD Separation**: Docker image creation should be handled by the CI/CD pipeline
- **Consistency**: Ensures all developers follow the same local build process

### Common Build Commands

#### Full Build with Tests
```bash
mvn clean install -Ddockerfile.skip
```

#### Build Without Tests
```bash
mvn clean install -DskipTests -Ddockerfile.skip
```

#### Package Only
```bash
mvn clean package -Ddockerfile.skip
```

#### Verify with Integration Tests
```bash
mvn clean verify -Ddockerfile.skip
```

## Multi-Module Projects

For multi-module Maven projects (like this repository), build from the root directory:

```bash
cd /home/hoatranv/sources/project-management
mvn clean install -Ddockerfile.skip
```

Or build specific modules:

```bash
cd /home/hoatranv/sources/project-management/base-platform
mvn clean install -Ddockerfile.skip
```

## Maven Wrapper

If the project uses Maven Wrapper (`mvnw`), use it instead of the global Maven installation:

```bash
./mvnw clean install -Ddockerfile.skip
```

## Additional Best Practices

### Dependency Management

- Always check for dependency updates regularly
- Use the parent POM for centralized dependency version management
- Avoid SNAPSHOT dependencies in production releases

### Build Performance

- Use `-T` flag for parallel builds: `mvn clean install -T 1C -Ddockerfile.skip`
- Consider using the Maven Daemon (mvnd) for faster builds
- Use incremental builds when possible: `mvn compile -Ddockerfile.skip`

### Profile Management

- Use Maven profiles for environment-specific configurations
- Activate profiles with `-P` flag: `mvn clean install -Pdev -Ddockerfile.skip`
- Document all available profiles in the project README

### Troubleshooting

#### Dependency Resolution Issues
```bash
mvn dependency:purge-local-repository -Ddockerfile.skip
mvn clean install -U -Ddockerfile.skip
```

#### Clear Local Repository Cache
```bash
rm -rf ~/.m2/repository
mvn clean install -Ddockerfile.skip
```

#### Analyze Dependencies
```bash
mvn dependency:tree -Ddockerfile.skip
mvn dependency:analyze -Ddockerfile.skip
```

## CI/CD Builds

In CI/CD pipelines (Jenkins, GitHub Actions, etc.), the `-Ddockerfile.skip` flag should **NOT** be used, as Docker image creation is part of the deployment process.

### Jenkins Pipeline Example
```groovy
stage('Build') {
    steps {
        sh 'mvn clean install'  // No -Ddockerfile.skip
    }
}
```

### GitHub Actions Example
```yaml
- name: Build with Maven
  run: mvn clean install  # No -Ddockerfile.skip
```

## Summary

**Remember**: Always use `-Ddockerfile.skip` for local development builds to optimize performance and maintain consistency across the development team.
