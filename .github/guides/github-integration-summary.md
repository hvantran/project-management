# GitHub Integration Summary for Role-Based Agents

**Created:** $(date)
**Purpose:** Summary of GitHub integration enhancements across all role-based agents

---

## 🎯 Overview

All role-based agents have been enhanced to use **GitHub as the single source of truth** for project management. This document summarizes the GitHub workflows for each agent.

---

## 📋 Agent GitHub Integration Matrix

| Agent | GitHub Tool | Primary Use Case | Key GitHub Features |
|-------|-------------|------------------|-------------------|
| **Product Owner** | Issues, Projects, Labels, Milestones | Create user stories, manage backlog | `gh issue create --template user-story.md`, project boards |
| **Business Analyst** | Issues (comments), Technical Notes | Add detailed scenarios, document requirements | Issue comments, Wiki, docs/ folder |
| **Project Manager** | Projects, | Sprint boards, task tracking, progress monitoring | `gh project view`, milestones, sprint labels |
| **Technical Architect** | Issues (spikes), Wiki, docs/ | Create technical notes, ADRs, architecture docs | Spike issues, technical-notes/ folder |
| **Developer** | Branches, PRs, Commits, Issues | Implement features, create PRs, track tasks | `gh issue develop`, `gh pr create`, commit linking |
| **Tester** | Issues (bugs), PR reviews, Labels | Report bugs, validate PRs, track testing | Bug templates, test status labels |
| **DevOps Engineer** | Actions, Deployments, Releases | CI/CD, deployment tracking, automation | GitHub Actions, deployment status |
| **Workflow Orchestrator** | All of the above | Coordinate multi-agent workflows | Full GitHub integration orchestration |

---

## 🔄 Complete Workflow Example: Feature Development

### 1. **Product Owner Creates User Story** 📖

```bash
# Create user story issue
PO_ISSUE=$(gh issue create \
  --title "[USER STORY] Customer can view order history" \
  --body "$(cat <<'EOF'
**As a** registered customer
**I want** to view my order history
**So that** I can track my purchases

### Acceptance Criteria
Scenario: View order history
  Given a customer with completed orders
  When customer views order history
  Then orders should be displayed chronologically

### Business Value
- Priority: High
- Metric: 30% increase in reorders
EOF
)" \
  --label "type: user-story,role: product-owner,priority: high,backlog" \
  --milestone "v2.1" \
  --assignee business-analyst | grep -oP 'https://[^\s]+')

# Add to Product Backlog
gh project item-add 1 --owner hvantran --url "$PO_ISSUE"

echo "✅ User story created: $PO_ISSUE"
```

### 2. **Business Analyst Adds Detailed Scenarios** 📊

```bash
# BA adds detailed BDD scenarios as comment
gh issue comment "$PO_ISSUE" \
  --body "## Detailed BDD Scenarios

\`\`\`gherkin
Scenario Outline: View orders with filtering
  Given a customer with orders in \"<Status>\"
  When filtering by status
  Then only \"<Status>\" orders shown

  Examples:
    | Status    |
    | Completed |
    | Pending   |
    | Cancelled |

Scenario: No orders to display
  Given a new customer with no orders
  When viewing order history
  Then \"No orders found\" message displayed
\`\`\`

@technical-architect Please review for technical feasibility.
@product-owner Scenarios aligned with business need."

# Update status
gh issue edit "$PO_ISSUE" --add-label "status: refined"

echo "✅ BA completed requirements refinement"
```

### 3. **Project Manager Creates Sprint Tasks**  📅

```bash
# PM extracts issue number
STORY_NUMBER=$(echo "$PO_ISSUE" | grep -oP '\d+$')

# Create implementation tasks
TASK_1=$(gh issue create \
  --title "[TASK] Implement order history API endpoint" \
  --body "Implements #$STORY_NUMBER

## Subtasks
- [ ] Create OrderHistoryController
- [ ] Implement pagination
- [ ] Add filtering logic
- [ ] Write unit tests (80%+ coverage)

## Estimated: 8 hours" \
  --label "type: task,role: developer,sprint: current" \
  --milestone "Sprint 16" \
  --assignee developer-username | grep -oP '\d+$')

TASK_2=$(gh issue create \
  --title "[TASK] Test order history feature" \
  --body "Validates #$STORY_NUMBER

## Subtasks
- [ ] Execute BDD scenarios
- [ ] Test edge cases
- [ ] Performance testing" \
  --label "type: task,role: tester,sprint: current" \
  --milestone "Sprint 16" \
  --assignee tester-username | grep -oP '\d+$')

# Add to Sprint Board
gh project item-add 2 --owner hvantran --url "https://github.com/hvantran/project-management/issues/$TASK_1"
gh project item-add 2 --owner hvantran --url "https://github.com/hvantran/project-management/issues/$TASK_2"

echo "✅ PM created tasks: #$TASK_1, #$TASK_2"
```

