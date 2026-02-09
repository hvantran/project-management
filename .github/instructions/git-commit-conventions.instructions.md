---
description: 'Git commit message conventions for this project - use repository name in square brackets as prefix'
applyTo: '**'
---

# Git Commit Message Conventions

## Commit Message Format

**Required Format:**
```
[<repository-name>] <subject>

<optional body>

<optional footer>
```

## Prefix Rules

**DO NOT USE** conventional commit prefixes like:
- ❌ `feat:`
- ❌ `fix:`
- ❌ `chore:`
- ❌ `docs:`
- ❌ `refactor:`
- ❌ `test:`
- ❌ `style:`
- ❌ `perf:`
- ❌ `ci:`
- ❌ `build:`

**ALWAYS USE** the repository name in square brackets:

### Repository Name Prefixes

| Repository | Prefix |
|------------|--------|
| parent-pom | `[parent-pom]` |
| action-manager-app | `[action-manager]` |
| ecommerce-stats-app | `[ecommerce-stats]` |
| external-endpoint-collector | `[endpoint-collector]` |
| spring-kafka-notifier | `[kafka-notifier]` |
| template-management-app | `[template-manager]` |
| project-management | `[project]` |
| base-platform | `[base-platform]` |
| account-platform | `[account-platform]` |
| deployment | `[deployment]` |

## Examples

### ✅ CORRECT Examples

```
[parent-pom] Add spring-boot-devtools to dependency management

Add spring-boot-devtools dependency to parent POM dependency management
with runtime scope and optional flag to enable auto-reload capability 
for development profile.

Related to hvantran/project-management#183
```

```
[action-manager] Enable DevTools auto-reload for dev profile

Configure Spring Boot DevTools in application-dev.properties to support
automatic restart and LiveReload functionality during development.

Related to hvantran/project-management#183
```

```
[endpoint-collector] Update database schema for user analytics

Add new analytics_events table and update user model to track
interaction patterns for better insights.

Resolves #42
```

```
[project] Update submodule references for DevTools implementation

Update all service submodules to include Spring Boot DevTools configuration
for improved development experience with automatic restart.

Resolves #183
```

### ❌ INCORRECT Examples

```
feat: Add spring-boot-devtools to dependency management
```

```
fix: Enable DevTools auto-reload
```

```
chore(deps): Update database schema
```

```
parent-pom: Add spring-boot-devtools  (wrong format, should use brackets)
```

## Subject Line Guidelines

1. **Keep it concise**: Max 72 characters (including prefix)
2. **Use imperative mood**: "Add feature" not "Added feature" or "Adds feature"
3. **Don't end with period**: No trailing punctuation
4. **Be descriptive**: Clearly state what the commit does
5. **Start with capital letter**: After the prefix

## Body Guidelines

1. **Wrap at 72 characters**: For readability
2. **Explain what and why**: Not how (code shows how)
3. **Separate from subject**: With a blank line
4. **Use bullet points**: For multiple changes
5. **Reference context**: Link to issues/PRs when relevant

## Footer Guidelines

### Issue References

- `Resolves #<issue>` - Closes the issue when merged
- `Fixes #<issue>` - Also closes the issue
- `Related to #<issue>` - References without closing
- `Part of #<issue>` - For partial implementation
- Cross-repo: `Related to hvantran/project-management#183`

### Breaking Changes

For breaking changes, add a footer:
```
BREAKING CHANGE: <description of what breaks>
```

### Co-authors

For pair programming or contributions:
```
Co-authored-by: Name <email@example.com>
```

## Multi-Repository Commits

When committing to **project-management** (the main repo with submodules):

```
[project] Update submodule references for DevTools implementation

Update all service submodules to include Spring Boot DevTools configuration.

Submodules updated:
- parent-pom: Add devtools dependency management
- action-manager: Configure auto-reload
- ecommerce-stats: Configure auto-reload
- endpoint-collector: Configure auto-reload
- kafka-notifier: Configure auto-reload
- template-manager: Configure auto-reload

Resolves #183
```

## Common Patterns

### Adding Features
```
[<repo>] Add <feature-name>
```

### Fixing Bugs
```
[<repo>] Resolve <issue-description>
```

### Updating Dependencies
```
[<repo>] Update <dependency-name> to version <x.y.z>
```

### Configuration Changes
```
[<repo>] Configure <component> for <purpose>
```

### Refactoring
```
[<repo>] Refactor <component> to improve <aspect>
```

### Documentation
```
[<repo>] Document <feature/component>
```

### Tests
```
[<repo>] Add tests for <feature/component>
```

## Automation & Tools

When using GitHub Copilot, `gh` CLI, or other tools to create commits, always ensure the 
repository name is wrapped in square brackets at the start of the commit message.

## Examples by Repository

### parent-pom
```
[parent-pom] Add spring-boot-devtools to dependency management
[parent-pom] Update Spring Boot version to 3.2.1
[parent-pom] Configure Maven compiler plugin for Java 21
```

### action-manager-app
```
[action-manager] Add Kafka consumer status indicator
[action-manager] Implement job execution retry mechanism
[action-manager] Update MongoDB connection configuration
```

### ecommerce-stats-app
```
[ecommerce-stats] Add sales analytics dashboard
[ecommerce-stats] Optimize database query performance
[ecommerce-stats] Configure JPA auditing for entities
```

### external-endpoint-collector
```
[endpoint-collector] Add health check endpoints
[endpoint-collector] Implement rate limiting for API calls
[endpoint-collector] Update PostgreSQL schema migrations
```

### spring-kafka-notifier
```
[kafka-notifier] Configure Kafka producer settings
[kafka-notifier] Add message retry logic
[kafka-notifier] Update notification templates
```

### template-management-app
```
[template-manager] Add FreeMarker template support
[template-manager] Implement template versioning
[template-manager] Configure MongoDB indexes
```

### project-management
```
[project] Update all submodules to latest versions
[project] Add git commit conventions instruction
[project] Configure CI/CD pipeline
```

## IDE Integration

Configure your IDE/editor commit message templates to use repository prefixes:

**Example Template:**
```
[repository-name] 

Related to hvantran/project-management#
```

## Important Notes

1. **Always use square brackets**: `[repo-name]` not `repo-name:`
2. **Repository name comes first**: Before any description
3. **Space after bracket**: `[repo] Message` not `[repo]Message`
4. **Consistent naming**: Use the shortened names from the table above
5. **GitHub Copilot will follow this**: Once this instruction is in place
