package org.example.project_management.service.data.impl;

import jakarta.transaction.Transactional;
import org.example.project_management.entity.Project;
import org.example.project_management.exception.ProjectNotFoundException;
import org.example.project_management.repository.ProjectRepository;
import org.example.project_management.service.data.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(final ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public Project saveProject(Project project) {
        logger.info("Creating a new project: {}", project.getTitle());
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        logger.info("Getting all projects");
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        logger.info("Getting a project by Id: {}", id);
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
    }

    @Override
    @Transactional
    public Project updateProject(Long id, Project projectDetails) {
        logger.info("Updating existing project");
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        if(projectDetails.getTitle() != null)
            project.setTitle(projectDetails.getTitle());
        else
            project.setTitle(project.getTitle());

        if(projectDetails.getDescription() != null)
            project.setDescription(projectDetails.getDescription());
        else
            project.setDescription(project.getDescription());

        if(projectDetails.getDeadline() != null)
            project.setDeadline(projectDetails.getDeadline());
        else
            project.setDeadline(project.getDeadline());

        if(projectDetails.getStartDate() != null)
            project.setStartDate(projectDetails.getStartDate());
        else
            project.setStartDate(project.getStartDate());

        if(projectDetails.getStatus() != null)
            project.setStatus(projectDetails.getStatus());
        else
            project.setStatus(project.getStatus());

        if(projectDetails.getClient() != null)
            project.setClient(projectDetails.getClient());
        else
            project.setClient(project.getClient());

        if(projectDetails.getInvoices() != null && !projectDetails.getInvoices().isEmpty())
            project.setInvoices(projectDetails.getInvoices());
        else
            project.setInvoices(project.getInvoices());

        if(projectDetails.getTasks() != null && !projectDetails.getTasks().isEmpty())
            project.setTasks(projectDetails.getTasks());
        else
            project.setTasks(project.getTasks());

        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        logger.info("Deleting a project with Id: {}", id);
        Project project = getProjectById(id);
        projectRepository.delete(project);
    }
}