### 4. **Technical Architect Creates Technical Note** 🏗️

```bash
# TA creates spike for investigation
SPIKE=$(gh issue create \
  --title "[SPIKE] Order history pagination strategy" \
  --body "Research optimal pagination for large order lists.

Timeboxed: 4 hours
Related to #$STORY_NUMBER" \
  --label "type: spike,role: technical-architect" \
  --assignee @me | grep -oP '\d+$')

# Create technical note
mkdir -p docs/technical-notes
cat > docs/technical-notes/order-history-pagination.md <<'EOF'
# Order History Pagination Strategy

**Author:** @technical-architect
**Date:** 2024-04-01
**Related:** #SPIKE_NUMBER, #STORY_NUMBER

## Problem
Need efficient pagination for customers with 1000+ orders.

## Options Analyzed
1. Offset-based pagination
2. Cursor-based pagination
3. Keyset pagination

## Recommendation
Use cursor-based pagination with order ID

## Rationale
- Consistent results during pagination
- Better performance for large datasets
- Standard for REST APIs

## Implementation Notes
- Use `order_id` as cursor
- Return `next_cursor` in response
- Default page size: 20 orders
EOF

# Commit and link
git add docs/technical-notes/order-history-pagination.md
git commit -m "[project] Add order history pagination tech note

Technical investigation for optimal pagination strategy.

Related to #$SPIKE"

git push

# Close spike
gh issue close "$SPIKE" \
  --comment "Research complete. See: [Pagination Strategy](../docs/technical-notes/order-history-pagination.md)"

echo "✅ TA completed spike and created ADR"
```

### 5. **Developer Implements Feature** 👨‍💻

```bash
# Developer starts task
gh issue edit "$TASK_1" --add-label "status: in-progress"

# Create feature branch from issue
gh issue develop "$TASK_1" --checkout
# Creates: feature/TASK_1-implement-order-history-api

# TDD Cycle (repeated)
# RED: Write test
git add src/test/java/OrderHistoryControllerTest.java
git commit -m "[action-manager] Add failing test for order history endpoint

RED phase - test first.

Related to #$TASK_1"

# GREEN: Implement
git add src/main/java/OrderHistoryController.java
git commit -m "[action-manager] Implement order history endpoint

GREEN phase - make test pass.

Related to #$TASK_1"

# REFACTOR: Polish
git add src/main/java/OrderHistoryController.java
git commit -m "[action-manager] Refactor order history for clarity

REFACTOR phase - improve code.

Related to #$TASK_1"

# Push and create PR
git push -u origin feature/$TASK_1-implement-order-history-api

PR=$(gh pr create \
  --title "Implement order history API endpoint" \
  --body "## Description
Implements order history with pagination and filtering.

## Changes
- Created OrderHistoryController
- Implemented cursor pagination
- Added status filtering
- 95% test coverage

## Testing
- ✅ All unit tests passing
- ✅ BDD scenarios passing
- ✅ Manual testing completed

## Checklist
- [x] TDD followed
- [x] Coverage ≥ 80%
- [x] Documentation updated

Closes #$TASK_1
Related to #$STORY_NUMBER" \
  --reviewer technical-architect,business-analyst \
  --label "review-needed" | grep -oP '\d+$')

echo "✅ Developer created PR #$PR"
```

### 6. **Tester Validates** 🧪

```bash
# Tester executes BDD scenarios
cd tests
cucumber features/order_history.feature

# All pass - update issue
gh issue comment "$TASK_2" \
  --body "## Test Results ✅

All BDD scenarios PASSED:
- ✅ View order history
- ✅ Pagination works correctly
- ✅ Status filtering functional
- ✅ Empty state handled

Test coverage: 98%
Performance: Average response 120ms (target: <200ms)

**Recommendation:** APPROVED for merge"

# Close testing task
gh issue close "$TASK_2" --comment "All tests passing. Feature validated."

# Approve PR
gh pr review "$PR" --approve --body "All BDD scenarios pass. Code quality excellent. LGTM! ✅"

echo "✅ Tester approved PR"
```

### 7. **Developer Merges PR** 🔄

```bash
# Merge after approval
gh pr merge "$PR" --squash --delete-branch

# Automatically closes #TASK_1 and updates GitHub Projects

echo "✅ PR merged, task closed automatically"
```

### 8. **DevOps Deploys** 🚀

```bash
# DevOps triggers deployment
gh workflow run deploy-staging.yml \
  --ref main \
  --field environment=staging

# Monitor deployment
gh run watch

# Update user story with deployment status
gh issue comment "$STORY_NUMBER" \
  --body "✅ Deployed to staging environment
  
**URL:** https://staging.example.com
**Version:** v2.1.0-rc.1
**Deployment Time:** $(date)

Ready for UAT testing."

echo "✅ Deployed to staging"
```

