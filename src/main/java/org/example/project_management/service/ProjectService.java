package org.example.project_management.service;

import org.example.project_management.entity.Project;

import java.util.List;

public interface ProjectService {
    Project saveProject(Project project);
    List<Project> getAllProjects();
    Project getProjectById(Long id);
    Project updateProject(Long id, Project projectDetails);
    void deleteProject(Long id);
    List<Project> getClientProjectsByClientId(Long clientId);
}
