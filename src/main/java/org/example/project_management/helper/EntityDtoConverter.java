package org.example.project_management.helper;

import org.example.project_management.dto.ClientDto;
import org.example.project_management.dto.InvoiceDto;
import org.example.project_management.dto.ProjectDto;
import org.example.project_management.dto.ProjectTaskDto;
import org.example.project_management.entity.*;
import org.example.project_management.exception.InvoiceNotFoundException;
import org.example.project_management.exception.ProjectNotFoundException;
import org.example.project_management.exception.ProjectTaskNotFoundException;
import org.example.project_management.repository.ClientRepository;
import org.example.project_management.repository.InvoiceRepository;
import org.example.project_management.repository.ProjectRepository;
import org.example.project_management.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityDtoConverter {
    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public EntityDtoConverter(ProjectRepository projectRepository, ClientRepository clientRepository, ProjectTaskRepository projectTaskRepository, InvoiceRepository invoiceRepository) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public ClientDto convertClientToClientDto(Client client) {
        List<Long> projectIds = client.getProjects()
                .stream()
                .map(Project::getId)
                .collect(Collectors.toList());
        ClientDto clientDto =
                new ClientDto(client.getId(), client.getName(), client.getEmail(), client.getPhone(), projectIds);
        return clientDto;
    }

    public Client convertClientDtoToClient(ClientDto clientDto) {
        Client client = new Client();
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());

        List<Project> projects = new ArrayList<>();
        // To create a client one or more projects are NOT required
        if (clientDto.getProjectIds() != null && !clientDto.getProjectIds().isEmpty()) {
            clientDto.getProjectIds().forEach(projectId -> {
                Project project = projectRepository.findById(projectId)
                        .orElseThrow(() -> new ProjectNotFoundException("Project {" + projectId + "} not found during ClientDto to Client conversion"));
                projects.add(project);
            });
        }
        client.setProjects(projects);

        return client;
    }

    public List<ClientDto> convertClientsToClientDtos(List<Client> clients) {
        return clients.stream()
                .map(this::convertClientToClientDto)
                .collect(Collectors.toList());
    }

    public InvoiceDto convertInvoiceToInvoiceDto(Invoice invoice) {
        InvoiceDto invoiceDto =
                new InvoiceDto(invoice.getId(), invoice.getAmount(), invoice.getDueDate(), invoice.getStatus().getDisplayName(), invoice.getProject().getId());
        return invoiceDto;
    }

    public Invoice convertInvoiceDtoToInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();
        invoice.setAmount(invoiceDto.getAmount());
        invoice.setStatus(InvoiceStatus.valueOf(invoiceDto.getStatus()));
        invoice.setDueDate(invoiceDto.getDueDate());

        // To create an invoice a project is required
        if (invoiceDto.getProjectId() == null) {
            throw new ProjectNotFoundException("Project ID is required for InvoiceDto to Invoice conversion");
        } else {
            Project project = projectRepository.findById(invoiceDto.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException("Project {" + invoiceDto.getProjectId() + "} not found during InvoiceDto to Invoice conversion"));
            invoice.setProject(project);
            return invoice;
        }
    }

    public List<InvoiceDto> convertInvoicesToInvoiceDtos(List<Invoice> invoices) {
        return invoices.stream()
                .map(this::convertInvoiceToInvoiceDto)
                .collect(Collectors.toList());
    }

    public ProjectDto convertProjectToProjectDto(Project project) {

        List<Long> taskIds = new ArrayList<>();
        project.getTasks().forEach(task -> taskIds.add(task.getId()));

        List<Long> invoiceIds = new ArrayList<>();
        project.getInvoices().forEach(invoice -> invoiceIds.add(invoice.getId()));

        ProjectDto projectDto =
                new ProjectDto(project.getId(), project.getTitle(), project.getDescription(), project.getStartDate(), project.getDeadline(), project.getStatus().getDisplayName(), project.getClient().getId(), taskIds, invoiceIds);

        return projectDto;
    }

    public Project convertProjectDtoToProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        project.setStartDate(projectDto.getStartDate());
        project.setDeadline(projectDto.getDeadline());
        project.setStatus(ProjectStatus.valueOf(projectDto.getStatus()));

        // To create a project a client is required
        if(projectDto.getClientId() == null) {
            throw new ProjectNotFoundException("Client ID is required for ProjectDto to Project conversion");
        } else {
            Client client = clientRepository.findById(projectDto.getClientId())
                    .orElseThrow(() -> new ProjectNotFoundException("Client {" + projectDto.getClientId() + "} not found during ProjectDto to Project conversion"));
            project.setClient(client);
        }

        // To create a project one or more tasks are NOT required
        List<ProjectTask> tasks = new ArrayList<>();
        if(projectDto.getTaskIds() != null && !projectDto.getTaskIds().isEmpty()) {
            projectDto.getTaskIds().forEach(taskId -> {
                ProjectTask task = projectTaskRepository.findById(taskId)
                        .orElseThrow(() -> new ProjectTaskNotFoundException("Project Task {" + taskId + "} not found during ProjectDto to Project conversion"));
                tasks.add(task);
            });
        }
        project.setTasks(tasks);

        // To create a project one or more invoices are NOT required
        List<Invoice> invoices = new ArrayList<>();
        if(projectDto.getInvoiceIds() != null && !projectDto.getInvoiceIds().isEmpty()) {
            projectDto.getInvoiceIds().forEach(invoiceId -> {
                Invoice invoice = invoiceRepository.findById(invoiceId)
                        .orElseThrow(() -> new InvoiceNotFoundException("Invoice {" + invoiceId + "} not found during ProjectDto to Project conversion"));
                invoices.add(invoice);
            });
        }
        project.setInvoices(invoices);

        return project;
    }

    public List<ProjectDto> convertProjectsToProjectDtos(List<Project> projects) {
        return projects.stream()
                .map(this::convertProjectToProjectDto)
                .collect(Collectors.toList());
    }

    public ProjectTaskDto convertProjectTaskToProjectTaskDto(ProjectTask projectTask) {
        ProjectTaskDto projectTaskDto =
                new ProjectTaskDto(projectTask.getId(), projectTask.getTitle(), projectTask.getDescription(), projectTask.getDueDate(), projectTask.getStatus().getDisplayName(), projectTask.getProject().getId());
        return projectTaskDto;
    }

    public ProjectTask convertProjectTaskDtoToProjectTask(ProjectTaskDto projectTaskDto) {
        ProjectTask projectTask = new ProjectTask();
        projectTask.setTitle(projectTaskDto.getTitle());
        projectTask.setDescription(projectTaskDto.getDescription());
        projectTask.setDueDate(projectTaskDto.getDueDate());
        projectTask.setStatus(TaskStatus.valueOf(projectTaskDto.getStatus()));

        // To create a project task a project is required
        if(projectTaskDto.getProjectId() == null) {
            throw new ProjectTaskNotFoundException("Project ID is required for ProjectTaskDto to ProjectTask conversion");
        } else {
            Project project = projectRepository.findById(projectTaskDto.getProjectId())
                    .orElseThrow(() -> new ProjectTaskNotFoundException("Project {" + projectTaskDto.getProjectId() + "} not found during ProjectTaskDto to ProjectTask conversion"));
            projectTask.setProject(project);
            return projectTask;
        }
    }

    public List<ProjectTaskDto> convertProjectTasksToProjectTaskDtos(List<ProjectTask> projectTasks) {
        return projectTasks.stream()
                .map(this::convertProjectTaskToProjectTaskDto)
                .collect(Collectors.toList());
    }
}
