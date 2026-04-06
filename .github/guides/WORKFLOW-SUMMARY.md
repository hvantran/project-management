# Multi-Agent Workflow with GitHub Integration - Complete Summary

## 🎯 Quick Start: Working with Role-Based Agents

This document provides a complete overview of how to use the 8 role-based agents with GitHub for managing sprints, backlogs, and development workflows.

---

## 📚 Table of Contents

1. [Multi-Repository Structure](#multi-repository-structure)
2. [Agent Overview](#agent-overview)
3. [Complete Workflow](#complete-workflow)
4. [GitHub Integration](#github-integration)
5. [Practical Examples](#practical-examples)
6. [Quick Reference](#quick-reference)

---

## Multi-Repository Structure

### 🏗️ Repository Architecture

Your workspace uses a **parent-child repository structure**:

```
project-management/           ← Parent repo (GitHub board, backlog, sprint management)
├── .github/
│   ├── agents/              ← Custom agents
│   ├── ISSUE_TEMPLATE/      ← Issue templates
│   └── workflows/           ← CI/CD workflows
├── services/                ← Git submodules
│   ├── action-manager-app/
│   ├── ecommerce-stats-app/
│   ├── external-endpoint-collector/
│   ├── spring-kafka-notifier/
│   └── template-management-app/
├── base-platform/
├── account-platform/
├── parent-pom/
└── deployment-new/
```

### 📝 Multi-Repo Conventions

#### **Issue Naming Convention**
All issues in parent repo must have repository prefix:

```
Format: [repo-name] Issue title

Examples:
✅ [action-manager] Add search functionality to header
✅ [ecommerce-stats] Implement sales analytics dashboard
✅ [endpoint-collector] Add health check endpoint
✅ [kafka-notifier] Configure retry mechanism
✅ [template-manager] Add email template versioning
✅ [parent-pom] Update Spring Boot version to 3.2.1
✅ [base-platform] Add common logging framework
✅ [deployment] Configure Kubernetes ingress
```

#### **Branch Naming Convention**
Branches are created in child repositories:

```
Format: feature/[repo-name]-[issue-number]-[description]

Examples:
feature/action-manager-110-search-header
feature/ecommerce-stats-125-sales-dashboard
bugfix/endpoint-collector-201-timeout-error
hotfix/kafka-notifier-305-message-loss
```

#### **Commit Message Convention**
Follow the commit conventions specified in `.github/instructions/git-commit-conventions.instructions.md`:

```
Format: [repository-name] <subject>

Examples:
[action-manager] Implement search API endpoint
[ecommerce-stats] Add analytics dashboard component
[endpoint-collector] Fix timeout handling
```

#### **Pull Request Workflow**
PRs are created in child repositories but referenced from parent issues:

```
1. Developer creates branch in child repo (e.g., action-manager-app)
2. Developer implements feature
3. Developer creates PR in child repo
4. PR title references parent issue: "Implement search (closes hvantran/project-management#110)"
5. After PR merged, developer updates parent issue
```

### 🎯 Centralized Management

**Parent Repository (project-management) manages:**
- ✅ GitHub Project board (Sprint Kanban)
- ✅ Backlog (all user stories and tasks)
- ✅ Milestones (Sprint 1, Sprint 2, etc.)
- ✅ Epic tracking
- ✅ Cross-service coordination

**Child Repositories (services/*) manage:**
- ✅ Feature branches
- ✅ Pull requests
- ✅ Code reviews
- ✅ CI/CD pipelines
- ✅ Service-specific deployments

### 📊 Example: Multi-Repo Issue Flow

```
Parent Repo (project-management):
├─ Issue #110: [action-manager] Add search to header
│  ├─ Labels: user-story, action-manager, sprint-6
│  ├─ Milestone: Sprint 6
│  └─ On Project Board: "Sprint 6 Board"
│
├─ Issue #111: [action-manager] [REQ] Search - Detailed requirements
│  └─ Labels: requirements, action-manager
│
├─ Task #112: [action-manager] Implement SearchBox component
│  └─ Assigned to: @frontend-dev
│
└─ Task #113: [action-manager] Implement search API endpoint
   └─ Assigned to: @backend-dev

Child Repo (action-manager-app):
├─ Branch: feature/action-manager-112-searchbox-component
├─ PR #25: "Add SearchBox component (closes hvantran/project-management#112)"
├─ Branch: feature/action-manager-113-search-api
└─ PR #26: "Add search API (closes hvantran/project-management#113)"
```

---

## Agent Overview

### 8 Role-Based Agents + 1 Orchestrator

| Agent | Primary Role | GitHub Tools | Key Outputs |
|-------|-------------|--------------|-------------|
| **Product Owner** | Define vision, prioritize features | Issues (user stories), Milestones, Projects | User stories with BDD acceptance criteria |
| **Business Analyst** | Detail requirements, bridge business-tech | Sub-issues, Comments, Discussions | BDD scenarios, EARS requirements |
| **Project Manager** | Plan execution, track progress | Projects (sprint boards), Tasks, Milestones | Sprint plans, task breakdowns |
| **Technical Architect** | Design systems, make tech decisions | Discussions (ADRs), Wiki, Spikes | Architecture docs, ADRs, tech specs |
| **Developer** | Implement with TDD/BDD | Branches, Pull Requests, Commits | Code, tests, step definitions |
| **Tester** | Validate quality, execute BDD scenarios | PR Reviews, Bug Issues, QA reports | Test results, bug reports, QA sign-off |
| **DevOps Engineer** | Deploy, monitor, maintain infrastructure | Releases, Actions (CI/CD), Environments | Deployments, monitoring, runbooks |
| **Workflow Orchestrator** | Coordinate multi-agent flows | Project automation, Labels, Reviews | Workflow guidance, handoff coordination |

---

## Complete Workflow

### Phase 1: Sprint Planning (PO + BA + PM + TA)

```
┌─────────────────┐
│  Product Owner  │  Creates user stories with BDD acceptance criteria
└────────┬────────┘
         │ Issue #1: "As a user, I want to search products"
         │ Given-When-Then scenarios
         ▼
┌─────────────────┐
│ Business Analyst│  Adds detailed BDD scenarios and requirements
└────────┬────────┘
         │ Sub-issue #2: Detailed Gherkin scenarios
         │ Data models, edge cases
         ▼
┌─────────────────┐
│Technical Architect│ Creates architecture design
└────────┬────────┘
         │ Discussion: ADR-001 (Architecture Decision)
         │ Wiki: System diagrams
         ▼
┌─────────────────┐
│ Project Manager │  Breaks down into tasks, creates sprint board
└────────┬────────┘
         │ Task #3: "Implement search API"
         │ Task #4: "Create search UI"
         │ Sprint Board: GitHub Project
         ▼
     [Sprint Ready]
```

**GitHub Actions:**
```bash
# PO: Create user story
gh issue create \
  --title "As a user, I want to search products by category" \
  --label "user-story,priority:high" \
  --milestone "Sprint 5"

# BA: Add detailed requirements
gh issue create \
  --title "[REQ] Product search - Detailed scenarios" \
  --body "Parent: #1" \
  --label "requirements"

# TA: Document architecture decision
gh api repos/:owner/:repo/discussions \
  -f title="ADR-001: Search architecture" \
  -f body="$(cat adr.md)"

# PM: Break into tasks and create sprint board
gh project create --title "Sprint 5 Board"
gh issue create --title "[TASK] Implement search API" --body "Parent: #1"
gh issue create --title "[TASK] Create search UI" --body "Parent: #1"
```

---

### Phase 2: Development (Developer + TA)

```
┌─────────────────┐
│   Developer     │  Implements using TDD
└────────┬────────┘
         │ 1. Create branch: feature/search-api-#3
         │ 2. RED: Write failing tests
         │ 3. GREEN: Make tests pass
         │ 4. REFACTOR: Optimize code
         │ 5. Create Pull Request
         ▼
     [Code Ready for Review]
```

**TDD Cycle Example:**
```bash
# Step 1: Create branch from issue
gh issue develop 3 --checkout
# Creates: feature/action-manager-3-implement-search-api

# Step 2: RED - Write failing test
cat > SearchServiceTest.java << 'EOF'
@Test
void shouldReturnProductsWhenSearching() {
    // Arrange
    String query = "laptop";
    
    // Act
    List<Product> results = searchService.search(query);
    
    // Assert
    assertThat(results).isNotEmpty();
}
EOF

mvn test  # FAILS ❌
git commit -m "[action-manager] Add search test (RED phase)"

# Step 3: GREEN - Implement minimum code to pass
cat > SearchService.java << 'EOF'
public List<Product> search(String query) {
    return productRepository.findByNameContaining(query);
}
EOF

mvn test  # PASSES ✅
git commit -m "[action-manager] Implement basic search (GREEN phase)"

# Step 4: REFACTOR - Optimize
# Add caching, indexing, etc.
mvn test  # STILL PASSES ✅
git commit -m "[action-manager] Add caching to search (REFACTOR phase)"

# Step 5: Create PR
gh pr create \
  --title "Implement product search API" \
  --body "Closes #3" \
  --reviewer @tech-lead,@tester
```

**BDD Step Definitions:**
```java
// Developer implements step definitions for BA's scenarios
@Given("the product catalog contains {int} products")
public void productCatalogContains(int count) {
    testDataService.createProducts(count);
}

@When("I search for {string}")
public void searchFor(String query) {
    searchResults = searchService.search(query);
}

@Then("I should see {int} results")
public void shouldSeeResults(int expectedCount) {
    assertThat(searchResults).hasSize(expectedCount);
}
```

---

### Phase 3: Testing (Tester + Developer)

```
┌─────────────────┐
│     Tester      │  Validates implementation
└────────┬────────┘
         │ 1. Review Pull Request
         │ 2. Execute BDD scenarios
         │ 3. Run manual tests
         │ 4. Report bugs (if any)
         │ 5. Approve PR when pass
         ▼
   [Quality Validated]
```

**QA Workflow:**
```bash
# Tester: Review PR
gh pr view 12 --json title,body,commits

# Run BDD tests
mvn verify -Pcucumber
# Scenarios: 5 passing, 0 failing

# Execute manual test cases
# ... perform exploratory testing ...

# Option A: Approve if all tests pass
gh pr review 12 --approve --body "✅ QA APPROVED

**Test Results:**
- BDD Scenarios: 5/5 passing
- Manual Tests: All test cases passed
- Performance: Response time < 200ms
- No regressions found

Ready for merge and deployment."

# Option B: Request changes if bugs found
gh issue create \
  --title "[BUG] Search returns duplicate results" \
  --label "bug,priority:high" \
  --body "$(cat bug-report.md)"

gh pr review 12 --request-changes --body "Found issue #45, please fix"
```

---

### Phase 4: Deployment (DevOps + PM)

```
┌─────────────────┐
│  DevOps Engineer│  Deploys to production
└────────┬────────┘
         │ 1. Merge approved PR
         │ 2. Create release
         │ 3. Deploy via CI/CD
         │ 4. Monitor deployment
         │ 5. Update issue status
         ▼
    [Feature Live]
```

**Deployment Workflow:**
```bash
# Developer: Merge approved PR
gh pr merge 12 --squash --delete-branch

# DevOps: Create release
gh release create v1.5.0 \
  --title "Sprint 5 Release - Product Search" \
  --generate-notes

# CI/CD automatically triggers deployment
gh run list --workflow=deploy.yml
gh run view 123456 --log

# Monitor deployment
kubectl rollout status deployment/action-manager

# Update issue status
gh issue close 3 --comment "✅ Deployed to production in v1.5.0"
gh issue close 1 --comment "✅ Feature complete and deployed by @devops"
```

---

### Phase 5: Sprint Review (All Roles)

```
┌─────────────────┐
│   PM Reviews    │  Generate sprint metrics
│   PO Accepts    │  Demo to stakeholders
│   Team Retros   │  Continuous improvement
└─────────────────┘
```

**Sprint Closure:**
```bash
# PM: Generate sprint report
gh issue list --milestone "Sprint 5" --json number,title,state,assignees

# Output:
# Completed: 8 stories, 23 tasks
# In Progress: 2 tasks (moved to Sprint 6)
# Velocity: 34 story points

# PO: Close sprint milestone
gh api repos/:owner/:repo/milestones/5 -X PATCH -f state=closed

# PM: Archive sprint board
gh project close PROJECT_NUMBER

# Team: Retrospective (record in discussions)
gh api repos/:owner/:repo/discussions \
  -f title="Sprint 5 Retrospective" \
  -f body="What went well, what to improve..."
```

---

## GitHub Integration

### 1. Backlog Management (Issues)

**Issue Hierarchy:**
```
Epic (Milestone)
├── User Story (Issue with label: user-story)
│   ├── Requirements (Sub-issue with label: requirements)
│   ├── Task 1 (Issue with label: task, references parent)
│   ├── Task 2 (Issue with label: task, references parent)
│   └── Bug (Issue with label: bug, references parent)
```

**Labels Strategy:**
```yaml
Priority Labels:
  - priority:critical  # Must fix immediately
  - priority:high      # Sprint goal items
  - priority:medium    # Nice to have
  - priority:low       # Backlog

Type Labels:
  - user-story         # PO created
  - requirements       # BA detailed specs
  - task               # PM broken down work
  - spike              # TA research
  - bug                # Tester found issue
  - deployment         # DevOps task

Status Labels:
  - status:ready-for-ba   # PO approved
  - status:ready-for-dev  # Requirements complete
  - status:in-progress    # Developer working
  - status:in-review      # Under QA review
  - status:blocked        # Dependency blocker

Component Labels:
  - action-manager        # Service-specific
  - ecommerce-stats
  - endpoint-collector
  - kafka-notifier
  - template-manager
```

---

### 2. Sprint Board (GitHub Projects)

**Board Structure:**
```
┌──────────┬──────────┬──────────────┬──────────┬──────────┐
│ Backlog  │ To Do    │ In Progress  │ Review   │   Done   │
├──────────┼──────────┼──────────────┼──────────┼──────────┤
│ Story #1 │ Task #3  │ Task #5      │ PR #12   │ Task #2  │
│ Story #2 │ Task #4  │ Task #6      │ PR #13   │ Task #7  │
│ Epic #1  │          │ Bug #45      │          │ Story #3 │
│          │          │              │          │ Task #8  │
└──────────┴──────────┴──────────────┴──────────┴──────────┘
```

**Setup Commands:**
```bash
# Create project board
gh project create --owner USER --title "Sprint 5 Board"

# Add issues to board
gh project item-add PROJECT_NUMBER --url "https://github.com/USER/REPO/issues/1"

# Move issues between columns
gh project item-edit \
  --project-id PROJECT_ID \
  --field-id STATUS_FIELD_ID \
  --field-value "In Progress"
```

**Automation Example:**
``yaml
# .github/workflows/project-automation.yml
name: Auto-update Project Board

on:
  pull_request:
    types: [opened, ready_for_review, closed]

jobs:
  update-board:
    runs-on: ubuntu-latest
    steps:
      - name: Move to In Progress when PR opened
        if: github.event.action == 'opened'
        run: |
          gh project item-edit --field-value "In Progress"
          
      - name: Move to Review when PR ready
        if: github.event.action == 'ready_for_review'
        run: |
          gh project item-edit --field-value "Review"
          
      - name: Move to Done when PR merged
        if: github.event.action == 'closed' && github.event.pull_request.merged
        run: |
          gh project item-edit --field-value "Done"
```

---

### 3. Branch Management

**Branch Naming Convention:**
```
feature/[service]-[issue]-[description]
  └─ feature/action-manager-42-search-api

bugfix/[service]-[issue]-[description]
  └─ bugfix/action-manager-201-duplicate-results

hotfix/[service]-[description]
  └─ hotfix/kafka-notifier-security-patch

spike/[issue]-[description]
  └─ spike/401-elasticsearch-evaluation

release/[version]
  └─ release/v1.5.0
```

**Branch Workflow:**
```bash
# Create branch from issue (automatic naming)
gh issue develop 42 --checkout

# Manual branch creation
git checkout -b feature/action-manager-42-search-api

# Push and create PR
git push -u origin HEAD
gh pr create --fill

# Link to parent issue
gh issue comment 1 --body "Implementation in progress: #PR_NUMBER"
```

---

### 4. Technical Notes (Wiki + Discussions)

**Documentation Strategy:**

| Type | Location | Owner | Format |
|------|----------|-------|--------|
| Architecture Decisions | GitHub Discussions (ADR category) | Technical Architect | ADR template |
| API Documentation | Wiki  | Developer | OpenAPI/Markdown |
| System Architecture | Wiki | Technical Architect | Diagrams + Markdown |
| Onboarding Guides | Wiki | PM | Step-by-step guides |
| Meeting Notes | Discussions | PM | Markdown |
| Technical Spikes | Issues with `spike` label | TA | Issue template |

**ADR Example:**
```bash
# TA: Create architecture decision record
gh api repos/:owner/:repo/discussions \
  -f title="ADR-001: Use PostgreSQL for search indexing" \
  -f body="# ADR-001: Search Database Choice

## Status
Accepted

## Context
Need full-text search capability for 100K+ products with complex filters.

## Decision
Use PostgreSQL with GIN indexes instead of Elasticsearch.

## Consequences
### Positive
- ✅ Reduced infrastructure complexity (one less service)
- ✅ ACID guarantees for search consistency
- ✅ Team has PostgreSQL expertise

### Negative
- ❌ Potentially slower than Elasticsearch for very large datasets
- ❌ Limited natural language processing capabilities

## Alternatives Considered
1. Elasticsearch - Better search features, more complexity
2. MongoDB - Document model good fit, less mature FTS

## Review Date
2026-Q3 - Reassess if product catalog exceeds 1M items"
```

---

## Practical Examples

### Example 1: Complete User Story Flow

**Scenario:** Add product favoriting feature

```bash
# Step 1: PO creates user story
gh issue create \
  --title "As a user, I want to favorite products for quick access" \
  --label "user-story,priority:high" \
  --milestone "Sprint 6" \
  --body "### User Story
As a logged-in user,
I want to favorite products,
So that I can quickly find them later.

### Acceptance Criteria

**Scenario 1: Add product to favorites**
Given I am viewing a product
When I click the 'favorite' button
Then the product is added to my favorites list
And the button shows 'favorited' state

**Scenario 2: Remove from favorites**
Given I have favorited a product
When I click the 'favorited' button
Then the product is removed from favorites
And the button returns to 'favorite' state

**Scenario 3: View favorites list**
Given I have 5 favorited products
When I navigate to 'My Favorites'
Then I see all 5 products
And they are sorted by most recently added"

# Issue #50 created

# Step 2: BA adds detailed requirements
gh issue create \
  --title "[REQ] Product favoriting - Technical requirements" \
  --label "requirements" \
  --body "Parent: #50

### Detailed BDD Scenarios

\`\`\`gherkin
Feature: Product Favoriting

  Scenario: Add product to favorites
    Given I am logged in as 'user@example.com'
    And I am viewing product 'Laptop Pro 15'
    And the product is not in my favorites
    When I click the 'favorite' button
    Then the API should receive POST /api/users/me/favorites
    And the response status should be 201
    And the favorite button should show 'favorited' icon
    And the favorites count should increment by 1

  Scenario Outline: Favorites persistence
    Given I have <initial> favorited products
    When I favorite product '<product>'
    Then my favorites list should contain <final> products
    And product '<product>' should appear at position <position>

    Examples:
      | initial | product      | final | position |
      | 0       | Laptop       | 1     | 1        |
      | 1       | Mouse        | 2     | 1        |
      | 5       | Keyboard     | 6     | 1        |
\`\`\`

### Technical Requirements (EARS)

1. WHEN a logged-in user clicks the favorite button, THE SYSTEM SHALL persist the favorite relationship in the database
2. WHEN a user views their favorites, THE SYSTEM SHALL return favorites sorted by creation timestamp descending
3. IF a user attempts to favorite while not logged in, THEN THE SYSTEM SHALL return HTTP 401
4. THE SYSTEM SHALL support a maximum of 500 favorites per user

### Data Model

\`\`\`json
{
  \"userId\": \"uuid\",
  \"productId\": \"uuid\",
  \"createdAt\": \"timestamp\",
  \"metadata\": {
    \"source\": \"web | mobile\"
  }
}
\`\`\`

### Edge Cases
1. Double-click favorite button (idempotency)
2. Favoriting deleted product
3. Network failure during favorite action
4. Concurrent favorites from multiple devices"

# Issue #51 created

# Step 3: TA documents architecture
gh api repos/hvantran/project-management/discussions \
  -f title="ADR-002: Favorites data model" \
  -f body="# ADR-002: Favorites Data Model

## Decision
Use junction table with composite primary key (user_id, product_id).

## Rationale
- Ensures uniqueness (can't favorite same product twice)
- Fast lookups for both user favorites and product popularity
- Simple to query and maintain

## Schema
\`\`\`sql
CREATE TABLE user_favorites (
  user_id UUID REFERENCES users(id),
  product_id UUID REFERENCES products(id),
  created_at TIMESTAMP DEFAULT NOW(),
  source VARCHAR(10),
  PRIMARY KEY (user_id, product_id)
);

CREATE INDEX idx_favorites_user ON user_favorites(user_id, created_at DESC);
CREATE INDEX idx_favorites_product ON user_favorites(product_id);
\`\`\`"

# Step 4: PM breaks into tasks
gh issue create \
  --title "[TASK] Create favorites database schema" \
  --label "task,database" \
  --milestone "Sprint 6" \
  --assignee @developer1 \
  --body "Parent: #50

Create migration for user_favorites table per ADR-002."

gh issue create \
  --title "[TASK] Implement favorites API endpoints" \
  --label "task,backend" \
  --milestone "Sprint 6" \
  --assignee @developer2 \
  --body "Parent: #50

Implement:
- POST /api/users/me/favorites
- DELETE /api/users/me/favorites/{productId}
- GET /api/users/me/favorites

With TDD and BDD step definitions."

gh issue create \
  --title "[TASK] Add favorite button to product page" \
  --label "task,frontend" \
  --milestone "Sprint 6" \
  --assignee @developer3 \
  --body "Parent: #50

Add favorite button UI component with state management."

# Tasks #52, #53, #54 created

# Step 5: Developer implements (task #53)
gh issue develop 53 --checkout
# Creates branch: feature/action-manager-53-implement-favorites-api

# TDD: RED
cat > FavoritesServiceTest.java << 'EOF'
@Test
void shouldAddProductToFavorites() {
    // Arrange
    UUID userId = UUID.randomUUID();
    UUID productId = UUID.randomUUID();
    
    // Act
    Favorite result = favoritesService.addFavorite(userId, productId);
    
    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getUserId()).isEqualTo(userId);
    assertThat(result.getProductId()).isEqualTo(productId);
}
EOF

mvn test  # FAILS

# TDD: GREEN
# ... implement FavoritesService ...
mvn test  # PASSES

# BDD: Step definitions
cat > FavoritesStepDefs.java << 'EOF'
@Given("I am viewing product {string}")
public void viewingProduct(String productName) {
    currentProduct = productService.findByName(productName);
}

@When("I click the 'favorite' button")
public void clickFavoriteButton() {
    response = restTemplate.postForEntity(
        "/api/users/me/favorites",
        Map.of("productId", currentProduct.getId()),
        Favorite.class
    );
}

@Then("the response status should be {int}")
public void responseStatusShouldBe(int expectedStatus) {
    assertThat(response.getStatusCodeValue()).isEqualTo(expectedStatus);
}
EOF

# Commit and create PR
git add .
git commit -m "[action-manager] Implement favorites API (TDD + BDD)

Implemented favorites service with:
- POST /api/users/me/favorites - Add favorite
- DELETE /api/users/me/favorites/{id} - Remove favorite
- GET /api/users/me/favorites - List favorites

TDD: All tests passing (coverage 92%)
BDD: Step definitions for 3 scenarios implemented

Related to hvantran/project-management#50
Closes hvantran/project-management#53"

git push
gh pr create \
  --title "Implement favorites API endpoints" \
  --body "Closes #53" \
  --reviewer @tech-lead,@tester

# PR #60 created

# Step 6: Tester validates
gh pr checkout 60
mvn verify -Pcucumber
# All tests passing

gh pr review 60 --approve --body "✅ QA APPROVED

**BDD Tests:** 3/3 scenarios passing
**Unit Tests:** 25/25 passing, 92% coverage
**API Tests:** All endpoints working correctly

Tested edge cases:
✅ Double-click prevention (idempotent)
✅ Unauthorized access returns 401
✅ Invalid product returns 404

Ready for merge."

# Step 7: Developer merges
gh pr merge 60 --squash --delete-branch

# Step 8: DevOps deploys
# (CI/CD automatically triggers)

# Step 9: Close tasks and user story
gh issue close 52 --comment "✅ Schema deployed"
gh issue close 53 --comment "✅ API implemented and merged"
gh issue close 54 --comment "✅ UI completed"
gh issue close 50 --comment "✅ Feature complete and deployed to production

Deployed in release v1.6.0
All acceptance criteria validated by QA"
```

---

### Example 2: Bug Fix Flow

```bash
# Tester finds bug
gh issue create \
  --title "[BUG] Search returns duplicate products" \
  --label "bug,priority:high" \
  --body "### Bug Description
Search for 'laptop' returns duplicate products in results.

### Steps to Reproduce
1. Navigate to /products
2. Enter 'laptop' in search box
3. Click search

### Expected
Each product appears once

### Actual
Product 'Laptop Pro' appears 3 times

### Environment
- Service: action-manager-app
- Version: v1.5.0
- Browser: Chrome 90

### Impact
- Severity: High
- Affects all users" \
  --assignee @developer

# Issue #201 created

# Developer investigates
gh issue develop 201 --checkout

# Fix bug with TDD
cat > SearchServiceTest.java << 'EOF'
@Test
void shouldReturnUniqueProducts() {
    List<Product> results = searchService.search("laptop");
    Set<UUID> uniqueIds = results.stream()
        .map(Product::getId)
        .collect(Collectors.toSet());
    
    assertThat(results).hasSize(uniqueIds.size());  // No duplicates
}
EOF

# Fix implementation
# ...add DISTINCT to SQL query...

# Create PR
gh pr create \
  --title "[BUGFIX] Remove duplicate products from search results" \
  --body "Closes #201

**Root Cause:** SQL query was joining products table twice without DISTINCT

**Fix:** Added DISTINCT clause to query

**Testing:**
- ✅ New test case for duplicate prevention
- ✅ All existing tests still passing
- ✅ Manual verification complete" \
  --label "bugfix" \
  --reviewer @tester

# Tester approves
gh pr review --approve

# Merge and deploy
gh pr merge --squash
gh issue close 201 --comment "✅ Fixed and deployed to production"
```

---

## Quick Reference

### Daily Commands

```bash
# Morning standup - see your work (PARENT REPO)
gh issue list \
  --repo hvantran/project-management \
  --assignee @me \
  --state open

# Check sprint progress (PARENT REPO)
gh project view PROJECT_NUMBER --owner hvantran

# Check PRs needing review (CHILD REPOS)
cd services/action-manager-app
gh pr list --author @me
gh pr list --review-requested @me

# Check CI/CD status (CHILD REPOS)
gh run list --limit 5
```

### Agent Commands

```bash
# Product Owner commands (creates issues in PARENT REPO)
gh issue create \
  --repo hvantran/project-management \
  --title "[action-manager] User story title" \
  --label user-story

# Business Analyst commands (creates sub-issues in PARENT REPO)
gh issue comment ISSUE \
  --repo hvantran/project-management \
  --body "Detailed scenarios..."

# Project Manager commands (manages sprint in PARENT REPO)
gh project create --owner hvantran --title "Sprint X"
gh issue create \
  --repo hvantran/project-management \
  --title "[action-manager] Task title" \
  --label task

# Developer commands (works in CHILD REPO)
cd services/action-manager-app
git checkout -b feature/action-manager-130-feature
git commit -m "[action-manager] Implement feature

Related to hvantran/project-management#130"
gh pr create --title "Feature implementation" \
  --body "Closes hvantran/project-management#130"

# Tester commands (review PRs in CHILD REPOS)
cd services/action-manager-app
gh pr review PR --comment  # Add test results
gh pr review PR --approve  # QA sign-off

# DevOps commands (deploy from CHILD REPOS)
cd services/action-manager-app
gh release create v1.X.0  # Create release
gh run view RUN_ID --log  # Monitor deployment
```

### Commit Message Format (MANDATORY)

```bash
# Format for ALL repositories
[repository-name] <subject>

<body>

Related to hvantran/project-management#<issue>
Closes hvantran/project-management#<issue>
```

**Examples:**
```
[action-manager] Implement product search API

TDD implementation with full test coverage.
BDD step definitions for 5 scenarios completed.

Related to hvantran/project-management#50
Closes hvantran/project-management#53
```

```
[ecommerce-stats] Add sales analytics dashboard

Implemented React dashboard with Chart.js.
Real-time data updates via WebSocket.

Related to hvantran/project-management#75
```

```
[parent-pom] Update Spring Boot version to 3.2.1

Upgrade Spring Boot across all services.
Update dependency management.

Related to hvantran/project-management#100
```

### Multi-Repo Workflow Summary

```
PARENT REPO (project-management):
  ├─ Create sprint milestone
  ├─ Create user stories with [repo-name] prefix
  ├─ Create tasks with [repo-name] prefix
  ├─ Manage GitHub Project board
  └─ Track progress

CHILD REPOS (services/*/):
  ├─ Create feature branches: feature/[repo]-[issue]-[desc]
  ├─ Commit with: [repo-name] message
  ├─ Reference parent: hvantran/project-management#<issue>
  ├─ Create PR in child repo
  └─ Link PR to parent issue
```

---

## Summary Diagram

```
SPRINT PLANNING
   │
   ├─ PO: User stories → GitHub Issues (user-story label)
   ├─ BA: Requirements → Sub-issues + Comments
   ├─ TA: Architecture → Discussions (ADR)
   └─ PM: Tasks → GitHub Issues (task label)
           ↓
DEVELOPMENT
   │
   ├─ Developer: Branch → Code (TDD) → PR
   ├─ TA: Review architecture
   └─ PM: Track on GitHub Project Board
           ↓
TESTING
   │
   ├─ Tester: Execute BDD → PR Review
   ├─ Report bugs → GitHub Issues (bug label)
   └─ QA approval → PR approve
           ↓
DEPLOYMENT
   │
   ├─ Developer: Merge PR
   ├─ DevOps: Release → GitHub Release
   ├─ DevOps: Deploy → CI/CD Actions
   └─ PM: Close issues → Update sprint board
           ↓
SPRINT REVIEW
   │
   ├─ PM: Sprint metrics
   ├─ PO: Accept stories
   └─ Team: Retrospective → Discussion
```

---

## Next Steps

1. **Set up GitHub Project Board**
   - Create sprint board with columns
   - Configure automation workflows
   
2. **Create Issue Templates**
   - `.github/ISSUE_TEMPLATE/user-story.md`
   - `.github/ISSUE_TEMPLATE/task.md`
   - `.github/ISSUE_TEMPLATE/bug.md`
   
3. **Configure Labels**
   - Create priority, type, status, component labels
   
4. **Set Up CI/CD**
   - Create `.github/workflows/test.yml`
   - Create `.github/workflows/deploy.yml`
   
5. **Train Team**
   - Share this guide with all team members
   - Run a practice sprint
   - Refine based on feedback

---

## Key Takeaways

✅ **Each agent has specific GitHub responsibilities**
✅ **Issues track all work (stories, tasks, bugs)**
✅ **Projects visualize sprint progress (Kanban board)**
✅ **PRs are the quality gates (code review + QA)**
✅ **Branches follow naming conventions**
✅ **Commits link to issues for traceability**
✅**CI/CD automates testing and deployment**
✅ **Everything is traceable and auditable**

**This creates a complete, integrated workflow where requirements → design → development → testing → deployment are all tracked in GitHub.**
