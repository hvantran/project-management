# Multi-Repository Workflow - Quick Reference

## 📁 Repository Structure

```
project-management/              ← Parent (Issues, Sprint Board, Backlog)
├── services/
│   ├── action-manager-app/      ← Child (Code, Branches, PRs)
│   ├── ecommerce-stats-app/     ← Child (Code, Branches, PRs)
│   ├── external-endpoint-collector/
│   ├── spring-kafka-notifier/
│   └── template-management-app/
├── base-platform/
├── account-platform/
├── parent-pom/
└── deployment-new/
```

---

## 🎯 The Golden Rules

### Rule #1: Issues → Parent Repo ONLY
```bash
# ✅ Create issues in parent repo with [repo-name] prefix
gh issue create \
  --repo hvantran/project-management \
  --title "[action-manager] Add search functionality"

# ❌ DON'T create issues in child repos
```

### Rule #2: Branches → Child Repo ONLY
```bash
# ✅ Create branches in child repo
cd services/action-manager-app
git checkout -b feature/action-manager-130-search-api

# ❌ DON'T create branches in parent repo
```

### Rule #3: Pull Requests → Child Repo ONLY
```bash
# ✅ Create PRs in child repo
cd services/action-manager-app
gh pr create \
  --title "Add search API" \
  --body "Closes hvantran/project-management#130"

# ❌ DON'T create PRs in parent repo
```

### Rule #4: Link Everything
```bash
# Commits reference parent issue
[action-manager] Implement feature
Related to hvantran/project-management#130

# PRs close parent issue
Closes hvantran/project-management#130
```

---

## 📝 Naming Conventions

### Issue Titles (Parent Repo)
```
Format: [repository-name] Issue description

Examples:
[action-manager] Add search to header
[ecommerce-stats] Implement analytics dashboard
[endpoint-collector] Add health check endpoint
[kafka-notifier] Configure retry mechanism
[template-manager] Add email templates
[parent-pom] Update Spring Boot to 3.2.1
[base-platform] Add logging framework
[deployment] Configure Kubernetes ingress
```

### Branch Names (Child Repos)
```
Format: type/repo-name-issue-description

Examples:
feature/action-manager-130-search-api
feature/ecommerce-stats-145-analytics-dashboard
bugfix/endpoint-collector-201-timeout-error
hotfix/kafka-notifier-305-message-loss
```

### Commit Messages (Child Repos)
```
Format: [repository-name] Subject line

Body with details

Related to hvantran/project-management#<issue>
Closes hvantran/project-management#<issue>

Examples:
[action-manager] Implement search API endpoint
[ecommerce-stats] Add sales chart component
[parent-pom] Bump Spring Boot version
```

---

## 🔄 Complete Workflow Example

### Phase 1: Sprint Planning (Parent Repo)

```bash
# PO: Create sprint and user stories
cd /path/to/project-management

gh api repos/hvantran/project-management/milestones \
  -f title="Sprint 6" \
  -f due_on="2026-04-21T23:59:59Z"

gh issue create \
  --repo hvantran/project-management \
  --title "[action-manager] As a user, I want to search actions" \
  --label "user-story,action-manager" \
  --milestone "Sprint 6"

# Issue #130 created

# PM: Break into tasks
gh issue create \
  --repo hvantran/project-management \
  --title "[action-manager] Implement search API endpoint" \
  --label "task,backend,action-manager" \
  --milestone "Sprint 6" \
  --assignee developer1

# Task #131 created
```

### Phase 2: Development (Child Repo)

```bash
# Developer starts work
cd services/action-manager-app

# Create feature branch
git checkout -b feature/action-manager-131-search-api

# TDD: Write failing test
# ... write test code ...
git add src/test/
git commit -m "[action-manager] Add search API test (RED)

Write failing test for search endpoint.

Related to hvantran/project-management#131"

# TDD: Implement feature
# ... write implementation ...
git add src/main/
git commit -m "[action-manager] Implement search API (GREEN)

Implement search endpoint with pagination.
All tests passing, 85% coverage.

Related to hvantran/project-management#131"

# Push to child repo
git push origin feature/action-manager-131-search-api
```

### Phase 3: Pull Request (Child Repo)

```bash
# Still in child repo
cd services/action-manager-app

gh pr create \
  --title "Implement search API endpoint" \
  --body "Closes hvantran/project-management#131

## Summary
Implemented search API with pagination and filtering.

## Changes
- Added SearchController with GET /api/actions/search
- Implemented SearchService with business logic
- Added search repository methods
- 85% test coverage

## Testing
- Unit tests: 15/15 passing
- Integration tests: 5/5 passing

## References
- Parent task: hvantran/project-management#131
- User story: hvantran/project-management#130" \
  --reviewer tech-lead,tester

# PR #45 created in action-manager-app
```

### Phase 4: Review & Merge (Child Repo)

```bash
# Tester reviews PR in child repo
cd services/action-manager-app
gh pr review 45 --approve --body "QA approved, all tests passing"

# Developer merges
gh pr merge 45 --squash --delete-branch
```

### Phase 5: Update Parent (Parent Repo)

```bash
# Developer updates parent repo issue
gh issue comment 131 \
  --repo hvantran/project-management \
  --body "✅ Implementation complete and merged

**PR:** hvantran/action-manager-app#45
**Coverage:** 85%

Ready for deployment."

gh issue close 131 \
  --repo hvantran/project-management
```

---

## 🚀 Quick Commands by Role

