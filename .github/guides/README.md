# Workflow Guides & Documentation

This directory contains documentation, guides, and reference materials for the multi-agent software development workflow.

## 📁 Directory Structure

```
.github/
├── agents/          ← Agent definitions (*.agent.md)
└── guides/          ← You are here (Documentation)
    ├── *.md         ← Guide files
    └── README.md    ← This file
```

## 📚 Available Guides

### Quick Reference

| Guide | Purpose | Use When |
|-------|---------|----------|
| [**multi-repo-cheatsheet.md**](multi-repo-cheatsheet.md) | Daily commands, naming conventions, common mistakes | Daily development work |
| [**issue-template-guide.md**](issue-template-guide.md) | Complete reference for all issue templates | Creating issues, understanding templates |
| [**WORKFLOW-SUMMARY.md**](WORKFLOW-SUMMARY.md) | Complete end-to-end workflow with examples | Learning the full process |
| [**github-project-management-guide.md**](github-project-management-guide.md) | GitHub Projects, issues, PRs, CI/CD setup | Setting up GitHub integration |
| [**approval-workflow-guide.md**](approval-workflow-guide.md) | Review/approval gates between workflow phases | Implementing quality gates |
| [**github-integration-summary.md**](github-integration-summary.md) | GitHub integration summary and best practices | Understanding GitHub workflow |

---

## 🎯 Guide Details

### 1. Multi-Repo Cheat Sheet
**File:** [multi-repo-cheatsheet.md](multi-repo-cheatsheet.md)

**Quick Reference for:**
- The golden rules (issues, branches, PRs, linking)
- Repository naming conventions
- Complete workflow example
- Commands by role (PO, PM, Dev, Tester, DevOps)
- Common mistakes to avoid
- Success checklist

**Use this when:** You need quick commands or want to verify naming conventions.

---

### 2. Issue Template Guide
**File:** [issue-template-guide.md](issue-template-guide.md)

**Complete Reference for:**
- All four issue templates (User Story, Task, Bug, Spike)
- Template structure and usage
- Multi-repo naming conventions
- When to use each template
- Best practices per template type
- Issue lifecycle by type
- GitHub CLI commands for issue creation

**Use this when:** Creating issues, customizing templates, or understanding issue lifecycle.

---

### 3. Workflow Summary
**File:** [WORKFLOW-SUMMARY.md](WORKFLOW-SUMMARY.md)

**Comprehensive Guide for:**
- Multi-repository structure and architecture
- Complete workflow from planning to deployment
- GitHub integration (issues, projects, branches, PRs)
- Practical examples with real commands
- Phase-by-phase breakdown (Planning → Dev → Testing → Deploy)

**Use this when:** You're learning the workflow or need detailed examples.

---

### 4. GitHub Project Management Guide
**File:** [github-project-management-guide.md](github-project-management-guide.md)

**Detailed Documentation for:**
- GitHub Projects setup (Kanban boards)
- Issue templates and management
- Label strategy and organization
- Branch naming conventions
- Pull request workflow
- CI/CD integration
- GitHub CLI commands

**Use this when:** Setting up GitHub Projects or configuring automation.

---

### 5. Approval Workflow Guide
**File:** [approval-workflow-guide.md](approval-workflow-guide.md)

**Implementation Guide for:**
- Manual approval gates
- GitHub Issues for approvals
- Slack integration for notifications
- Hybrid approval workflows
- Quality checklists per role
- Team collaboration patterns

**Use this when:** Implementing review/approval processes between workflow phases.

---

### 6. GitHub Integration Summary
**File:** [github-integration-summary.md](github-integration-summary.md)

**Overview of:**
- GitHub integration best practices
- How agents use GitHub tools
- Sprint and backlog management
- Cross-repository collaboration

**Use this when:** Understanding how everything fits together.

---

## 🏗️ Multi-Repository Workflow

### Core Concepts

**Parent Repository** (`project-management`):
- All issues, user stories, tasks
- Sprint milestones
- GitHub Project boards
- Backlog management

