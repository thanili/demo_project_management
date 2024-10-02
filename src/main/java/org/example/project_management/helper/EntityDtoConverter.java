package org.example.project_management.helper;

import org.example.project_management.dto.ClientDto;
import org.example.project_management.dto.InvoiceDto;
import org.example.project_management.dto.ProjectDto;
import org.example.project_management.dto.ProjectTaskDto;
import org.example.project_management.entity.Client;
import org.example.project_management.entity.Invoice;
import org.example.project_management.entity.Project;
import org.example.project_management.entity.ProjectTask;
import org.example.project_management.exception.ClientNotFoundException;
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
        clientDto.getProjectIds().forEach(projectId -> {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ClientNotFoundException("Client not found"));
            projects.add(project);
        });
        // Consider creating a method to get all projects of a client (in projectRepository)
        //projects = projectRepository.findAllByClientId(client.getId());

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
                new InvoiceDto(invoice.getId(), invoice.getAmount(), invoice.getDueDate(), invoice.getStatus(), invoice.getProject().getId());
        return invoiceDto;
    }

    public Invoice convertInvoiceDtoToInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();
        invoice.setAmount(invoiceDto.getAmount());
        invoice.setStatus(invoiceDto.getStatus());
        invoice.setDueDate(invoiceDto.getDueDate());
        Project project = projectRepository.findById(invoiceDto.getProjectId())
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
        invoice.setProject(project);
        return invoice;
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
                new ProjectDto(project.getId(), project.getTitle(), project.getDescription(), project.getStartDate(), project.getDeadline(), project.getStatus(), project.getClient().getId(), taskIds, invoiceIds);

        return projectDto;
    }

    public Project convertProjectDtoToProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        project.setStartDate(projectDto.getStartDate());
        project.setDeadline(projectDto.getDeadline());
        project.setStatus(projectDto.getStatus());

        Client client = clientRepository.findById(projectDto.getClientId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        project.setClient(client);

        // Consider creating a method to get all projectTasks of a project (in projectRepository)
        List<ProjectTask> tasks = new ArrayList<>();
        projectDto.getTaskIds().forEach(taskId -> {
            ProjectTask task = projectTaskRepository.findById(taskId)
                    .orElseThrow(() -> new ProjectTaskNotFoundException("Project Task not found"));
            tasks.add(task);
        });
        project.setTasks(tasks);

        // Consider creating a method to get all invoices for a project (in projectRepository)
        List<Invoice> invoices = new ArrayList<>();
        projectDto.getInvoiceIds().forEach(invoiceId -> {
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
            invoices.add(invoice);
        });
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
                new ProjectTaskDto(projectTask.getId(), projectTask.getTitle(), projectTask.getDescription(), projectTask.getDueDate(), projectTask.getStatus(), projectTask.getProject().getId());
        return projectTaskDto;
    }

    public ProjectTask convertProjectTaskDtoToProjectTask(ProjectTaskDto projectTaskDto) {
        ProjectTask projectTask = new ProjectTask();
        projectTask.setTitle(projectTaskDto.getTitle());
        projectTask.setDescription(projectTaskDto.getDescription());
        projectTask.setDueDate(projectTaskDto.getDueDate());
        projectTask.setStatus(projectTaskDto.getStatus());
        Project project = projectRepository.findById(projectTaskDto.getProjectId())
                .orElseThrow(() -> new ProjectTaskNotFoundException("Project Task not found"));
        projectTask.setProject(project);
        return projectTask;
    }

    public List<ProjectTaskDto> convertProjectTasksToProjectTaskDtos(List<ProjectTask> projectTasks) {
        return projectTasks.stream()
                .map(this::convertProjectTaskToProjectTaskDto)
                .collect(Collectors.toList());
    }
}
