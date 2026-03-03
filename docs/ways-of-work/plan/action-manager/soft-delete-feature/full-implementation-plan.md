# Soft Delete Feature - Implementation Plan

## Executive Summary

Implement soft delete functionality in Action Manager to provide users with a two-stage deletion process:
1. **Soft Delete**: Move actions to DELETED status (recoverable)
2. **Permanent Delete**: Remove actions from database permanently (not recoverable)

**Current Gap**: The DELETED status exists but is unused. Deleting an action permanently removes it immediately.

**Solution**: Implement proper soft delete workflow with trash view and restoration capabilities.

---

## Technical Architecture

### System Components

```
Frontend (React/TypeScript)
├── ActionSummary (Enhanced with soft delete)
├── TrashView (NEW - shows deleted actions)
└── ConfirmationDialog (Enhanced with danger variant)

Backend (Spring Boot/Java)
├── ActionControllerV1 (New endpoints)
├── ActionManagerServiceImpl (New methods)
└── ActionDocumentRepository (Enhanced queries)

Database (MongoDB)
└── actions collection (New fields: deletedAt, previousStatus)
```

### Data Flow

**Soft Delete Flow:**
```
User clicks Delete → Confirmation → API PUT /actions/{id}/soft-delete
→ Set status=DELETED, deletedAt=now, previousStatus=currentStatus
→ Pause jobs → Save → Return success
```

**Permanent Delete Flow:**
```
User in Trash → Delete Forever → Strong warning confirmation
→ API DELETE /actions/{id}/permanent → Validate status=DELETED
→ Delete action+jobs+results → Clear stats → Return success
```

**Restore Flow:**
```
User in Trash → Restore → API PUT /actions/{id}/restore
→ Set status=previousStatus, deletedAt=null → Resume jobs → Return success
```

---

## Database Schema Changes

### ActionDocument (MongoDB)

**NEW Fields:**
```java
private Long deletedAt;          // Timestamp (epoch seconds), nullable
private ActionStatus previousStatus;  // Status before deletion, nullable
```

**Index:**
```javascript
db.actions.createIndex(
  { "actionStatus": 1, "deletedAt": -1 },
  { name: "idx_status_deleted_at" }
);
```

**No migration required** - MongoDB flexible schema handles null values automatically.

---

## API Endpoints

### 1. Soft Delete (NEW)
```
PUT /v1/actions/{actionId}/soft-delete
Response: 200 OK
{
  "actionId": "uuid",
  "actionStatus": "DELETED",
  "deletedAt": 1706140800,
  "message": "Action moved to trash"
}
```

### 2. Permanent Delete (NEW)
```
DELETE /v1/actions/{actionId}/permanent
Response: 204 No Content
Constraint: Action must be in DELETED status
```

### 3. Get Deleted Actions (NEW)
```
GET /v1/actions/deleted?pageIndex=0&pageSize=20&orderBy=-deletedAt
Response: 200 OK
{
  "content": [...],
  "totalElements": 45,
  "pageIndex": 0,
  "pageSize": 20
}
```

### 4. Restore Action (ENHANCED)
```
PUT /v1/actions/{actionId}/restore
Body (optional): { "targetStatus": "ACTIVE" }
Response: 200 OK
{
  "actionId": "uuid",
  "actionStatus": "ACTIVE",
  "restoredAt": 1706144400
}
```

### 5. Existing DELETE (DEPRECATED)
```
DELETE /v1/actions/{actionId}
Warning: Deprecated - use soft-delete + permanent flow
Maintained for backward compatibility (6 months)
```

---

## Backend Implementation

### ActionDocument Entity

```java
@Document("actions")
public class ActionDocument {
    @Id
    private String hash;
    private String actionName;
    private ActionStatus actionStatus;
    private long createdAt;
    
    // NEW FIELDS
    private Long deletedAt;
    private ActionStatus previousStatus;
}
```

### ActionManagerService Interface

