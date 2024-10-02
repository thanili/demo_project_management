package org.example.project_management.repository.auth;

import org.example.project_management.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}