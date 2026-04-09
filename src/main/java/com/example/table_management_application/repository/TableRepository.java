package com.example.table_management_application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.table_management_application.models.table.TableEntity;
import com.example.table_management_application.models.table.TableStatus;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

  // Find a table by id — returns Optional so .orElseThrow() can be used
  Optional<TableEntity> findTableById(Long id);

  // Find tables by status
  List<TableEntity> findTablesByStatus(TableStatus status);

  // Find tables by category
  List<TableEntity> findTablesByCategory(String category);

  // Check name exist
  boolean existsByTableName(String tableName);
}