```java
public interface ActionManagerService {
    // NEW METHODS
    void softDelete(String actionId);
    void permanentDelete(String actionId);
    RestoreResponse restore(String actionId, ActionStatus targetStatus);
    Page<ActionOverviewDTO> getDeletedActions(Pageable pageable);
    
    // EXISTING (keep for compatibility)
    void delete(String hash); // Mark as @Deprecated
}
```

### ActionManagerServiceImpl

```java
@Service
public class ActionManagerServiceImpl implements ActionManagerService {
    
    @Override
    @Transactional
    @LoggingMonitor(description = "Soft delete action {argument0}")
    public void softDelete(String actionId) {
        ActionDocument action = findActionDocument(actionId);
        
        // Validate: Cannot soft delete if already deleted
        if (action.getActionStatus() == ActionStatus.DELETED) {
            throw new InvalidArgumentException("Action already deleted");
        }
        
        // Store previous status for restoration
        action.setPreviousStatus(action.getActionStatus());
        action.setActionStatus(ActionStatus.DELETED);
        action.setDeletedAt(System.currentTimeMillis() / 1000);
        
        // Pause all jobs
        List<JobDocument> jobs = jobDocumentRepository.findJobByActionId(actionId);
        jobs.forEach(job -> job.setJobStatus(JobStatus.PAUSED));
        jobDocumentRepository.saveAll(jobs);
        jobs.stream()
            .map(JobDocument::getHash)
            .forEach(jobManagerService::pause);
        
        actionDocumentRepository.save(action);
    }
    
    @Override
    @Transactional
    @LoggingMonitor(description = "Permanent delete action {argument0}")
    public void permanentDelete(String actionId) {
        ActionDocument action = findActionDocument(actionId);
        
        // Validate: Can only permanently delete if in DELETED status
        if (action.getActionStatus() != ActionStatus.DELETED) {
            throw new InvalidArgumentException(
                "Action must be in DELETED status. Current: " + action.getActionStatus()
            );
        }
        
        // Perform hard delete (existing logic)
        actionDocumentRepository.deleteById(actionId);
        jobManagerService.deleteJobsByActionId(actionId);
        actionManagerStatistics.removeActionStats(actionId);
    }
    
    @Override
    @Transactional
    @LoggingMonitor(description = "Restore action {argument0}")
    public RestoreResponse restore(String actionId, ActionStatus targetStatus) {
        ActionDocument action = findActionDocument(actionId);
        
        // Validate: Can only restore from DELETED status
        if (action.getActionStatus() != ActionStatus.DELETED) {
            throw new InvalidArgumentException("Action not in DELETED status");
        }
        
        // Determine target status
        ActionStatus newStatus = targetStatus != null ? 
            targetStatus : action.getPreviousStatus();
        
        if (newStatus == null) {
            newStatus = ActionStatus.ACTIVE; // Default fallback
        }
        
        // Restore action
        action.setActionStatus(newStatus);
        action.setDeletedAt(null);
        action.setPreviousStatus(null);
        
        // Resume scheduled jobs if returning to ACTIVE
        if (newStatus == ActionStatus.ACTIVE) {
            List<JobDocument> jobs = jobDocumentRepository.findJobByActionId(actionId);
            jobs.stream()
                .filter(JobDocument::isScheduled)
                .forEach(job -> {
                    job.setJobStatus(JobStatus.SCHEDULED);
                    jobManagerService.resume(job.getHash());
                });
            jobDocumentRepository.saveAll(jobs);
        }
        
        actionDocumentRepository.save(action);
        
        return RestoreResponse.builder()
            .actionId(actionId)
            .actionStatus(newStatus)
            .restoredAt(System.currentTimeMillis() / 1000)
            .message("Action restored successfully")
            .build();
    }
    
    @Override
    public Page<ActionOverviewDTO> getDeletedActions(Pageable pageable) {
        List<ActionStatus> deletedStatus = List.of(ActionStatus.DELETED);
        Page<ActionDocument> deletedActions = 
            actionDocumentRepository.findByActionStatusIn(deletedStatus, pageable);
        return getActionOverviewDTOs(deletedActions);
    }
}
```

