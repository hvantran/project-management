# Custom Agents

This directory contains custom GitHub Copilot agents for the multi-agent software development workflow.

## 📁 Directory Structure

```
.github/
├── agents/          ← You are here (Agent definitions only)
│   ├── *.agent.md   ← Agent files
│   └── README.md    ← This file
└── guides/          ← Documentation and guides
    ├── *.md         ← Guide documents
    └── README.md    ← Guide index
```

## 🤖 Available Agents

### Role-Based Agents (8)

| Agent | File | Purpose |
|-------|------|---------|
| **Product Owner** | [product-owner.agent.md](product-owner.agent.md) | Define product vision, create user stories with BDD acceptance criteria |
| **Business Analyst** | [business-analyst.agent.md](business-analyst.agent.md) | Detail requirements, create BDD scenarios (Gherkin) |
| **Project Manager** | [project-manager.agent.md](project-manager.agent.md) | Plan execution, break down tasks, track sprint progress |
| **Technical Architect** | [technical-architect.agent.md](technical-architect.agent.md) | Design architecture, create ADRs, set up BDD/TDD frameworks |
| **Developer** | [developer.agent.md](developer.agent.md) | Implement using TDD/BDD, create branches, manage PRs |
| **Tester** | [tester.agent.md](tester.agent.md) | Execute BDD scenarios, validate quality, report bugs |
| **DevOps Engineer** | [devops-engineer.agent.md](devops-engineer.agent.md) | Deploy, monitor, manage infrastructure and CI/CD |
| **Workflow Orchestrator** | [workflow-orchestrator.agent.md](workflow-orchestrator.agent.md) | Coordinate multi-agent workflows with approval gates |

## 🚀 Quick Start

### Invoke an Agent

**Use lowercase hyphenated names:**

```
@product-owner Create Sprint 6 and prepare user stories
@developer Help me implement task #130 using TDD
@tester Validate PR #45 and execute BDD scenarios
@workflow-orchestrator Implement search feature with approval gates
```

### Common Patterns

**Sprint Preparation (PO only):**
```
@product-owner Create Sprint 6 (April 7-21) with user stories for:
- [action-manager] Search actions by name
- [ecommerce-stats] Sales analytics dashboard
```

**Full Feature Workflow (Orchestrator):**
```
@workflow-orchestrator Implement OAuth2 authentication in action-manager-app
```

**Single Task (Direct agent):**
```
@developer Implement task #130 using TDD approach
```

## 📚 Documentation

All guides and documentation are in [`../guides/`](../guides/README.md):

- [Multi-Repo Cheat Sheet](../guides/multi-repo-cheatsheet.md) - Quick reference for daily work
- [Workflow Summary](../guides/WORKFLOW-SUMMARY.md) - Complete workflow guide
- [GitHub Project Management](../guides/github-project-management-guide.md) - GitHub integration details
- [Approval Workflow Guide](../guides/approval-workflow-guide.md) - Review/approval processes

## 🏗️ Multi-Repository Structure

This workspace uses a **parent-child repository** structure:

- **Parent repo** (`project-management`): Issues, backlog, sprint board
- **Child repos** (`services/*`): Code, branches, pull requests

**Key Conventions:**
- Issues: `[repo-name] Issue title` (in parent repo)
- Branches: `feature/repo-name-issue-description` (in child repos)
- Commits: `[repo-name] Commit message` (in child repos)
- PRs: Link to parent issue with `Closes hvantran/project-management#123`

See [multi-repo-cheatsheet.md](../guides/multi-repo-cheatsheet.md) for complete details.

## 🛠️ Extending Agents

To create a new agent:

1. Create `new-agent.agent.md` in this directory
2. Follow the structure of existing agents:
   ```yaml
   ---
   description: "Short description of the agent's purpose"
   name: "new-agent"  # Use lowercase-hyphenated to match filename
   tools: ["githubRepo", "run_in_terminal"]  # Add tools based on role
   ---
   
   # New Agent Name
   
   You are a [role] responsible for [purpose]...
   
   ## Your Responsibilities
   [List responsibilities]
   
   ## Role Boundaries
   [Define what this agent does and doesn't do]
   
   [Agent content...]
   ```
3. Invoke as: `@new-agent <your request>`
4. Document the agent in this README
5. Add usage examples in [`../guides/`](../guides/)

**Important:** The `name` field must match the filename pattern (without `.agent.md`) for proper discovery.

## 📖 More Information

- **Agent Customization**: See VS Code Copilot agent documentation
- **Workflow Details**: Check [`../guides/WORKFLOW-SUMMARY.md`](../guides/WORKFLOW-SUMMARY.md)
- **GitHub Integration**: See [`../guides/github-project-management-guide.md`](../guides/github-project-management-guide.md)

---

**Need help?** Invoke `@workflow-orchestrator` for guided workflow or check the [guides](../guides/README.md).