### Product Owner (Parent Repo)
```bash
# Create sprint
gh api repos/hvantran/project-management/milestones \
  -f title="Sprint X"

# Create user story
gh issue create \
  --repo hvantran/project-management \
  --title "[repo-name] User story" \
  --label user-story

# View backlog
gh issue list \
  --repo hvantran/project-management \
  --label user-story \
  --state open
```

### Project Manager (Parent Repo)
```bash
# Create sprint board
gh project create --owner hvantran --title "Sprint X Board"

# Create task
gh issue create \
  --repo hvantran/project-management \
  --title "[repo-name] Task description" \
  --label task \
  --milestone "Sprint X"

# View sprint progress
gh project view PROJECT_NUMBER --owner hvantran
```

### Developer (Child Repo)
```bash
# List your tasks (from parent)
gh issue list \
  --repo hvantran/project-management \
  --assignee @me \
  --state open

# Navigate to child repo
cd services/{repo-name}

# Create feature branch
git checkout -b feature/{repo}-{issue}-{description}

# Commit with reference
git commit -m "[repo-name] Message

Related to hvantran/project-management#{issue}"

# Create PR
gh pr create \
  --title "Feature title" \
  --body "Closes hvantran/project-management#{issue}"

# After merge, update parent
gh issue close {issue} \
  --repo hvantran/project-management \
  --comment "Merged in PR #{pr_number}"
```

### Tester (Child Repo)
```bash
# Review PR in child repo
cd services/{repo-name}
gh pr view {pr_number}
gh pr review {pr_number} --approve

# Report bug in parent repo
gh issue create \
  --repo hvantran/project-management \
  --title "[repo-name] Bug description" \
  --label bug
```

### DevOps (Child Repo)
```bash
# Deploy from child repo
cd services/{repo-name}
gh release create v1.X.0
gh run view --log

# Update deployment status in parent
gh issue comment {issue} \
  --repo hvantran/project-management \
  --body "Deployed v1.X.0 to production"
```

---

## 📊 Repository Prefix Mapping

| Prefix | Repository | Service |
|--------|-----------|---------|
| `[action-manager]` | action-manager-app | Action execution service |
| `[ecommerce-stats]` | ecommerce-stats-app | Analytics service |
| `[endpoint-collector]` | external-endpoint-collector | API gateway |
| `[kafka-notifier]` | spring-kafka-notifier | Notification service |
| `[template-manager]` | template-management-app | Template engine |
| `[parent-pom]` | parent-pom | Maven parent POM |
| `[base-platform]` | base-platform | Common libraries |
| `[account-platform]` | account-platform | Auth service |
| `[deployment]` | deployment-new | Infrastructure configs |

---

## ❌ Common Mistakes to Avoid

### Mistake #1: Wrong Issue Location
```bash
❌ WRONG: Create issue in child repo
cd services/action-manager-app
gh issue create --title "Add feature"  # No!

✅ CORRECT: Create issue in parent repo
gh issue create \
  --repo hvantran/project-management \
  --title "[action-manager] Add feature"
```

### Mistake #2: Missing Repository Prefix
```bash
❌ WRONG: [action-manager] without brackets
❌ WRONG: action-manager: with colon
❌ WRONG: Add feature (no prefix at all)

✅ CORRECT: [action-manager] Add feature
```

### Mistake #3: Wrong Branch Location
```bash
❌ WRONG: Create branch in parent repo
cd project-management
git checkout -b feature/new-feature  # No!

✅ CORRECT: Create branch in child repo
cd services/action-manager-app
git checkout -b feature/action-manager-130-new-feature
```

### Mistake #4: Incomplete Issue References
```bash
❌ WRONG: Related to #130
❌ WRONG: Closes #130

✅ CORRECT: Related to hvantran/project-management#130
✅ CORRECT: Closes hvantran/project-management#130
```

### Mistake #5: No Pull Request
```bash
❌ WRONG: Push directly to main
git push origin main  # No!

✅ CORRECT: Create feature branch and PR
git checkout -b feature/action-manager-130-feature
git push origin feature/action-manager-130-feature
gh pr create
```

---

## 🔍 Finding Information

### Where is the sprint board?
**Parent repo:** `hvantran/project-management` → Projects tab

### Where do I see my tasks?
**Parent repo:** `hvantran/project-management` → Issues → Filter by assignee

### Where do I create my branch?
**Child repo:** `services/{repo-name}/` (wherever the code is)

### Where do I create my PR?
**Child repo:** Same as branch location

### Where do I update task status?
**Parent repo:** Comment on issue in `hvantran/project-management`

---

## 📞 Quick Help

**If you're unsure about...**

| Question | Answer |
|----------|--------|
| Where to create issue? | Parent repo (project-management) |
| Where to create branch? | Child repo (services/*) |
| Where to create PR? | Child repo (same as branch) |
| How to name issue? | [repo-name] Description |
| How to name branch? | feature/repo-issue-description |
| How to reference parent issue? | hvantran/project-management#123 |
| Can I skip PR? | **NO!** PRs are mandatory |

---

## 🎯 Success Checklist

Before closing a task, verify:

- [ ] Issue created in parent repo with [repo-name] prefix
- [ ] Branch created in child repo (not parent)
- [ ] Commits use [repo-name] prefix
- [ ] Commits reference parent: hvantran/project-management#XXX
- [ ] PR created in child repo
- [ ] PR links to parent issue: Closes hvantran/project-management#XXX
- [ ] PR approved by reviewer
- [ ] PR merged (not pushed directly to main)
- [ ] Parent issue updated/closed

---

**Questions?** Check [WORKFLOW-SUMMARY.md](./WORKFLOW-SUMMARY.md) for complete details.
