# GitHub Project Management Integration Guide

Complete guide for using GitHub to manage backlogs, sprints, issues, technical notes, and branches across all role-based agents.

---

## 🎯 Overview

**Purpose:** Leverage GitHub as a centralized project management tool for the entire software development workflow.

**Benefits:**
- ✅ Single source of truth
- ✅ Built-in issue tracking and project boards
- ✅ Git branch management
- ✅ Pull request reviews
- ✅ GitHub Actions for automation
- ✅ Rich markdown support
- ✅ Integration with development workflow

---

## 📋 GitHub Project Structure

### **Repository Organization**

```
project-management/  (Main repo)
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── user-story.md          # Product Owner template
│   │   ├── technical-spike.md     # Technical Architect template
│   │   ├── bug-report.md          # Tester template
│   │   └── task.md                # Developer template
│   ├── PULL_REQUEST_TEMPLATE.md
│   ├── workflows/
│   │   ├── ci.yml                 # CI/CD automation
│   │   └── project-automation.yml # Auto-move issues on boards
│   └── agents/                    # Custom agents
├── docs/
│   ├── architecture/              # Technical Architect docs
│   ├── requirements/              # Business Analyst docs
│   ├── user-stories/              # Product Owner docs
│   └── technical-notes/           # Developer/Architect notes
└── projects/                      # GitHub Projects (Kanban boards)
    ├── Sprint Board
    ├── Product Backlog
    └── Technical Debt
```

---

## 🏷️ Label System

### **Priority Labels**
- `priority: critical` 🔴 - Must be done immediately
- `priority: high` 🟠 - Should be done this sprint
- `priority: medium` 🟡 - Should be done soon
- `priority: low` 🟢 - Nice to have

### **Type Labels**
- `type: user-story` 📖 - User story (Product Owner)
- `type: task` ✅ - Implementation task (Developer)
- `type: bug` 🐛 - Bug report (Tester)
- `type: spike` 🔬 - Research/investigation (Technical Architect)
- `type: technical-debt` ⚙️ - Technical debt
- `type: documentation` 📝 - Documentation work

### **Role Labels**
- `role: product-owner` 👔 - Requires PO input
- `role: business-analyst` 📊 - Requires BA analysis
- `role: developer` 👨‍💻 - Development work
- `role: tester` 🧪 - Testing work
- `role: technical-architect` 🏗️ - Architecture decisions
- `role: devops` 🚀 - Infrastructure/deployment

### **Status Labels**
- `status: needs-review` 👀 - Awaiting approval
- `status: in-progress` 🔄 - Currently being worked on
- `status: blocked` 🚧 - Blocked by dependency
- `status: ready` ✅ - Ready for implementation

### **Sprint Labels**
- `sprint: current` 🏃 - Current sprint
- `sprint: next` ⏭️ - Next sprint
- `backlog` 📚 - Product backlog

---

## 🗂️ GitHub Projects (Kanban Boards)

### **Sprint Board**

**Columns:**
1. **To Do** - Ready for sprint
2. **In Progress** - Currently being worked on
3. **In Review** - Code review or approval needed
4. **Testing** - QA validation
5. **Done** - Completed this sprint

**Automation:**
- Auto-move to "In Progress" when issue assigned
- Auto-move to "In Review" when PR opened
- Auto-move to "Done" when PR merged

### **Product Backlog**

**Columns:**
1. **New** - Just created
2. **Refined** - Requirements analyzed by BA
3. **Estimated** - Sized by team
4. **Ready** - Ready for sprint planning
5. **Archived** - Deprioritized/Won't do

### **Technical Debt Board**

**Columns:**
1. **Identified** - Technical debt logged
2. **Prioritized** - Ranked by impact
3. **Planned** - Scheduled for sprint
4. **In Progress** - Being addressed
5. **Resolved** - Completed

---

## 📝 Issue Templates

**Complete Reference:** See [Issue Template Guide](issue-template-guide.md) for detailed documentation of all issue templates.

