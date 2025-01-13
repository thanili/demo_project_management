package org.example.project_management.service.impl;

import org.example.project_management.entity.Client;
import org.example.project_management.entity.Invoice;
import org.example.project_management.entity.Project;
import org.example.project_management.entity.ProjectTask;
import org.example.project_management.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectCoordinatorServiceImpl implements ProjectCoordinatorService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectCoordinatorService.class);

    private final ClientService clientService;
    private final ProjectService projectService;
    private final InvoiceService invoiceService;
    private final ProjectTaskService projectTaskService;

    @Autowired
    public ProjectCoordinatorServiceImpl(ClientService clientService,
                                         ProjectService projectService,
                                         InvoiceService invoiceService,
                                         ProjectTaskService projectTaskService) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.invoiceService = invoiceService;
        this.projectTaskService = projectTaskService;
    }

    @Override
    public Project createClientProject(long clientId) {
        Client client = clientService.getClientById(clientId);
        Project newProject = new Project();
        newProject.setClient(client);
        return newProject;
    }

    @Override
    public void handleSubmitProject(Project project) {
        if(project.getId() != null) {
            Project existingProject = projectService.getProjectById(project.getId());
            if (existingProject != null) {
                project.setClient(existingProject.getClient());
                project.setInvoices(existingProject.getInvoices());
                project.setTasks(existingProject.getTasks());
            }
        } else {
            // New project
            Client client = clientService.getClientById(project.getClient().getId());
            project.setClient(client);
        }
        projectService.saveProject(project);
    }

    @Override
    public List<Invoice> getInvoicesByProjectId(Long projectId) {
        logger.info("Getting invoices of project: {}", projectId);
        return invoiceService.getInvoicesByProjectId(projectId);
    }

    @Override
    public List<ProjectTask> getProjectTasksByProjectId(Long projectId) {
        logger.info("Getting project tasks of project: {}", projectId);
        return projectTaskService.getProjectTasksByProjectId(projectId);
    }

    @Override
    public List<Project> getClientProjectsByClientId(Long clientId) {
        logger.info("Getting projects of client: {}", clientId);
        return projectService.getClientProjectsByClientId(clientId);
    }

    @Override
    public ProjectTask createProjectTask(long projectId) {
        Project project = projectService.getProjectById(projectId);
        ProjectTask newProjectTask = new ProjectTask();
        newProjectTask.setProject(project);
        return newProjectTask;
    }

    @Override
    public void handleSubmitProjectTask(ProjectTask projectTask) {
        if(projectTask.getId() != null) {
            ProjectTask existingProjectTask = projectTaskService.getProjectTaskById(projectTask.getId());
            if (existingProjectTask != null) {
                projectTask.setProject(existingProjectTask.getProject());
            }
        } else {
            // New projectTask
            Project project = projectService.getProjectById(projectTask.getProject().getId());
            projectTask.setProject(project);
        }
        projectTaskService.saveProjectTask(projectTask);
    }

    @Override
    public Invoice createProjectInvoice(long projectId) {
        Project project = projectService.getProjectById(projectId);
        Invoice newInvoice = new Invoice();
        newInvoice.setProject(project);
        return newInvoice;
    }

    @Override
    public void handleSubmitProjectInvoice(Invoice invoice) {
        if(invoice.getId() != null) {
            Invoice existingInvoice = invoiceService.getInvoiceById(invoice.getId());
            if (existingInvoice != null) {
                invoice.setProject(existingInvoice.getProject());
            }
        } else {
            // New invoice
            Project project = projectService.getProjectById(invoice.getProject().getId());
            invoice.setProject(project);
        }
        invoiceService.saveInvoice(invoice);
    }
}
