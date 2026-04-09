package com.example.table_management_application.role_permission;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEnity, Long> {
  Optional<RoleEnity> findByName(String name);
}
