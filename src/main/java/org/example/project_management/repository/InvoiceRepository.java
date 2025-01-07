package org.example.project_management.repository;

import org.example.project_management.entity.Invoice;
import org.example.project_management.entity.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // Method to find all project invoices by project ID
    @Query("SELECT p FROM Invoice p WHERE p.project.id = ?1")
    Optional<List<Invoice>> findAllByProjectId(Long projectId);
}