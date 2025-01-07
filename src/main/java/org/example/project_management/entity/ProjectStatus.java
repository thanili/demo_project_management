package org.example.project_management.entity;

public enum ProjectStatus {
    IDLE("Idle"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
