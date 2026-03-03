# Soft Delete Feature Implementation Plan

## Goal

Implement a comprehensive soft delete mechanism for the Action Manager application that properly handles the lifecycle of actions from active status through soft deletion to permanent removal. This feature will ensure that when users delete an action through the UI, it transitions to a DELETED status rather than being immediately removed from the database. Actions in DELETED status will remain queryable and recoverable for a period before permanent deletion. The UI landing page will provide a dedicated delete icon for each action that permanently removes the action from the system when users explicitly request permanent deletion.

This implementation addresses the current gap where the DELETED status exists in the ActionStatus enum but is not utilized in the deletion workflow. The feature will provide users with a safety net to recover accidentally deleted actions while maintaining the ability to permanently remove actions when necessary.

## Requirements

### Functional Requirements

- **FR1**: When a user clicks the delete icon on an action in the main landing page, the system SHALL transition the action to DELETED status (soft delete) rather than permanently removing it
- **FR2**: Actions with DELETED status SHALL be excluded from the main action list by default but remain in the database
- **FR3**: The system SHALL provide a "Trash" or "Deleted Actions" view where users can see all actions with DELETED status
- **FR4**: Users SHALL be able to restore actions from DELETED status back to their previous status (ACTIVE, PAUSED, etc.)
- **FR5**: Users SHALL be able to permanently delete actions from the Trash view through a "Delete Forever" action with explicit confirmation
- **FR6**: When an action is permanently deleted, THE SYSTEM SHALL remove:
  - The action document from the database
  - All associated job documents
  - All job execution results
  - Action statistics from memory
- **FR7**: When an action transitions to DELETED status, THE SYSTEM SHALL:
  - Set the action status to DELETED
  - Pause all associated scheduled jobs
  - Update the action's deletedAt timestamp
  - Maintain all action data and job history for potential restoration
- **FR8**: The system SHALL support filtering and searching within deleted actions
- **FR9**: The API SHALL provide distinct endpoints for soft delete and permanent delete operations
- **FR10**: The system SHALL track the deletion timestamp and the user who performed the deletion (future enhancement placeholder)

### Non-Functional Requirements

- **NFR1**: Soft delete operations SHALL complete within 2 seconds for actions with up to 1000 jobs
- **NFR2**: The system SHALL maintain referential integrity during both soft and hard delete operations
- **NFR3**: Deleted actions SHALL not impact the performance of the main action listing queries
- **NFR4**: The implementation SHALL be backward compatible with existing archived actions
- **NFR5**: All delete operations SHALL be fully transactional to prevent data inconsistency
- **NFR6**: The system SHALL provide comprehensive audit logging for all deletion and restoration operations

