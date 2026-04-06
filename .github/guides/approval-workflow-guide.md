# Review & Approval Workflow Guide

A comprehensive guide for implementing approval gates in multi-agent collaboration workflows.

---

## Overview

**Purpose:** Ensure quality and alignment at each phase by requiring explicit approval before proceeding to the next role.

**Key Principle:** "Shift-left quality" - catch issues early through structured reviews.

---

## 🎯 Approval Gate Strategies

### Strategy 1: Manual Approval Prompts ⭐ (Recommended for Getting Started)

**How it works:**
- Agent completes deliverable
- Orchestrator pauses and summarizes output
- User reviews and explicitly approves or requests changes
- Only after approval does workflow proceed

**Implementation:**
```
User: "@workflow-orchestrator Implement payment feature with approval gates"

Orchestrator: 
"Phase 1 - Discovery & Planning
Invoking @product-owner to create user stories..."

[After @product-owner completes]

Orchestrator:
"✅ @product-owner has completed user stories.

📄 Deliverable: user-stories.md
   - 3 user stories created
   - BDD acceptance criteria defined
   - Business value documented

👉 APPROVAL REQUIRED
   Please review the deliverable and respond:
   - 'approve' to proceed to @business-analyst
   - 'request changes: [feedback]' to iterate
   - 'show details' to see full content"

User: "approve"

Orchestrator:
"✅ Approved. Proceeding to @business-analyst..."
```

**Pros:**
- ✅ Simple, no tooling required
- ✅ Complete control over workflow
- ✅ Clear decision points

**Cons:**
- ❌ Requires active participation
- ❌ No persistent audit trail

---

### Strategy 2: GitHub Issues/PRs for Approval ⭐⭐ (Recommended for Teams)

**How it works:**
- Each phase creates a GitHub Issue or Draft PR
- Reviewers approve via comments/reviews
- Orchestrator checks approval status before proceeding

**Implementation:**

#### Phase Output as GitHub Issue
```markdown
Issue Title: [APPROVAL NEEDED] User Stories - Payment Feature

Labels: needs-review, product-owner-deliverable

## Phase: Discovery (Product Owner)

### Deliverables
- User stories with BDD acceptance criteria
- Business value assessment
- Priority ranking

### Artifacts
📄 See: docs/features/payment-feature/user-stories.md

### Review Checklist
- [ ] User stories follow BDD format (Given-When-Then)
- [ ] Business value is clear
- [ ] Acceptance criteria are testable
- [ ] Priorities align with roadmap

### Next Steps
After approval, this will be handed off to:
- @business-analyst for detailed scenario development

---

**To approve:** Comment "LGTM" or "Approved"
**To request changes:** Comment with specific feedback
**Assigned to:** @product-manager, @stakeholders
```

#### Using GitHub CLI
```bash
# Agent creates issue after completing work
gh issue create \
  --title "[APPROVAL NEEDED] User Stories - Payment Feature" \
  --body-file approval-template.md \
  --label "needs-review,product-owner" \
  --assignee "stakeholder-username"

# Check approval status
gh issue view 123 --json comments \
  | jq '.comments[] | select(.body | contains("Approved"))'

# Proceed if approved
if [approved]; then
  gh issue edit 123 --add-label "approved"
  gh issue close 123
  # Continue to next phase
fi
```