**Child Repositories** (`services/*`):
- Feature branches
- Pull requests
- Code and tests
- CI/CD pipelines

### Key Conventions

```bash
# Issues (Parent repo only)
[action-manager] Add search to header
[ecommerce-stats] Implement dashboard

# Branches (Child repos only)
feature/action-manager-130-search-api
bugfix/ecommerce-stats-201-chart-error

# Commits (Child repos)
[action-manager] Implement search API
Related to hvantran/project-management#130

# PRs (Child repos)
Closes hvantran/project-management#130
```

---

## 🚀 Getting Started

### For New Team Members

1. **Read:** [multi-repo-cheatsheet.md](multi-repo-cheatsheet.md) (10 min)
2. **Review:** [WORKFLOW-SUMMARY.md](WORKFLOW-SUMMARY.md) (30 min)
3. **Practice:** Create a test issue and go through the workflow

### For Sprint Planning

1. Use `@product-owner` to create sprint and user stories
2. Reference: [WORKFLOW-SUMMARY.md - Sprint Planning](WORKFLOW-SUMMARY.md#phase-1-sprint-planning-po--ba--pm--ta)

### For Development

1. Follow [multi-repo-cheatsheet.md - Developer Commands](multi-repo-cheatsheet.md#developer-child-repo)
2. Use `@developer` agent for TDD/BDD implementation

### For QA/Testing

1. Follow [WORKFLOW-SUMMARY.md - Testing Phase](WORKFLOW-SUMMARY.md#phase-3-testing-tester--developer)
2. Use `@tester` agent for BDD scenario execution

---

## 📖 Agent Documentation

All custom agents are defined in [`../agents/`](../agents/README.md):

- [Product Owner](../agents/product-owner.agent.md)
- [Business Analyst](../agents/business-analyst.agent.md)
- [Project Manager](../agents/project-manager.agent.md)
- [Technical Architect](../agents/technical-architect.agent.md)
- [Developer](../agents/developer.agent.md)
- [Tester](../agents/tester.agent.md)
- [DevOps Engineer](../agents/devops-engineer.agent.md)
- [Workflow Orchestrator](../agents/workflow-orchestrator.agent.md)

---

## 🔄 Workflow Diagram

```
SPRINT PLANNING (Parent Repo)
   │
   ├─ @product-owner: User stories → Issues
   ├─ @business-analyst: Requirements → Sub-issues
   ├─ @technical-architect: Architecture → Discussions
   └─ @project-manager: Tasks → Sprint board
           ↓
DEVELOPMENT (Child Repos)
   │
   ├─ @developer: Branch → TDD/BDD → PR
   └─ @technical-architect: Review
           ↓
TESTING (Child Repos)
   │
   ├─ @tester: Execute BDD → Review PR
   └─ @developer: Fix issues
           ↓
DEPLOYMENT (Child Repos)
   │
   ├─ @devops-engineer: Deploy → Monitor
   └─ @project-manager: Update parent issues
```

---

## 🛠️ Maintenance

### Updating Documentation

When updating guides:
1. Edit the relevant `.md` file in this directory
2. Update cross-references if file names change
3. Update this README if adding/removing guides
4. Commit with: `[project] Update [guide-name] documentation`

### Adding New Guides

1. Create new `.md` file in this directory
2. Follow consistent structure (see existing guides)
3. Add entry to this README
4. Link from relevant agent files if applicable

---

## 📞 Quick Links

- **Agents Directory:** [`../agents/`](../agents/README.md)
- **Instructions:** [`../instructions/`](../instructions/)
- **Issue Templates:** [`../ISSUE_TEMPLATE/`](../ISSUE_TEMPLATE/)
- **Workflows:** [`../workflows/`](../workflows/)

---

**Questions?** Check the [multi-repo-cheatsheet.md](multi-repo-cheatsheet.md) or invoke `@workflow-orchestrator` for guidance.
