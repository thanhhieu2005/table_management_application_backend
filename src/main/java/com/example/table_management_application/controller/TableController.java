package com.example.table_management_application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.table_management_application.dto.table.UpdateTableRequest;
import com.example.table_management_application.models.table.TableEntity;
import com.example.table_management_application.models.table.TableStatus;
import com.example.table_management_application.service.TableService;

@RestController
@RequestMapping("/api/v1/tables")
public class TableController {
  @Autowired
  private TableService tableService;

  @GetMapping
  public List<TableEntity> getAllTable() {
    return tableService.findAllTables();
  }

  @PostMapping
  public ResponseEntity<TableEntity> create(@RequestBody TableEntity table) {
    return ResponseEntity.ok(tableService.createTable(table));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<TableEntity> updateStatus(@PathVariable Long id, @RequestParam TableStatus newTableStatus) {
    return ResponseEntity.ok(tableService.updateTableStatus(id, newTableStatus));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<TableEntity> update(@RequestBody UpdateTableRequest request) {
    return ResponseEntity.ok(tableService.updateTable(request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable Long id) {
    tableService.deleteTable(id);
    return ResponseEntity.ok("Delete table successfully");
  }
}
