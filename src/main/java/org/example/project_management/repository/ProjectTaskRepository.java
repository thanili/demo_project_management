package org.example.project_management.repository;

import org.example.project_management.entity.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
    // Method to find all project tasks by project ID
    @Query("SELECT p FROM ProjectTask p WHERE p.project.id = ?1")
    Optional<List<ProjectTask>> findAllByProjectId(Long projectId);
}