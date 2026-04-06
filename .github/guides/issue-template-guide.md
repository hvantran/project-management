# Issue Template Guide

Complete reference for using GitHub issue templates in the multi-agent workflow. Each template is designed for specific agent roles and follows BDD/TDD principles.

---

## 🎯 Overview

**Location:** `.github/ISSUE_TEMPLATE/`

**Purpose:** Standardize issue creation across different roles and ensure all necessary information is captured from the start.

**Available Templates:**
1. **User Story** - Product Owner creates feature requests with BDD acceptance criteria
2. **Technical Task** - Developer/Technical Architect creates implementation tasks
3. **Bug Report** - Tester reports defects with full context
4. **Technical Spike** - Technical Architect initiates research/investigation

---

## 📝 Issue Templates

### **1. User Story Template** (Product Owner)
`.github/ISSUE_TEMPLATE/user-story.md`

```markdown
---
name: User Story
about: Product Owner creates user stories with BDD acceptance criteria
title: '[USER STORY] '
labels: 'type: user-story, role: product-owner, status: needs-review'
assignees: ''
---

## User Story

**As a** [user type]
**I want** [goal/desire]
**So that** [benefit/value]

## Business Value
- **Priority:** High / Medium / Low
- **Business Value:** [Description]
- **Success Metrics:** [How we measure success]

## Acceptance Criteria (BDD Format)

### Scenario: [Main success scenario]
```gherkin
Given [initial context]
When [action or event]
Then [expected outcome]
And [additional verification]
```

### Scenario: [Alternative or error scenario]
```gherkin
Given [different context]
When [action]
Then [expected result]
```

## Dependencies
- [ ] Dependency 1
- [ ] Dependency 2

## Technical Notes
[Any technical considerations or constraints]

## Definition of Done
- [ ] Acceptance criteria validated
- [ ] Code reviewed
- [ ] Tests passing
- [ ] Documentation updated
- [ ] Deployed to staging

---

**Next Steps:**
- Assign to @business-analyst for detailed requirements
- Add to Product Backlog project
```

**When to Use:**
- Creating new feature requests
- Defining user-facing functionality
- Starting sprint planning
- Backlog grooming

**Key Elements:**
- User story format (As a/I want/So that)
- BDD scenarios with Given-When-Then
- Clear acceptance criteria
- Business value and metrics
- Definition of Done checklist

---

### **2. Technical Task Template** (Developer)
`.github/ISSUE_TEMPLATE/task.md`

```markdown
---
name: Technical Task
about: Implementation task created by Developer or Technical Architect
title: '[TASK] '
labels: 'type: task, role: developer'
assignees: ''
---

## Task Description
[Clear description of what needs to be implemented]

## Related User Story
Implements #[issue-number]

## Technical Approach
- [ ] Step 1
- [ ] Step 2
- [ ] Step 3

## TDD Checklist
- [ ] Write failing tests (RED)
- [ ] Implement minimal code (GREEN)
- [ ] Refactor code (REFACTOR)
- [ ] Code coverage ≥ 80%

## Files to Modify
- `path/to/file1.java`
- `path/to/file2.java`

## Estimated Effort
[Hours or Story Points]

## Definition of Done
- [ ] All tests passing
- [ ] Code reviewed
- [ ] Documentation updated
- [ ] PR merged

---

**Branch:** `feature/[issue-number]-brief-description`
```

**When to Use:**
- Breaking down user stories into implementation tasks
- Technical work not tied to user stories
- Refactoring or infrastructure work
- Creating specific development tasks

**Key Elements:**
- Clear task description
- Link to parent user story
- Technical approach steps
- TDD checklist (Red-Green-Refactor)
- Files to modify
- Branch naming convention

---

### **3. Bug Report Template** (Tester)
`.github/ISSUE_TEMPLATE/bug-report.md`

