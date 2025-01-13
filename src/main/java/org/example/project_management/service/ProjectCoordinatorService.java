package org.example.project_management.service;

import org.example.project_management.entity.Invoice;
import org.example.project_management.entity.Project;
import org.example.project_management.entity.ProjectTask;

import java.util.List;

public interface ProjectCoordinatorService {
    Project createClientProject(long clientId);
    void handleSubmitProject(Project project);
    List<Invoice> getInvoicesByProjectId(Long projectId);
    List<ProjectTask> getProjectTasksByProjectId(Long projectId);
    List<Project> getClientProjectsByClientId(Long clientId);
    ProjectTask createProjectTask(long projectId);
    void handleSubmitProjectTask(ProjectTask projectTask);
    Invoice createProjectInvoice(long projectId);
    void handleSubmitProjectInvoice(Invoice invoice);
}