**Quick Reference:**
- **User Story** (`.github/ISSUE_TEMPLATE/user-story.md`) - Product Owner creates features with BDD acceptance criteria
- **Technical Task** (`.github/ISSUE_TEMPLATE/task.md`) - Developer creates implementation tasks with TDD checklist
- **Bug Report** (`.github/ISSUE_TEMPLATE/bug-report.md`) - Tester reports defects with reproducible steps
- **Technical Spike** (`.github/ISSUE_TEMPLATE/technical-spike.md`) - Technical Architect initiates timeboxed research

**Multi-Repo Naming:** Always prefix issue titles with `[repo-name]` to indicate the target repository.

---

## 🌳 Branch Naming Strategy

### **Branch Types**

```
feature/[issue-number]-brief-description
├─ feature/123-oauth2-authentication
├─ feature/456-payment-processing
└─ feature/789-user-profile

bugfix/[issue-number]-brief-description
├─ bugfix/234-login-timeout
└─ bugfix/567-null-pointer-exception

spike/[issue-number]-brief-description
├─ spike/345-database-performance
└─ spike/678-microservices-architecture

refactor/[issue-number]-brief-description
├─ refactor/890-legacy-code-cleanup
└─ refactor/901-extract-service-layer

hotfix/[issue-number]-brief-description
├─ hotfix/111-production-crash
└─ hotfix/222-security-vulnerability
```

### **Branch Workflow**

```
1. Create issue on GitHub
2. Assign to appropriate role
3. Create branch from issue:
   $ gh issue develop 123 --name feature/123-oauth2-auth
4. Make commits referencing issue:
   $ git commit -m "[action-manager] Implement OAuth2 service

   - Add OAuth2 service class
   - Implement token validation
   
   Related to #123"
5. Create PR when ready:
   $ gh pr create --title "Implement OAuth2 authentication" \
                  --body "Closes #123"
6. Request review from appropriate role
7. Merge after approval
```

---

## 📚 Technical Notes Management

### **Location**
`docs/technical-notes/[topic-name].md`

### **Structure**

```markdown
# Technical Note: [Topic]

**Author:** @technical-architect / @developer
**Date:** YYYY-MM-DD
**Related Issues:** #123, #456
**Status:** Draft / Final / Deprecated

## Context
[Why this note exists]

## Problem Statement
[What problem are we solving]

## Analysis
[Detailed analysis]

## Recommendations
[What we recommend]

## Decision
[Final decision made]

## References
- Link 1
- Link 2

## Related ADRs
- [ADR-001: Architecture Decision](../architecture/adr-001.md)
```

### **Linking to Issues**

In any issue, reference technical notes:
```markdown
See technical note: [OAuth2 Implementation](../docs/technical-notes/oauth2-implementation.md)
```

---

## 🔄 GitHub Actions Automation

### **Auto-assign Labels**
`.github/workflows/label-automation.yml`

```yaml
name: Auto Label Issues and PRs

on:
  issues:
    types: [opened, edited]
  pull_request:
    types: [opened, edited]

jobs:
  auto-label:
    runs-on: ubuntu-latest
    steps:
      - name: Label user stories
        if: contains(github.event.issue.title, '[USER STORY]')
        uses: actions/github-script@v7
        with:
          script: |
            github.rest.issues.addLabels({
              owner: context.repo.owner,
              repo: context.repo.repo,
              issue_number: context.issue.number,
              labels: ['type: user-story', 'role: product-owner']
            })
      
      - name: Label bugs
        if: contains(github.event.issue.title, '[BUG]')
        uses: actions/github-script@v7
        with:
          script: |
            github.rest.issues.addLabels({
              owner: context.repo.owner,
              repo: context.repo.repo,
              issue_number: context.issue.number,
              labels: ['type: bug', 'priority: high']
            })
```

### **Auto-move on Project Board**
`.github/workflows/project-automation.yml`

