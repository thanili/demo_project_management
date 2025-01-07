package org.example.project_management.web;

import org.example.project_management.entity.Project;
import org.example.project_management.entity.ProjectTask;
import org.example.project_management.service.ProjectService;
import org.example.project_management.service.ProjectTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hub")
public class ProjectTaskWebController {

    private final ProjectTaskService projectTaskService;
    private final ProjectService projectService;

    public ProjectTaskWebController(ProjectTaskService projectTaskService, ProjectService projectService) {
        this.projectTaskService = projectTaskService;
        this.projectService = projectService;
    }

    @GetMapping("/tasks/{id}/edit")
    public String editProjectTask(Model model, @PathVariable Long id) {
        ProjectTask projectTask = projectTaskService.getProjectTaskById(id);
        model.addAttribute("projectTask", projectTask);
        return "edit-task";
    }

    @PostMapping("/tasks/handleSubmit")
    public String submitForm(ProjectTask projectTask) {
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
        return "redirect:/hub/projects/" + projectTask.getProject().getId() + "/tasks";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteProjectTask(@PathVariable Long id) {
        ProjectTask projectTask = projectTaskService.getProjectTaskById(id);
        Project project = projectTask.getProject();
        projectTaskService.deleteProjectTask(id);
        return "redirect:/hub/projects/" + project.getId() + "/tasks";
    }

    @GetMapping("/projects/{projectId}/tasks/new")
    public String newProjectTaskForm(Model model, @PathVariable long projectId) {
        Project project = projectService.getProjectById(projectId);
        ProjectTask newProjectTask = new ProjectTask();
        newProjectTask.setProject(project);
        model.addAttribute("projectTask", newProjectTask);
        return "edit-task";
    }
}
