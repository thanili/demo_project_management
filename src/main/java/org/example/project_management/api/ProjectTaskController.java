package org.example.project_management.api;

import jakarta.validation.Valid;
import org.example.project_management.dto.ProjectTaskDto;
import org.example.project_management.entity.ProjectTask;
import org.example.project_management.helper.EntityDtoConverter;
import org.example.project_management.service.ProjectTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-tasks")
public class ProjectTaskController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectTaskController.class);

    private final ProjectTaskService projectTaskService;
    private final EntityDtoConverter entityDtoConverter;

    @Autowired
    public ProjectTaskController(final ProjectTaskService projectTaskService,
                            final EntityDtoConverter entityDtoConverter) {
        this.projectTaskService = projectTaskService;
        this.entityDtoConverter = entityDtoConverter;
    }

    @PostMapping
    public ResponseEntity<ProjectTaskDto> createProjectTask(@Valid @RequestBody ProjectTaskDto projectTaskDto) {
        logger.info("Request to create a new project task received: {}", projectTaskDto.getTitle());
        ProjectTask savedProjectTask = projectTaskService.saveProjectTask(entityDtoConverter.convertProjectTaskDtoToProjectTask(projectTaskDto));
        return ResponseEntity.ok(entityDtoConverter.convertProjectTaskToProjectTaskDto(savedProjectTask));
    }

    @GetMapping
    public ResponseEntity<List<ProjectTaskDto>> getAllProjectTasks() {
        logger.info("Request to get all projects tasks received");
        List<ProjectTask> projectTasks = projectTaskService.getAllProjectTasks();
        return ResponseEntity.ok(entityDtoConverter.convertProjectTasksToProjectTaskDtos(projectTasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectTaskDto> getProjectTaskById(@PathVariable Long id) {
        logger.info("Request to get project task by Id received: {}", id);
        ProjectTask projectTask = projectTaskService.getProjectTaskById(id);
        return ResponseEntity.ok(entityDtoConverter.convertProjectTaskToProjectTaskDto(projectTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectTaskDto> updateProjectTask(@PathVariable Long id, @Valid @RequestBody ProjectTaskDto projectTaskDtoDetails) {
        logger.info("Request to update an existing project task received: {}", projectTaskDtoDetails.getTitle());
        ProjectTask projectTaskDetails = entityDtoConverter.convertProjectTaskDtoToProjectTask(projectTaskDtoDetails);
        ProjectTask updatedProjectTask = projectTaskService.updateProjectTask(id, projectTaskDetails);
        return ResponseEntity.ok(entityDtoConverter.convertProjectTaskToProjectTaskDto(updatedProjectTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable Long id) {
        logger.info("Request to delete project task by Id received: {}", id);
        projectTaskService.deleteProjectTask(id);
        return ResponseEntity.ok("Project Task deleted successfully");
    }
}