### 9. **Product Owner Validates & Closes** ✅

```bash
# PO validates in staging
# After successful UAT

gh issue close "$STORY_NUMBER" \
  --comment "✅ **User Story Accepted**

All acceptance criteria met:
- [x] Order history displays correctly
- [x] Pagination works smoothly
- [x] Filtering functional
- [x] Performance meets SLA

Approved for production release.

Great work team! @developer @tester @devops"

echo "✅ User story completed!"
```

---

## 📊 GitHub Project Board Automation

### Sprint Board Columns

```
┌─────────────┬──────────────┬─────────────┬─────────────┬─────────┐
│   To Do     │ In Progress  │  In Review  │   Testing   │  Done   │
├─────────────┼──────────────┼─────────────┼─────────────┼─────────┤
│ New issues  │ Assigned     │ PR opened   │ QA testing  │ Merged  │
│ or labeled  │ + labeled    │ + labeled   │ + labeled   │ + closed│
│ "sprint:    │ "status:     │ "review-    │ "status:    │ issues  │
│  current"   │  in-progress"│  needed"    │  testing"   │         │
└─────────────┴──────────────┴─────────────┴─────────────┴─────────┘
```

### Automation Rules

```yaml
# .github/workflows/project-automation.yml
name: Project Board Automation

on:
  issues:
    types: [opened, labeled, assigned, closed]
  pull_request:
    types: [opened, closed, review_requested]

jobs:
  move-cards:
    runs-on: ubuntu-latest
    steps:
      - name: Move new issues to To Do
        if: github.event.action == 'labeled' && contains(github.event.label.name, 'sprint: current')
        # Move to "To Do" column
        
      - name: Move assigned issues to In Progress
        if: github.event.action == 'assigned'
        # Move to "In Progress" column
        
      - name: Move PR opened to In Review
        if: github.event_name == 'pull_request' && github.event.action == 'opened'
        # Move related issue to "In Review"
        
      - name: Move to Testing when labeled
        if: contains(github.event.label.name, 'status: testing')
        # Move to "Testing" column
        
      - name: Move closed to Done
        if: github.event.action == 'closed'
        # Move to "Done" column
```

---

## 🏷️ Standard GitHub Labels

### Priority Labels
```bash
gh label create "priority: critical" --color "FF0000" --description "Must fix immediately"
gh label create "priority: high" --color "FF9900" --description "Important, do this sprint"
gh label create "priority: medium" --color "FFCC00" --description "Should do soon"
gh label create "priority: low" --color "99CC00" --description "Nice to have"
```

### Type Labels
```bash
gh label create "type: user-story" --color "0E8A16" --description "Product Owner user story"
gh label create "type: task" --color "1D76DB" --description "Implementation task"
gh label create "type: bug" --color "D73A4A" --description "Bug report"
gh label create "type: spike" --color "FBCA04" --description "Research/investigation"
gh label create "type: technical-debt" --color "666666" --description "Technical debt"
```

### Role Labels
```bash
gh label create "role: product-owner" --color "C5DEF5" --description "PO responsibility"
gh label create "role: business-analyst" --color "BFD4F2" --description "BA responsibility"
gh label create "role: developer" --color "B4E1FA" --description "Developer work"
gh label create "role: tester" --color "BFDADC" --description "Testing work"
gh label create "role: technical-architect" --color "D4C5F9" --description "Architecture work"
gh label create "role: devops" --color "E99695" --description "DevOps work"
```

### Status Labels
```bash
gh label create "status: needs-review" --color "FBCA04" --description "Awaiting review"
gh label create "status: in-progress" --color "0E8A16" --description "Work in progress"
gh label create "status: blocked" --color "D93F0B" --description "Blocked by dependency"
gh label create "status: ready" --color "0E8A16" --description "Ready to start"
gh label create "status: testing" --color "1D76DB" --description "In QA testing"
```

### Sprint Labels
```bash
gh label create "sprint: current" --color "0052CC" --description "Current sprint"
gh label create "sprint: next" --color "5319E7" --description "Next sprint"
gh label create "backlog" --color "grey" --description "Product backlog"
```

---

## 📈 Dashboard Queries

### Product Owner Dashboard

```bash
#!/bin/bash
# Product Owner daily dashboard

echo "=== PRODUCT BACKLOG HEALTH ==="
echo "Stories ready for sprint:"
gh issue list --label "type: user-story,status: ready,backlog" --json number,title | jq 'length'

echo ""
echo "Stories needing refinement:"
gh issue list --label "type: user-story,backlog" --label "!status: ready" --json number,title

echo ""
echo "=== CURRENT SPRINT PROGRESS ==="
echo "Sprint stories:"
gh issue list --label "type: user-story,sprint: current" --json number,title,state | \
  jq -r '.[] | "#\(.number): \(.title) [\(.state)]"'
```

