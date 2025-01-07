package org.example.project_management.entity;

public enum InvoiceStatus {
    ISSUED("Issued"),
    PAID("Paid"),
    CANCELLED("Cancelled");

    private final String displayName;

    InvoiceStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
