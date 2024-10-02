package org.example.project_management.exception;

public class ProjectTaskNotFoundException extends RuntimeException {
    public ProjectTaskNotFoundException(String message) {
        super(message);
    }
}
