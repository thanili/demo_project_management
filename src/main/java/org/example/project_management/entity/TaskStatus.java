package org.example.project_management.entity;

public enum TaskStatus {
    IDLE("Idle"),
    IN_PROGRESS("In Progress"),
    SUBMITTED("Submitted"),
    COMPLETE("Complete");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
