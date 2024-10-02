package org.example.project_management.service.data;

import org.example.project_management.entity.ProjectTask;

import java.util.List;

public interface ProjectTaskService {
    ProjectTask saveProjectTask(ProjectTask projectTask);
    List<ProjectTask> getAllProjectTasks();
    ProjectTask getProjectTaskById(Long id);
    ProjectTask updateProjectTask(Long id, ProjectTask projectTaskDetails);
    void deleteProjectTask(Long id);
}