### ActionControllerV1

```java
@RestController
@RequestMapping("/v1/actions")
public class ActionControllerV1 {
    
    @PutMapping("/{actionId}/soft-delete")
    @PreAuthorize("hasAuthority('ACTION_DELETE')")
    public ResponseEntity<SoftDeleteResponse> softDeleteAction(@PathVariable String actionId) {
        actionManagerService.softDelete(actionId);
        return ResponseEntity.ok(SoftDeleteResponse.builder()
            .actionId(actionId)
            .actionStatus(ActionStatus.DELETED)
            .deletedAt(System.currentTimeMillis() / 1000)
            .message("Action moved to trash")
            .build());
    }
    
    @DeleteMapping("/{actionId}/permanent")
    @PreAuthorize("hasAuthority('ACTION_PERMANENT_DELETE')")
    public ResponseEntity<Void> permanentDeleteAction(@PathVariable String actionId) {
        actionManagerService.permanentDelete(actionId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/deleted")
    public ResponseEntity<PageResponseDTO<ActionOverviewDTO>> getDeletedActions(
            @RequestParam("pageIndex") int pageIndex,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "-deletedAt") String orderBy) {
        
        Sort sort = createSortFromOrderBy(orderBy);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<ActionOverviewDTO> deleted = actionManagerService.getDeletedActions(pageable);
        return ResponseEntity.ok(new PageResponseDTO<>(deleted));
    }
    
    @PutMapping("/{actionId}/restore")
    @PreAuthorize("hasAuthority('ACTION_RESTORE')")
    public ResponseEntity<RestoreResponse> restoreAction(
            @PathVariable String actionId,
            @RequestBody(required = false) RestoreRequest request) {
        
        ActionStatus targetStatus = request != null ? request.getTargetStatus() : null;
        RestoreResponse response = actionManagerService.restore(actionId, targetStatus);
        return ResponseEntity.ok(response);
    }
}
```

---

## Frontend Implementation

### New Components

#### 1. TrashView Component

```typescript
// Location: src/components/actions/TrashView.tsx

const TrashView: React.FC = () => {
  const [deletedActions, setDeletedActions] = useState<DeletedActionOverview[]>([]);
  const [loading, setLoading] = useState(false);
  const [pageIndex, setPageIndex] = useState(0);
  const [pageSize] = useState(20);
  
  useEffect(() => {
    loadDeletedActions();
  }, [pageIndex]);
  
  const loadDeletedActions = () => {
    ActionAPI.loadDeletedActionsAsync(
      pageIndex, pageSize, '-deletedAt',
      restClient,
      (result) => setDeletedActions(result.content)
    );
  };
  
  const handleRestore = (action: DeletedActionOverview) => {
    showConfirmDialog({
      title: 'Restore Action',
      message: `Restore "${action.name}" to ${action.previousStatus} status?`,
      onConfirm: () => {
        ActionAPI.restoreAction(action.hash, null, restClient, () => {
          showNotification('Action restored successfully', 'success');
          loadDeletedActions();
        });
      }
    });
  };
  
  const handlePermanentDelete = (action: DeletedActionOverview) => {
    showConfirmDialog({
      title: 'Permanently Delete Action',
      message: (
        <>
          <p><strong>⚠️ Warning: This cannot be undone!</strong></p>
          <p>Delete "{action.name}" permanently?</p>
          <p>All jobs and history will be removed.</p>
        </>
      ),
      variant: 'danger',
      confirmLabel: 'Delete Forever',
      onConfirm: () => {
        ActionAPI.permanentDeleteAction(action.hash, restClient, () => {
          showNotification('Action permanently deleted', 'success');
          loadDeletedActions();
        });
      }
    });
  };
  
  return (
    <Container>
      <PageHeader title="Trash" breadcrumbs={breadcrumbs} />
      {deletedActions.length === 0 ? (
        <EmptyState 
          icon={<DeleteOutlineIcon />}
          message="No deleted actions"
        />
      ) : (
        <DeletedActionTable 
          actions={deletedActions}
          onRestore={handleRestore}
          onPermanentDelete={handlePermanentDelete}
        />
      )}
    </Container>
  );
};
```

