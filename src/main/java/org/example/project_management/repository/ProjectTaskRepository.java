package org.example.project_management.repository;

import org.example.project_management.entity.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
}