**Pros:**
- ✅ Persistent audit trail
- ✅ Async review (doesn't block)
- ✅ Built into existing GitHub workflow
- ✅ Can assign multiple reviewers

**Cons:**
- ❌ Requires GitHub CLI integration
- ❌ More complex setup

---

### Strategy 3: Slack Notifications + Manual Approval ⭐⭐⭐ (Best for Distributed Teams)

**How it works:**
- Agent completes work
- Sends Slack notification to channel/user
- Human reviews in Slack
- Replies with approval/feedback
- Orchestrator continues based on response

**Implementation:**

#### Slack Webhook Integration
```bash
# .env file
SLACK_WEBHOOK_URL=https://hooks.slack.com/services/YOUR/WEBHOOK/URL
SLACK_APPROVAL_CHANNEL=#feature-approvals
```

#### Notification Template
```json
{
  "channel": "#feature-approvals",
  "username": "Workflow Orchestrator",
  "icon_emoji": ":robot_face:",
  "attachments": [
    {
      "color": "warning",
      "title": "🔔 Approval Required: User Stories Complete",
      "fields": [
        {
          "title": "Phase",
          "value": "Discovery & Planning",
          "short": true
        },
        {
          "title": "Agent",
          "value": "@product-owner",
          "short": true
        },
        {
          "title": "Deliverable",
          "value": "user-stories.md",
          "short": true
        },
        {
          "title": "Next Phase",
          "value": "@business-analyst - Detailed Scenarios",
          "short": true
        }
      ],
      "text": "*Summary:*\n• 3 user stories created\n• BDD acceptance criteria defined\n• Business value: High priority",
      "actions": [
        {
          "type": "button",
          "text": "View in GitHub",
          "url": "https://github.com/org/repo/blob/main/docs/user-stories.md"
        },
        {
          "type": "button",
          "text": "Review Checklist",
          "url": "https://github.com/org/repo/issues/123"
        }
      ],
      "footer": "React with ✅ to approve | 🔄 to request changes"
    }
  ]
}
```

#### Sending Notification
```bash
# Send Slack notification
curl -X POST $SLACK_WEBHOOK_URL \
  -H 'Content-Type: application/json' \
  -d @slack-notification.json

# Human reviews and reacts with emoji
# Bot monitors reactions and proceeds accordingly
```

**Interactive Approval with Slack Workflow:**
```yaml
# Slack Workflow Builder configuration
trigger: "Agent completes phase"
steps:
  1. Send message to #feature-approvals
  2. Add reaction buttons: ✅ Approve | ❌ Reject | 🔄 Request Changes
  3. If ✅: 
     - Update GitHub issue status
     - Notify orchestrator to proceed
  4. If ❌ or 🔄:
     - Collect feedback
     - Notify agent to revise
```

**Pros:**
- ✅ Real-time notifications
- ✅ Async, non-blocking
- ✅ Team visibility
- ✅ Mobile-friendly approvals
- ✅ Audit trail in Slack

**Cons:**
- ❌ Requires Slack webhook setup
- ❌ More infrastructure

---

### Strategy 4: Hybrid Approach ⭐⭐⭐⭐ (Enterprise Best Practice)

**Combine multiple strategies:**

```
Agent completes work
    ↓
Creates GitHub Issue/PR with deliverable
    ↓
Sends Slack notification with link
    ↓
Stakeholders review in GitHub
    ↓
Approve via GitHub comment/PR review
    ↓
Slack bot updates status
    ↓
Orchestrator checks GitHub approval status
    ↓
Proceeds to next phase
```

**Example:**
```bash
# Step 1: @product-owner completes user stories
# Agent creates artifact: docs/features/user-stories.md

# Step 2: Create GitHub issue for approval
gh issue create \
  --title "[APPROVAL] User Stories - Payment Feature" \
  --body "$(cat docs/features/user-stories.md)" \
  --label "needs-review,product-owner" \
  --assignee "product-manager"

# Step 3: Send Slack notification
ISSUE_URL=$(gh issue view 123 --json url -q .url)
curl -X POST $SLACK_WEBHOOK_URL -d '{
  "text": "🔔 User Stories ready for review",
  "attachments": [{
    "text": "Please review and approve: '"$ISSUE_URL"'",
    "color": "warning"
  }]
}'

# Step 4: Wait for approval (polling or webhook)
while true; do
  APPROVED=$(gh issue view 123 --json labels -q '.labels[] | select(.name=="approved")')
  if [ -n "$APPROVED" ]; then
    echo "✅ Approved! Proceeding to @business-analyst"
    break
  fi
  sleep 60
done

# Step 5: Notify Slack of progression
curl -X POST $SLACK_WEBHOOK_URL -d '{
  "text": "✅ User Stories approved. Proceeding to @business-analyst..."
}'
```

---

## 📋 Approval Checklist Templates

### Product Owner Deliverable Approval
```markdown
## Review Checklist

### User Stories Quality
- [ ] Written in "As a/I want/So that" format
- [ ] BDD acceptance criteria (Given-When-Then) included
- [ ] Business value clearly stated
- [ ] Success metrics defined
- [ ] Dependencies identified

### Business Alignment
- [ ] Aligns with product roadmap
- [ ] Priority is appropriate
- [ ] Stakeholders are identified
- [ ] ROI is justified

### Readiness for Next Phase
- [ ] Sufficient detail for @business-analyst
- [ ] Scope is clear and bounded
- [ ] Risks are documented

**Approval Status:** ⏳ Pending | ✅ Approved | 🔄 Changes Requested
**Approver:** [Name]
**Date:** [YYYY-MM-DD]
**Comments:** [Feedback]
```

### Business Analyst Deliverable Approval
```markdown
## Review Checklist

### BDD Scenarios Quality
- [ ] Scenarios use Given-When-Then format
- [ ] Concrete examples provided (not abstract)
- [ ] Data tables used for variations
- [ ] Edge cases and negative scenarios included
- [ ] Declarative (WHAT) not imperative (HOW)

### Requirements Completeness
- [ ] All user stories have scenarios
- [ ] Business rules clearly documented
- [ ] Non-functional requirements included
- [ ] Dependencies mapped

### Technical Feasibility
- [ ] Reviewed by @technical-architect
- [ ] Implementation complexity assessed
- [ ] Data model defined

**Approval Status:** ⏳ Pending | ✅ Approved | 🔄 Changes Requested
**Approver:** [Name]
**Date:** [YYYY-MM-DD]
```

### Developer Deliverable Approval (Code Review)
```markdown
## Code Review Checklist

### Implementation Quality
- [ ] TDD followed (tests written first)
- [ ] All unit tests passing
- [ ] Code coverage ≥ 80%
- [ ] BDD step definitions implemented
- [ ] BDD scenarios passing

### Code Standards
- [ ] Follows coding conventions
- [ ] Linting passes
- [ ] No code smells
- [ ] Properly commented
- [ ] Error handling implemented

### Architecture Compliance
- [ ] Follows @technical-architect design
- [ ] Design patterns applied correctly
- [ ] Dependencies managed properly

**Approval Status:** ⏳ Pending | ✅ Approved | 🔄 Changes Requested
**Reviewer:** [Name]
**Date:** [YYYY-MM-DD]
```

### Tester Deliverable Approval (QA Sign-off)
```markdown
## QA Sign-off Checklist

### Test Execution
- [ ] All BDD scenarios executed
- [ ] Pass rate ≥ 95%
- [ ] Edge cases tested
- [ ] Regression tests passed

### Quality Metrics
- [ ] No critical defects
- [ ] High/medium defects resolved
- [ ] Performance within SLA
- [ ] Security scan clean

### Acceptance Criteria
- [ ] All acceptance criteria validated
- [ ] User scenarios verified
- [ ] Documentation updated

**Approval Status:** ⏳ Pending | ✅ Approved | 🔄 Changes Requested
**QA Lead:** [Name]
**Date:** [YYYY-MM-DD]
```

---

## 🔄 Approval Workflow Patterns

### Pattern 1: Sequential Approval (Waterfall-style)
```
@product-owner → Deliverable → [APPROVAL] → @business-analyst → [APPROVAL] → ...
```
**When to use:** High-risk changes, regulatory requirements, critical features

### Pattern 2: Parallel Review (Concurrent)
```
@business-analyst
    ↓
Deliverable → [Multiple Reviewers]
              - @product-owner
              - @technical-architect
              - @tester
    ↓ (All approve)
Proceed
```
**When to use:** Cross-functional alignment needed

### Pattern 3: Delegation Approval
```
@business-analyst → Deliverable → Auto-approve if criteria met
                                 → Manual review only if:
                                   - High complexity
                                   - New patterns
                                   - Risk identified
```
**When to use:** Mature teams, well-defined standards

---

## 🛠️ Implementation Guide

### Quick Start: Manual Approval Prompts

Update your workflow invocation:
```
User: "@workflow-orchestrator Implement payment feature with manual approval gates"

Orchestrator will pause at each phase and ask:
"Phase completed. Review deliverable at [path]. Approve to proceed? (yes/no/changes)"
```

### GitHub Integration Setup

1. **Install GitHub CLI:**
```bash
# Install gh CLI
brew install gh  # macOS
# or download from https://cli.github.com/

# Authenticate
gh auth login
```

2. **Configure approval template:**
```bash
mkdir -p .github/ISSUE_TEMPLATE
cat > .github/ISSUE_TEMPLATE/approval-gate.md << 'EOF'
---
name: Approval Gate
about: Review and approve agent deliverable
labels: needs-review
assignees: ''
---

## Phase: [Phase Name]
## Agent: [Agent Role]

### Deliverable
[Description and link to artifact]

### Review Checklist
- [ ] Quality criteria met
- [ ] Ready for next phase

**Approval:** Comment "Approved" or "Request changes: [feedback]"
EOF
```

3. **Workflow integration:**
Agent creates issue after completing work:
```bash
gh issue create \
  --template approval-gate \
  --title "[APPROVAL] User Stories Complete" \
  --assignee product-manager
```

### Slack Integration Setup

1. **Create Slack App:**
   - Go to https://api.slack.com/apps
   - Create New App
   - Enable Incoming Webhooks
   - Copy Webhook URL

2. **Store webhook securely:**
```bash
# Add to .env file (don't commit!)
echo "SLACK_WEBHOOK_URL=your-webhook-url" >> .env
```

3. **Send notifications:**
```bash
# Function to send Slack notification
send_slack_notification() {
  local message=$1
  local color=$2
  
  curl -X POST $SLACK_WEBHOOK_URL \
    -H 'Content-Type: application/json' \
    -d '{
      "attachments": [{
        "color": "'$color'",
        "text": "'$message'"
      }]
    }'
}

# Usage
send_slack_notification "✅ User Stories approved" "good"
```

---

## ✅ Best Practices

### 1. **Define Clear Approval Criteria**
Don't approve on gut feeling - use checklists

### 2. **Set Approval Timeouts**
```
Auto-approve if no response within:
- Low priority: 48 hours
- Medium priority: 24 hours
- High priority: 4 hours
```

### 3. **Delegate Appropriately**
```
Product Owner deliverable → Product Manager approves
Technical Architecture → Lead Architect approves
Code Implementation → Tech Lead + Peer review
QA Results → QA Lead approves
```

### 4. **Provide Context**
Every approval request should include:
- What was delivered
- Why it matters
- What's next
- What to look for

### 5. **Track Approval Metrics**
```
- Average time to approve per phase
- Approval rejection rate
- Most common change requests
- Iterative improvement areas
```

### 6. **Automate Where Possible**
```bash
# Auto-approve if all checks pass
if [ $TESTS_PASS -eq 1 ] && \
   [ $COVERAGE -ge 80 ] && \
   [ $LINTING_PASS -eq 1 ]; then
  echo "✅ Auto-approved - all quality gates passed"
else
  echo "⏸️ Manual review required"
fi
```

---

## 📊 Approval Tracking Dashboard

### GitHub Project Board
```
Columns:
1. In Progress (Agent working)
2. Needs Review (Awaiting approval)
3. Changes Requested (Feedback provided)
4. Approved (Ready for next phase)
5. Completed (Entire workflow done)
```

### Metrics to Track
```markdown
## Weekly Approval Metrics

| Phase | Avg Approval Time | Rejection Rate | Iterations |
|-------|------------------|----------------|------------|
| PO Stories | 4 hours | 20% | 1.3 |
| BA Scenarios | 8 hours | 15% | 1.1 |
| Dev Code | 12 hours | 25% | 1.5 |
| QA Testing | 6 hours | 10% | 1.0 |
```

---

## 🚀 Recommended Approach

**For Solo Developers:**
→ Use **Manual Approval Prompts** (Strategy 1)

**For Small Teams (2-5):**
→ Use **GitHub Issues** (Strategy 2)

**For Medium Teams (5-15):**
→ Use **Slack + GitHub** (Hybrid - Strategy 4)

**For Large/Distributed Teams:**
→ Use **Full Hybrid** with automated quality gates (Strategy 4 + automation)

---

## Example: Complete Approval Flow

```bash
# 1. @product-owner completes user stories
@product-owner "Create user stories for payment feature"

# 2. Orchestrator creates approval request
gh issue create \
  --title "[APPROVAL] Payment Feature - User Stories" \
  --body-file user-stories.md \
  --label needs-review,product-owner \
  --assignee product-manager

# 3. Send Slack notification
curl -X POST $SLACK_WEBHOOK_URL -d '{
  "text": "🔔 User Stories ready for review: https://github.com/org/repo/issues/123"
}'

# 4. Product Manager reviews in GitHub
# Adds comment: "Approved - great BDD scenarios!"

# 5. Orchestrator detects approval
gh issue view 123 --json comments | grep -q "Approved"

# 6. Update Slack
curl -X POST $SLACK_WEBHOOK_URL -d '{
  "text": "✅ User Stories approved. Starting @business-analyst phase..."
}'

# 7. Proceed to next phase
@business-analyst "Create detailed BDD scenarios from approved user stories"
```

---

## 📝 Summary

| Strategy | Complexity | Async | Audit Trail | Best For |
|----------|-----------|-------|-------------|----------|
| Manual Prompts | Low | No | No | Solo, Learning |
| GitHub Issues | Medium | Yes | Yes | Teams, Collaboration |
| Slack | Medium | Yes | Yes | Distributed Teams |
| Hybrid | High | Yes | Yes | Enterprise |

**Start simple, evolve as needed.**
