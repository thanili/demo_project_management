package org.example.project_management.web;

import org.example.project_management.entity.Client;
import org.example.project_management.entity.Invoice;
import org.example.project_management.entity.Project;
import org.example.project_management.entity.ProjectTask;
import org.example.project_management.service.ClientService;
import org.example.project_management.service.InvoiceService;
import org.example.project_management.service.ProjectService;
import org.example.project_management.service.ProjectTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hub")
public class ProjectWebController {

    private final ProjectTaskService projectTaskService;
    private final InvoiceService invoiceService;
    private final ProjectService projectService;
    private final ClientService clientService;

    public ProjectWebController(ProjectTaskService projectTaskService, InvoiceService invoiceService, ProjectService projectService, ClientService clientService) {
        this.projectTaskService = projectTaskService;
        this.invoiceService = invoiceService;
        this.projectService = projectService;
        this.clientService = clientService;
    }

    @GetMapping("/projects/{id}/tasks")
    public String getProjectTasks(Model model, @PathVariable Long id) {
        List<ProjectTask> projectTasks = projectTaskService.getProjectTasksByProjectId(id);
        Project project = projectService.getProjectById(id);
        model.addAttribute("projectTasks", projectTasks);
        model.addAttribute("project", project);
        return "project-tasks";
    }

    @GetMapping("/projects/{id}/invoices")
    public String getProjectInvoices(Model model, @PathVariable Long id) {
        List<Invoice> invoices = invoiceService.getInvoicesByProjectId(id);
        Project project = projectService.getProjectById(id);
        model.addAttribute("invoices", invoices);
        model.addAttribute("project", project);
        return "project-invoices";
    }

    @GetMapping("/projects/{id}/edit")
    public String editProject(Model model, @PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "edit-project";
    }

    @PostMapping("/projects/handleSubmit")
    public String submitForm(Project project) {
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
        return "redirect:/hub/clients/" + project.getClient().getId() + "/projects";
    }

    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        Client client = project.getClient();
        projectService.deleteProject(id);
        return "redirect:/hub/clients/" + client.getId() + "/projects";
    }

    @GetMapping("/clients/{clientId}/projects/new")
    public String newProjectForm(Model model, @PathVariable long clientId) {
        Client client = clientService.getClientById(clientId);
        Project newProject = new Project();
        newProject.setClient(client);
        model.addAttribute("project", newProject);
        return "edit-project";
    }
}
