package com.example.table_management_application.table;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.table_management_application.exception.ResourceNotFoundException;
import com.example.table_management_application.table.dto.UpdateTableRequest;

@Service
public class TableService {

  @Autowired
  private TableRepository tableRepository;

  public TableEntity createTable(TableEntity table) {
    if (table.getTableName() == null || table.getTableName().trim().isEmpty()) {
      throw new IllegalArgumentException("The table name is required");
    }

    if (tableRepository.existsByTableName(table.getTableName())) {
      throw new IllegalArgumentException("The table name already exists");
    }

    if (table.getCategory() == null || table.getCategory().trim().isEmpty()) {
      throw new IllegalArgumentException("The table category is required");
    }

    if (table.getPricePerHour() == null || table.getPricePerHour() <= 0) {
      throw new IllegalArgumentException("The table price per hour must be greater than 0");
    }

    table.setStatus(TableStatus.AVAILABLE);

    return tableRepository.save(table);
  }

  public TableEntity updateTable(UpdateTableRequest request) {
    // Throws ResourceNotFoundException if the table is not found
    TableEntity existing = tableRepository.findTableById(request.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + request.getId()));

    // Validate update fields
    if (request.getTableName() != null && request.getTableName().trim().isEmpty()) {
      throw new IllegalArgumentException("The table name cannot be blank");
    }

    if (request.getPricePerHour() != null && request.getPricePerHour() <= 0) {
      throw new IllegalArgumentException("The table price per hour must be greater than 0");
    }

    // Apply partial updates
    if (request.getTableName() != null) {
      existing.setTableName(request.getTableName());
    }
    if (request.getCategory() != null) {
      existing.setCategory(request.getCategory());
    }
    if (request.getPricePerHour() != null) {
      existing.setPricePerHour(request.getPricePerHour());
    }

    return tableRepository.save(existing);
  }

  public void deleteTable(Long id) {
    TableEntity table = tableRepository.findTableById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
    tableRepository.delete(table);
  }

  public TableEntity findTableById(Long id) {
    return tableRepository.findTableById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
  }

  public List<TableEntity> findAllTables() {
    return tableRepository.findAll();
  }

  public List<TableEntity> findTablesByStatus(TableStatus status) {
    return tableRepository.findTablesByStatus(status);
  }

  public List<TableEntity> findTablesByCategory(String category) {
    return tableRepository.findTablesByCategory(category);
  }

  @Transactional
  public TableEntity updateTableStatus(Long id, TableStatus newTableStatus) {
    TableEntity table = tableRepository.findTableById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
    table.setStatus(newTableStatus);
    return tableRepository.save(table);
  }
}
