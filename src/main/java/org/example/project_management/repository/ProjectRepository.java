package org.example.project_management.repository;

import org.example.project_management.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Method to find all projects by client ID
    @Query("SELECT p FROM Project p WHERE p.client.id = ?1")
    Optional<List<Project>> findAllByClientId(Long clientId);
}