```markdown
---
name: Bug Report
about: Report a defect found during testing
title: '[BUG] '
labels: 'type: bug, role: tester, priority: high'
assignees: ''
---

## Bug Description
[Clear, concise description of the bug]

## Severity
- [ ] Critical - System down
- [ ] High - Major feature broken
- [ ] Medium - Feature partially working
- [ ] Low - Minor issue

## Environment
- **Environment:** Production / Staging / Dev
- **Version:** [Version number]
- **Browser/OS:** [If applicable]

## Steps to Reproduce
1. Step 1
2. Step 2
3. Step 3

## Expected Behavior
[What should happen]

## Actual Behavior
[What actually happens]

## Evidence
```
[Paste logs, screenshots, or error messages]
```

## Impact
- **User Impact:** [Who is affected and how]
- **Frequency:** [How often does this occur]
- **Workaround:** [If available]

## Related
- Related User Story: #[issue-number]
- BDD Scenario: [Which scenario failed]

---

**Assignee:** @developer
```

**When to Use:**
- Reporting defects found during testing
- Documenting production issues
- Tracking failed test scenarios
- Recording regression issues

**Key Elements:**
- Clear bug description
- Severity classification
- Environment details
- Reproducible steps
- Expected vs. actual behavior
- Evidence (logs, screenshots)
- Impact assessment
- Link to related user story and BDD scenario

---

### **4. Technical Spike Template** (Technical Architect)
`.github/ISSUE_TEMPLATE/technical-spike.md`

```markdown
---
name: Technical Spike
about: Research or investigation task
title: '[SPIKE] '
labels: 'type: spike, role: technical-architect'
assignees: ''
---

## Research Question
[What do we need to investigate?]

## Context
[Why is this research needed?]

## Goals
- [ ] Goal 1
- [ ] Goal 2
- [ ] Goal 3

## Timeboxed Effort
[Maximum time to spend: e.g., 4 hours, 1 day]

## Research Areas
1. Area 1
2. Area 2
3. Area 3

## Success Criteria
- [ ] Questions answered
- [ ] Recommendations documented
- [ ] Findings presented to team

## Deliverables
- [ ] Technical note document
- [ ] Architecture Decision Record (ADR) if applicable
- [ ] Proof of concept (if applicable)

---

**Output:** Will be documented in `docs/technical-notes/[spike-name].md`
```

**When to Use:**
- Investigating new technologies
- Researching architecture decisions
- Exploring technical solutions
- Proof of concept work
- Performance investigation

**Key Elements:**
- Clear research question
- Context and rationale
- Specific goals
- Timeboxed effort (to prevent endless research)
- Research areas to explore
- Success criteria
- Expected deliverables

---

## 🚨 Multi-Repo Naming Conventions

**CRITICAL:** When creating issues in the parent `project-management` repository, **always** prefix the title with the target repository name:

### Issue Title Format
```
[repo-name] Description of the issue
```

### Repository Name Mapping

| Repository | Prefix |
|------------|--------|
| parent-pom | `[parent-pom]` |
| action-manager-app | `[action-manager]` |
| ecommerce-stats-app | `[ecommerce-stats]` |
| external-endpoint-collector | `[endpoint-collector]` |
| spring-kafka-notifier | `[kafka-notifier]` |
| template-management-app | `[template-manager]` |
| base-platform | `[base-platform]` |
| account-platform | `[account-platform]` |
| deployment-new | `[deployment]` |

### Examples

✅ **CORRECT:**
```
[action-manager] Add Kafka consumer health check endpoint
[ecommerce-stats] Implement sales analytics dashboard
[endpoint-collector] Fix PostgreSQL connection timeout
[parent-pom] Update Spring Boot to 3.2.1
```

❌ **INCORRECT:**
```
Add Kafka consumer health check endpoint (missing repo prefix)
action-manager: Add health check (wrong format)
feat: Add health check (using conventional commit instead)
```

---

## 📋 Using Issue Templates

### Creating a New Issue

**Via GitHub UI:**
1. Go to **Issues** tab in `project-management` repository
2. Click **New Issue**
3. Choose appropriate template
4. Fill in all required fields
5. Add **repository prefix** to title: `[repo-name] Description`
6. Assign appropriate labels
7. Link to project board
8. Create issue