```yaml
name: Project Board Automation

on:
  issues:
    types: [assigned, closed]
  pull_request:
    types: [opened, closed]

jobs:
  move-assigned-issues:
    runs-on: ubuntu-latest
    if: github.event.action == 'assigned'
    steps:
      - name: Move to In Progress
        uses: actions/github-script@v7
        with:
          script: |
            # Move issue to "In Progress" column when assigned
            
  move-pr-opened:
    runs-on: ubuntu-latest
    if: github.event.action == 'opened' && github.event.pull_request
    steps:
      - name: Move to In Review
        uses: actions/github-script@v7
        with:
          script: |
            # Move associated issue to "In Review" when PR opened
            
  move-completed:
    runs-on: ubuntu-latest
    if: github.event.action == 'closed'
    steps:
      - name: Move to Done
        uses: actions/github-script@v7
        with:
          script: |
            # Move to "Done" when closed
```

---

## 🚀 Common Workflows

### **Workflow 1: Product Owner Creates User Story**

```bash
# 1. Create user story issue
gh issue create \
  --title "[USER STORY] User can login with OAuth2" \
  --template user-story.md \
  --label "type: user-story,role: product-owner,priority: high" \
  --assignee business-analyst

# 2. Add to Product Backlog project
gh project item-add 1 --owner hvantran --content-id <issue-url>

# 3. BA reviews and refines
# (BA adds detailed BDD scenarios to issue comments)

# 4. PM adds to sprint
gh issue edit 123 --add-label "sprint: current"
gh project item-edit --project-id 1 --field "Status" --value "Ready"
```

### **Workflow 2: Developer Implements Feature**

```bash
# 1. Pick issue from sprint board
gh issue list --label "sprint: current,role: developer" --assignee @me

# 2. Assign to self
gh issue edit 123 --add-assignee @me

# 3. Create feature branch from issue
gh issue develop 123 --checkout

# 4. Implement using TDD
# (Write tests, implement code, commit)

# 5. Create sub-issues for tasks
gh issue create \
  --title "[TASK] Implement OAuth2 service class" \
  --body "Part of #123" \
  --label "type: task,role: developer"

# 6. Create PR when ready
gh pr create \
  --title "Implement OAuth2 authentication" \
  --body "Closes #123" \
  --reviewers technical-architect,business-analyst

# 7. Link to technical notes
echo "See: [OAuth2 Tech Note](docs/technical-notes/oauth2.md)" \
  >> PR body
```

### **Workflow 3: Tester Finds Bug**

```bash
# 1. Create bug report
gh issue create \
  --title "[BUG] Login times out after 10 seconds" \
  --template bug-report.md \
  --label "type: bug,priority: high,role: developer" \
  --assignee developer-username

# 2. Link to failed BDD scenario
gh issue comment 234 \
  --body "BDD Scenario failed: \`features/authentication.feature:15\`"

# 3. Developer creates hotfix branch
gh issue develop 234 \
  --name hotfix/234-login-timeout \
  --checkout

# 4. Fix and create PR
gh pr create \
  --title "[HOTFIX] Fix login timeout issue" \
  --body "Fixes #234"
```

### **Workflow 4: Technical Architect Creates Spike**

```bash
# 1. Create spike issue
gh issue create \
  --title "[SPIKE] Evaluate caching strategies" \
  --template technical-spike.md \
  --label "type: spike,role: technical-architect" \
  --assignee @me

# 2. Research and document findings
mkdir -p docs/technical-notes
cat > docs/technical-notes/caching-evaluation.md << 'EOF'
# Caching Strategies Evaluation

## Research Question
Which caching strategy should we use for user session data?

## Options Evaluated
1. Redis
2. Memcached
3. In-memory cache

## Recommendation
Use Redis for distributed caching...
EOF

# 3. Commit technical note
git add docs/technical-notes/caching-evaluation.md
git commit -m "[project] Add caching evaluation tech note

Related to #345"

# 4. Link in issue
gh issue comment 345 \
  --body "Research complete. See: [Caching Evaluation](docs/technical-notes/caching-evaluation.md)"

# 5. Close spike
gh issue close 345 --comment "Spike complete. Recommendation: Use Redis"
```