### Enhanced Components

#### 2. ActionSummary Updates

```typescript
// Change delete handler to use soft delete

// BEFORE
const handleDelete = (action) => {
  ActionAPI.deleteAction(action.hash, restClient, refreshList);
};

// AFTER
const handleDelete = (action) => {
  showConfirmDialog({
    title: 'Move to Trash',
    message: `Move "${action.name}" to trash? You can restore it later.`,
    variant: 'warning',
    onConfirm: () => {
      ActionAPI.softDeleteAction(action.hash, restClient, () => {
        showNotification('Action moved to trash', 'success');
        refreshList();
      });
    }
  });
};
```

#### 3. Enhanced ConfirmationDialog

```typescript
interface ConfirmationDialogProps {
  variant?: 'default' | 'warning' | 'danger';
  // ... other props
}

const ConfirmationDialog: React.FC<ConfirmationDialogProps> = ({
  variant = 'default',
  ...props
}) => {
  const getButtonColor = () => {
    switch (variant) {
      case 'danger': return 'error';
      case 'warning': return 'warning';
      default: return 'primary';
    }
  };
  
  return (
    <Dialog {...dialogProps}>
      <DialogTitle>
        {variant === 'danger' && <ErrorIcon color="error" />}
        {variant === 'warning' && <WarningIcon color="warning" />}
        {props.title}
      </DialogTitle>
      <DialogContent>{props.message}</DialogContent>
      <DialogActions>
        <Button onClick={props.onCancel}>Cancel</Button>
        <Button 
          onClick={props.onConfirm}
          color={getButtonColor()}
          variant="contained"
        >
          {props.confirmLabel || 'Confirm'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};
```

### API Client Updates

```typescript
// Add to ActionAPI class

static softDeleteAction = async (
  actionId: string,
  restClient: RestClient,
  successCallback: (response: SoftDeleteResponse) => void
) => {
  const requestOptions = {
    method: 'PUT',
    headers: { 'Accept': 'application/json' }
  };
  const url = `${ACTION_MANAGER_API_URL}/${actionId}/soft-delete`;
  await restClient.sendRequest(requestOptions, url, successCallback);
};

static permanentDeleteAction = async (
  actionId: string,
  restClient: RestClient,
  successCallback: () => void
) => {
  const requestOptions = {
    method: 'DELETE',
    headers: { 'Accept': 'application/json' }
  };
  const url = `${ACTION_MANAGER_API_URL}/${actionId}/permanent`;
  await restClient.sendRequest(requestOptions, url, successCallback);
};

static loadDeletedActionsAsync = async (
  pageIndex: number,
  pageSize: number,
  orderBy: string,
  restClient: RestClient,
  successCallback: (result: PagingResult) => void
) => {
  const url = `${ACTION_MANAGER_API_URL}/deleted?pageIndex=${pageIndex}&pageSize=${pageSize}&orderBy=${orderBy}`;
  await restClient.sendRequest({ method: 'GET' }, url, successCallback);
};
```

### Navigation Updates

```typescript
// Add to navigation menu
<ListItem button component={Link} to="/actions/trash">
  <ListItemIcon>
    <Badge badgeContent={deletedCount} color="error">
      <DeleteOutlineIcon />
    </Badge>
  </ListItemIcon>
  <ListItemText primary="Trash" />
</ListItem>
```

---

## Implementation Checklist

### Phase 1: Backend (3-4 days)
- [ ] Add `deletedAt` and `previousStatus` fields to ActionDocument
- [ ] Create `softDelete()` method in ActionManagerServiceImpl
- [ ] Rename existing `delete()` to `permanentDelete()` with validation
- [ ] Enhance `restore()` method to use previousStatus
- [ ] Create `getDeletedActions()` method
- [ ] Add controller endpoints (soft-delete, permanent, deleted, restore)
- [ ] Update ActionDocumentRepository with query methods
- [ ] Write unit tests (>80% coverage)
- [ ] Write integration tests
- [ ] Add error handling and validation

