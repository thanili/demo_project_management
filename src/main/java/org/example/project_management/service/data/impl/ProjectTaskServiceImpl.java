package org.example.project_management.service.data.impl;

import jakarta.transaction.Transactional;
import org.example.project_management.entity.ProjectTask;
import org.example.project_management.exception.ProjectTaskNotFoundException;
import org.example.project_management.repository.ProjectTaskRepository;
import org.example.project_management.service.data.ProjectTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectTaskServiceImpl.class);

    private final ProjectTaskRepository projectTaskRepository;

    @Autowired
    public ProjectTaskServiceImpl(final ProjectTaskRepository projectTaskRepository) {
        this.projectTaskRepository = projectTaskRepository;
    }

    @Override
    @Transactional
    public ProjectTask saveProjectTask(ProjectTask projectTask) {
        logger.info("Creating a new project task: {}", projectTask.getTitle());
        return projectTaskRepository.save(projectTask);
    }

    @Override
    public List<ProjectTask> getAllProjectTasks() {
        logger.info("Getting all projects tasks");
        return projectTaskRepository.findAll();
    }

    @Override
    public ProjectTask getProjectTaskById(Long id) {
        logger.info("Getting a project task by Id: {}", id);
        return projectTaskRepository.findById(id)
                .orElseThrow(() -> new ProjectTaskNotFoundException("Project task not found"));
    }

    @Override
    @Transactional
    public ProjectTask updateProjectTask(Long id, ProjectTask projectTaskDetails) {
        logger.info("Updating existing project task");
        ProjectTask projectTask = projectTaskRepository.findById(id)
                .orElseThrow(() -> new ProjectTaskNotFoundException("Project task not found"));

        if(projectTaskDetails.getTitle() != null)
            projectTask.setTitle(projectTaskDetails.getTitle());
        if(projectTaskDetails.getDueDate() != null)
            projectTask.setDueDate(projectTaskDetails.getDueDate());
        if(projectTaskDetails.getProject() != null)
            projectTask.setProject(projectTaskDetails.getProject());
        if(projectTaskDetails.getStatus() != null)
            projectTask.setStatus(projectTaskDetails.getStatus());
        if(projectTaskDetails.getDescription() != null)
            projectTask.setDescription(projectTaskDetails.getDescription());

        return projectTaskRepository.save(projectTask);
    }

    @Override
    @Transactional
    public void deleteProjectTask(Long id) {
        logger.info("Deleting a project task with Id: {}", id);
        ProjectTask projectTask = getProjectTaskById(id);
        projectTaskRepository.delete(projectTask);
    }
}
