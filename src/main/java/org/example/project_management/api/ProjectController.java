package org.example.project_management.api;

import jakarta.validation.Valid;
import org.example.project_management.dto.ProjectDto;
import org.example.project_management.entity.Project;
import org.example.project_management.helper.EntityDtoConverter;
import org.example.project_management.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;
    private final EntityDtoConverter entityDtoConverter;

    @Autowired
    public ProjectController(final ProjectService projectService,
                             final EntityDtoConverter entityDtoConverter) {
        this.projectService = projectService;
        this.entityDtoConverter = entityDtoConverter;
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectDto projectDto) {
        logger.info("Request to create a new project received: {}", projectDto.getTitle());
        Project savedProject = projectService.saveProject(entityDtoConverter.convertProjectDtoToProject(projectDto));
        return ResponseEntity.ok(entityDtoConverter.convertProjectToProjectDto(savedProject));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        logger.info("Request to get all projects received");
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(entityDtoConverter.convertProjectsToProjectDtos(projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        logger.info("Request to get project by Id received: {}", id);
        Project invoice = projectService.getProjectById(id);
        return ResponseEntity.ok(entityDtoConverter.convertProjectToProjectDto(invoice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDto projectDtoDetails) {
        logger.info("Request to update an existing project received: {}", projectDtoDetails.getTitle());
        Project projectDetails = entityDtoConverter.convertProjectDtoToProject(projectDtoDetails);
        Project updateProject = projectService.updateProject(id, projectDetails);
        return ResponseEntity.ok(entityDtoConverter.convertProjectToProjectDto(updateProject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        logger.info("Request to delete project by Id received: {}", id);
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted successfully");
    }
}