### Phase 2: Frontend (3-4 days)
- [ ] Create TrashView component
- [ ] Create DeletedActionTable component
- [ ] Update ActionSummary to use soft delete
- [ ] Enhance ConfirmationDialog with variants
- [ ] Update ActionAPI with new methods
- [ ] Add Trash route and navigation link
- [ ] Create TypeScript interfaces for new types
- [ ] Add loading and error states
- [ ] Test responsive design
- [ ] Accessibility testing

### Phase 3: Testing & Polish (2-3 days)
- [ ] End-to-end testing of full workflow
- [ ] Performance testing with large datasets
- [ ] Security testing (authorization)
- [ ] Cross-browser testing
- [ ] User acceptance testing
- [ ] Fix bugs and refine UX
- [ ] Update documentation

### Phase 4: Deployment (1-2 days)
- [ ] Update API documentation
- [ ] Update user guide
- [ ] Prepare release notes
- [ ] Deploy to staging
- [ ] Smoke test staging
- [ ] Production deployment
- [ ] Monitor metrics

**Total Duration: 9-13 days (approximately 2 weeks)**

---

## Success Criteria

### Functional
✅ Users can soft delete actions (move to trash)
✅ Soft deleted actions disappear from main view
✅ Trash view accessible and shows deleted actions
✅ Users can restore actions from trash
✅ Restored actions return to previous status
✅ Users can permanently delete from trash
✅ Permanent delete removes all data
✅ Strong confirmation for destructive actions

### Technical
✅ Soft delete < 2 seconds (1000 jobs)
✅ Permanent delete < 5 seconds
✅ All operations transactional
✅ Proper indexing for performance
✅ >80% code coverage
✅ Zero critical security issues
✅ Backward compatible
✅ Comprehensive audit logging

### UX
✅ Intuitive deletion workflow
✅ Clear confirmation messages
✅ Easy navigation to trash
✅ Helpful empty states
✅ Responsive design
✅ WCAG 2.1 AA compliant

---

## Risk Mitigation

| Risk | Severity | Mitigation |
|------|----------|------------|
| Data loss from premature permanent deletion | HIGH | Strong warnings, explicit confirmation, audit logs |
| Performance degradation | MEDIUM | Proper indexing, pagination, monitoring |
| Memory leaks from statistics | MEDIUM | Automatic cleanup, memory monitoring |
| Transaction failures | HIGH | Proper @Transactional, error handling, recovery scripts |
| Backward compatibility | HIGH | Deprecated endpoint with 6-month period, versioning |

---

## Future Enhancements

### Short-term (3-6 months)
- Auto-cleanup of old deleted actions (30-day retention)
- Bulk restore/delete operations
- User attribution (track who deleted)
- Search and filter in trash view

### Long-term (6-12 months)
- Soft delete for individual jobs
- Undo delete with grace period
- Approval workflows for permanent deletion
- Deletion analytics dashboard

---

## Appendix

### Database Index Creation
```javascript
db.actions.createIndex(
  { "actionStatus": 1, "deletedAt": -1 },
  { name: "idx_status_deleted_at" }
);
```

### Example API Calls

**Soft Delete:**
```bash
curl -X PUT http://localhost:8080/v1/actions/{id}/soft-delete \
  -H "Authorization: Bearer $TOKEN"
```

**Get Trash:**
```bash
curl -X GET "http://localhost:8080/v1/actions/deleted?pageIndex=0&pageSize=20" \
  -H "Authorization: Bearer $TOKEN"
```

**Restore:**
```bash
curl -X PUT http://localhost:8080/v1/actions/{id}/restore \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"targetStatus": "ACTIVE"}'
```

**Permanent Delete:**
```bash
curl -X DELETE http://localhost:8080/v1/actions/{id}/permanent \
  -H "Authorization: Bearer $TOKEN"
```

---

**Document Version:** 1.0  
**Created:** 2026-01-24  
**Status:** Ready for Implementation