### Project Manager Dashboard

```bash
#!/bin/bash
# Project Manager sprint tracking

MILESTONE=5  # Sprint 16

echo "=== SPRINT BURNDOWN ==="
gh api "repos/hvantran/project-management/milestones/$MILESTONE" | \
  jq '{sprint: .title, total: (.open_issues + .closed_issues), completed: .closed_issues, remaining: .open_issues, due: .due_on}'

echo ""
echo "=== TASK STATUS ==="
gh issue list --milestone "$MILESTONE" --json labels,state | \
  jq 'group_by(.labels[] | select(.name | startswith("status:")) | .name) | map({status: (.[0].labels[] | select(.name | startswith("status:")) | .name), count: length})'

echo ""
echo "=== BLOCKERS ==="
gh issue list --label "status: blocked,sprint: current" --json number,title,assignees
```

### Developer Dashboard

```bash
#!/bin/bash
# Developer daily tasks

echo "=== MY TASKS TODAY ==="
gh issue list --assignee @me --label "sprint: current" --state open \
  --json number,title,labels | \
  jq -r '.[] | "#\(.number): \(.title)"'

echo ""
echo "=== MY OPEN PRs ==="
gh pr list --author @me --state open \
  --json number,title,reviewRequests,reviews

echo ""
echo "=== PRs NEEDING MY REVIEW ==="
gh pr list --review-requested @me \
  --json number,title,author
```

### Tester Dashboard

```bash
#!/bin/bash
# Tester QA tracking

echo "=== ISSUES AWAITING TESTING ==="
gh issue list --label "status: testing" --json number,title,assignees

echo ""
echo "=== OPEN BUGS ==="
gh issue list --label "type: bug" --state open \
  --json number,title,labels,createdAt | \
  jq -r '.[] | "#\(.number): \(.title) [Priority: \(.labels[] | select(.name | startswith("priority:")) | .name)]"'

echo ""
echo "=== RECENTLY MERGED PRs (Need Validation) ==="
gh pr list --state merged --limit 10 \
  --json number,title,mergedAt,closedAt | \
  jq -r '.[] | "#\(.number): \(.title) (merged \(.mergedAt))"'
```

---

## 🎯 Key Benefits

### 1. **Single Source of Truth**
- All work tracked in GitHub Issues
- No external tools needed (Jira, Trello, etc.)
- Git history and issue history perfectly aligned

### 2. **Traceability**
- Every commit links to issue: `Related to #123`
- Every PR closes an issue: `Closes #123`
- User story → Tasks → Commits → PRs → Deployment

### 3. **Automation**
- GitHub Actions automate board movements
- Auto-close issues when PRs merge
- Auto-update milestones

### 4. **Transparency**
- Real-time progress visible on boards
- Stakeholders can track progress
- Historical data for retrospectives

### 5. **Developer Experience**
- Create branches from issues: `gh issue develop 123`
- CLI-first workflow
- Integrated with existing Git workflow

---

## 📚 Reference Links

- **GitHub Project Management Guide:** [github-project-management-guide.md](./github-project-management-guide.md)
- **Product Owner Agent:** [product-owner.agent.md](../agents/product-owner.agent.md)
- **Project Manager Agent:** [project-manager.agent.md](../agents/project-manager.agent.md)
- **Developer Agent:** [developer.agent.md](../agents/developer.agent.md)
- **Business Analyst Agent:** [business-analyst.agent.md](../agents/business-analyst.agent.md)
- **Tester Agent:** [tester.agent.md](../agents/tester.agent.md)
- **Technical Architect Agent:** [technical-architect.agent.md](../agents/technical-architect.agent.md)
- **DevOps Engineer Agent:** [devops-engineer.agent.md](../agents/devops-engineer.agent.md)
- **Workflow Orchestrator:** [workflow-orchestrator.agent.md](../agents/workflow-orchestrator.agent.md)

---

## 🚀 Quick Start

### Setup GitHub Labels
```bash
# Run this once to set up all standard labels
./scripts/setup-github-labels.sh
```

### Create First Sprint
```bash
# Create sprint milestone
gh api repos/hvantran/project-management/milestones \
  -f title="Sprint 1" \
  -f due_on="2024-04-15T23:59:59Z"

# Create sprint board
gh project create --owner hvantran --title "Sprint 1 Board"
```

### Create First User Story (Product Owner)
```bash
gh issue create \
  --title "[USER STORY] First feature" \
  --template user-story.md \
  --label "type: user-story,role: product-owner,priority: high"
```

---

**Status:** ✅ All role-based agents enhanced with GitHub integration
**Last Updated:** $(date)
**Maintained by:** Workflow Orchestrator Agent
