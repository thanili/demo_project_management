package org.example.project_management.web;

import jakarta.validation.Valid;
import org.example.project_management.entity.ProjectTask;
import org.example.project_management.service.ProjectCoordinatorService;
import org.example.project_management.service.ProjectTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hub")
public class ProjectTaskWebController {

    private final ProjectTaskService projectTaskService;
    private final ProjectCoordinatorService projectCoordinatorService;

    public ProjectTaskWebController(ProjectTaskService projectTaskService,
                                    ProjectCoordinatorService projectCoordinatorService) {
        this.projectTaskService = projectTaskService;
        this.projectCoordinatorService = projectCoordinatorService;
    }

    @GetMapping("/tasks/{id}/edit")
    public String editProjectTask(Model model, @PathVariable Long id) {
        model.addAttribute("projectTask", projectTaskService.getProjectTaskById(id));
        return "edit-task";
    }

    @PostMapping("/tasks/save")
    public String saveProjectTask(@Valid ProjectTask projectTask, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "edit-task";

        projectCoordinatorService.handleSubmitProjectTask(projectTask);
        return "redirect:/hub/projects/" + projectTask.getProject().getId() + "/tasks";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteProjectTask(@PathVariable Long id) {
        Long projectId = projectTaskService.getProjectTaskById(id).getProject().getId();
        projectTaskService.deleteProjectTask(id);
        return "redirect:/hub/projects/" + projectId + "/tasks";
    }

    @GetMapping("/projects/{projectId}/tasks/new")
    public String newProjectTaskForm(Model model, @PathVariable long projectId) {
        model.addAttribute("projectTask", projectCoordinatorService.createProjectTask(projectId));
        return "edit-task";
    }
}
