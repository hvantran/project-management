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
- [ ] **Critical** - System down, no workaround
- [ ] **High** - Major feature broken, workaround exists
- [ ] **Medium** - Feature partially working
- [ ] **Low** - Minor issue, cosmetic

## Environment
- **Environment:** Production / Staging / Development
- **Version/Release:** [Version number or commit SHA]
- **Browser/Client:** [If applicable - e.g., Chrome 121, Safari 17]
- **Operating System:** [If applicable - e.g., Ubuntu 22.04, macOS 14]

## Steps to Reproduce
1. Step 1 (be specific - e.g., "Navigate to /admin/users")
2. Step 2 (e.g., "Click 'Add User' button")
3. Step 3 (e.g., "Enter username 'test@user'")
4. Observe the issue

## Expected Behavior
[What should happen according to requirements]

## Actual Behavior
[What actually happens]

## Evidence

### Error Messages
```
[Paste error messages, stack traces, or logs]
```

### Screenshots
[Attach screenshots if applicable]

### Request/Response (if API bug)
**Request:**
```json
{
  "example": "request payload"
}
```

**Response:**
```json
{
  "error": "response payload"
}
```

## Impact
- **User Impact:** [Who is affected and how - e.g., "All users cannot login"]
- **Frequency:** [How often does this occur - e.g., "100% reproducible", "Intermittent - 10% of attempts"]
- **Workaround:** [If available - e.g., "Users can use forgot password flow"]

## Related Information
- **Related User Story:** #[issue-number]
- **BDD Scenario:** [Which acceptance criteria or scenario failed]
- **Related Bugs:** #[issue-number] (if applicable)
- **First Noticed:** [When was this first observed]

## Root Cause Analysis
[To be filled by @developer after investigation]

## Fix Verification Steps
[Steps tester should follow to verify the fix]
1. Retry same reproduction steps
2. Verify expected behavior occurs
3. Test related scenarios to check for regressions
4. Verify fix in all affected environments

---

**Assignee:** @developer (assign to appropriate developer)
**Priority:** Set priority label based on severity and impact