**Via GitHub CLI:**
```bash
# Create user story
gh issue create --template user-story.md \
  --title "[action-manager] User can view Kafka consumer status" \
  --label "type: user-story,role: product-owner,status: needs-review" \
  --project "Product Backlog"

# Create technical task
gh issue create --template task.md \
  --title "[action-manager] Implement Kafka consumer health endpoint" \
  --label "type: task,role: developer" \
  --assignee @me

# Create bug report
gh issue create --template bug-report.md \
  --title "[ecommerce-stats] Sales chart not loading" \
  --label "type: bug,priority: high,role: tester"

# Create technical spike
gh issue create --template technical-spike.md \
  --title "[endpoint-collector] Research rate limiting strategies" \
  --label "type: spike,role: technical-architect"
```

---

## 🔄 Issue Lifecycle

### 1. User Story (Product Owner)
```
Created → BA Analysis → Refined → Estimated → Ready → Sprint Planning → Development
```

### 2. Technical Task (Developer)
```
Created → In Progress → Code Review → Testing → Done
```

### 3. Bug Report (Tester)
```
Reported → Reproduced → In Progress → Fixed → Verified → Closed
```

### 4. Technical Spike (Technical Architect)
```
Created → Research → Document Findings → Review → Closed
```

---

## 🎯 Best Practices

### For All Templates

1. **Be Specific:**
   - Use clear, descriptive titles with repository prefix
   - Include all relevant context
   - Link to related issues

2. **Repository Prefix:**
   - **ALWAYS** include `[repo-name]` in issue title
   - This indicates which child repository will receive feature branches and PRs
   - Required for multi-repo workflow tracking

3. **Use Labels:**
   - Apply appropriate type, priority, and role labels
   - Add sprint labels when planning
   - Use status labels to track progress

4. **Link to Projects:**
   - Add to appropriate project board
   - Ensures visibility in kanban workflow

5. **Assign Owners:**
   - Assign to responsible person or team
   - Tag relevant agents in comments

### For User Stories

- Write from user perspective (As a/I want/So that)
- Include measurable success metrics
- Define clear BDD scenarios with Given-When-Then
- Keep scenarios focused and testable
- List all dependencies upfront

### For Technical Tasks

- Link to parent user story
- Break down into small, manageable steps
- Follow TDD checklist religiously
- List all files to modify
- Include branch naming pattern

### For Bug Reports

- Provide reproducible steps
- Include environment details
- Attach evidence (logs, screenshots)
- Reference failed BDD scenario
- Assess user impact

### For Technical Spikes

- Timebox the effort (don't research forever)
- Define clear success criteria
- Document findings in `docs/technical-notes/`
- Create ADR if architecture decision made
- Share learnings with team

---

## 🔗 Related Documentation

- **Agents:** [Available Agents](../agents/README.md)
- **Workflow:** [Complete Workflow Summary](WORKFLOW-SUMMARY.md)
- **GitHub Management:** [GitHub Project Management](github-project-management-guide.md)
- **Multi-Repo:** [Multi-Repo Cheatsheet](multi-repo-cheatsheet.md)
- **Templates:** [Issue Templates](../ISSUE_TEMPLATE/)

---

## 📚 Quick Reference Commands

```bash
# List all issue templates
ls .github/ISSUE_TEMPLATE/

# Create issue with template
gh issue create --template <template-name>.md

# View template content
cat .github/ISSUE_TEMPLATE/<template-name>.md

# List issues by type
gh issue list --label "type: user-story"
gh issue list --label "type: task"
gh issue list --label "type: bug"
gh issue list --label "type: spike"

# List issues by role
gh issue list --label "role: product-owner"
gh issue list --label "role: developer"
gh issue list --label "role: tester"
gh issue list --label "role: technical-architect"

# List issues by repository
gh issue list --search "[action-manager]"
gh issue list --search "[ecommerce-stats]"
gh issue list --search "[endpoint-collector]"
```

---

**Last Updated:** January 2025  
**Maintainer:** Development Team  
**Version:** 1.0
