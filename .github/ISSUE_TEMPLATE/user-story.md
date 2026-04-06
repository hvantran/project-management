---
name: User Story
about: Product Owner creates user stories with BDD acceptance criteria
title: '[USER STORY] '
labels: 'type: user-story, role: product-owner, backlog'
assignees: ''
---

## User Story

**As a** [user type]
**I want** [goal/desire]
**So that** [benefit/value]

## Business Value
- **Priority:** High / Medium / Low
- **Business Impact:** [Description of business value]
- **Success Metrics:** [How we measure success - e.g., "30% increase in user engagement"]

## Acceptance Criteria (BDD Format)

### Scenario: [Main success scenario name]
```gherkin
  Given [initial context - be specific with test data]
  When [action or event performed]
  Then [expected outcome - specific and measurable]
  And [additional verification if needed]
```

### Scenario: [Alternative or error scenario name]
```gherkin
  Given [different context]
  When [action]
  Then [expected result]
```

**Example:**
```gherkin
Scenario: Successful user registration
  Given a new user with email "user@example.com"
  When the user completes the registration form
  Then an account should be created
  And a confirmation email should be sent to "user@example.com"
  And the user should be redirected to the welcome page

Scenario: Registration with existing email
  Given a user with email "user@example.com" already exists
  When a new user tries to register with "user@example.com"
  Then registration should be rejected
  And error message "Email already registered" should be displayed
```

## Dependencies
- [ ] Dependency 1 (if any - e.g., requires API endpoint #123)
- [ ] Dependency 2

## Technical Notes
[Optional: Any technical considerations or constraints]

## Definition of Done
- [ ] All acceptance criteria validated
- [ ] BDD scenarios passing
- [ ] Code reviewed and approved
- [ ] Tests passing (80%+ coverage)
- [ ] Documentation updated
- [ ] Deployed to staging
- [ ] Product Owner approval

---

**Next Steps:**
- [ ] Assign to @business-analyst for detailed requirements
- [ ] Add to Product Backlog project
- [ ] Add to milestone/release if planned