---

## 📊 Dashboard Views

### **Sprint Dashboard**

```bash
# View current sprint issues
gh issue list \
  --label "sprint: current" \
  --json number,title,assignees,labels,state \
  --template '{{range .}}{{.number}}: {{.title}} [@{{range .assignees}}{{.login}}{{end}}] {{.state}}
{{end}}'

# View sprint progress
gh project view 1 --owner hvantran --format json | \
  jq '.items | group_by(.status) | map({status: .[0].status, count: length})'
```

### **Backlog Grooming**

```bash
# List unrefined stories
gh issue list \
  --label "type: user-story" \
  --label "!status: refined" \
  --json number,title,createdAt

# List stories ready for sprint
gh issue list \
  --label "status: ready" \
  --label "backlog" \
  --json number,title,labels
```

### **Technical Debt Tracking**

```bash
# List all technical debt
gh issue list \
  --label "type: technical-debt" \
  --state all \
  --json number,title,state,createdAt

# Prioritized technical debt
gh issue list \
  --label "type: technical-debt,priority: high"
```

---

## ✅ Best Practices

### 1. **Single Source of Truth**
- All work items are GitHub issues
- All discussions happen in issue comments
- All decisions are documented in technical notes

### 2. **Traceability**
- Every commit references an issue: `Related to #123`
- Every PR closes an issue: `Closes #123`
- Every technical note links to issues

### 3. **Automation**
- Use GitHub Actions for repetitive tasks
- Auto-label based on title patterns
- Auto-move issues on project boards

### 4. **Branch Strategy**
- Always create branches from issues
- Follow naming conventions
- Delete branches after merge

### 5. **Documentation**
- Technical notes in `docs/technical-notes/`
- Architecture decisions in `docs/architecture/adr-*.md`
- Meeting notes reference issue numbers

### 6. **Labels**
- Use consistent labeling
- Combine type, role, priority, and status labels
- Review and clean up labels regularly

---

## 🎓 Quick Reference

### **Common Commands**

```bash
# Issues
gh issue list
gh issue create --template user-story.md
gh issue view 123
gh issue edit 123 --add-label "priority: high"
gh issue close 123

# Projects
gh project list --owner hvantran
gh project view 1
gh project item-add 1 --content-id <issue-url>

# Pull Requests
gh pr create --title "Feature X" --body "Closes #123"
gh pr view 456
gh pr review 456 --approve
gh pr merge 456

# Labels
gh label list
gh label create "priority: critical" --color "FF0000"

# Search
gh search issues "is:open label:bug"
gh search prs "is:open label:needs-review"
```

---

## 📈 Metrics to Track

### Sprint Metrics
- **Velocity**: Story points completed per sprint
- **Burn-down**: Work remaining over time
- **Cycle time**: Time from start to done
- **Lead time**: Time from creation to done

### Quality Metrics
- **Bug count**: Open bugs by severity
- **Bug fix time**: Average time to resolve bugs
- **Rework rate**: Issues reopened after closure
- **Code review time**: Time spent in review

### Process Metrics
- **Sprint completion rate**: % of sprint goals achieved
- **Story refinement rate**: % of backlog refined
- **Technical debt ratio**: Technical debt vs feature work

---

## 🔗 Integration with Agents

Each role-based agent will use GitHub tools:

- **@product-owner**: Create user story issues
- **@business-analyst**: Add detailed scenarios to issues
- **@project-manager**: Manage projects and sprint boards
- **@technical-architect**: Create spikes, maintain technical notes
- **@developer**: Create tasks, branches, PRs, update issues
- **@tester**: Create bug reports, validate acceptance criteria
- **@devops-engineer**: Manage deployment issues, automate workflows

---

**Summary:** GitHub becomes your central hub for all project management, replacing external tools like Jira while keeping everything in sync with your code repository. 🚀
