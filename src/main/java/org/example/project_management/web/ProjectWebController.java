package org.example.project_management.web;

import jakarta.validation.Valid;
import org.example.project_management.entity.Project;
import org.example.project_management.service.ProjectCoordinatorService;
import org.example.project_management.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hub")
public class ProjectWebController {

    private final ProjectService projectService;
    private final ProjectCoordinatorService projectCoordinatorService;

    public ProjectWebController(ProjectService projectService, ProjectCoordinatorService projectCoordinatorService) {
        this.projectService = projectService;
        this.projectCoordinatorService = projectCoordinatorService;
    }

    @GetMapping("/projects/{id}/tasks")
    public String getProjectTasks(Model model, @PathVariable Long id) {
        model.addAttribute("projectTasks", projectCoordinatorService.getProjectTasksByProjectId(id));
        model.addAttribute("project", projectService.getProjectById(id));
        return "project-tasks";
    }

    @GetMapping("/projects/{id}/invoices")
    public String getProjectInvoices(Model model, @PathVariable Long id) {
        model.addAttribute("invoices", projectCoordinatorService.getInvoicesByProjectId(id));
        model.addAttribute("project", projectService.getProjectById(id));
        return "project-invoices";
    }

    @GetMapping("/projects/{id}/edit")
    public String editProject(Model model, @PathVariable Long id) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "edit-project";
    }

    @PostMapping("/projects/save")
    public String saveProject(@Valid Project project, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "edit-project";

        projectCoordinatorService.handleSubmitProject(project);
        return "redirect:/hub/clients/" + project.getClient().getId() + "/projects";
    }

    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        Long clientId = projectService.getProjectById(id).getClient().getId();
        projectService.deleteProject(id);
        return "redirect:/hub/clients/" + clientId + "/projects";
    }

    @GetMapping("/clients/{clientId}/projects/new")
    public String newProjectForm(Model model, @PathVariable long clientId) {
        model.addAttribute("project", projectCoordinatorService.createClientProject(clientId));
        return "edit-project";
    }
}
