package com.example.table_management_application.table;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.table_management_application.common.ApiResponse;
import com.example.table_management_application.table.dto.UpdateTableRequest;

@RestController
@RequestMapping("/api/v1/tables")
public class TableController {
  @Autowired
  private TableService tableService;

  @PreAuthorize("hasAuthority('TABLE_READ')")
  @GetMapping
  public ApiResponse<List<TableEntity>> getAllTable() {
    return ApiResponse.success("Get all tables successfully", tableService.findAllTables());
  }

  @PreAuthorize("hasAuthority('TABLE_WRITE')")
  @PostMapping
  public ApiResponse<TableEntity> create(@RequestBody TableEntity table) {
    return ApiResponse.created("Create table successfully", tableService.createTable(table));
  }

  @PreAuthorize("hasAuthority('TABLE_WRITE')")
  @PatchMapping("/{id}/status")
  public ApiResponse<TableEntity> updateStatus(@PathVariable Long id, @RequestParam TableStatus newTableStatus) {
    return ApiResponse.success("Update table status successfully", tableService.updateTableStatus(id, newTableStatus));
  }

  @PreAuthorize("hasAuthority('TABLE_WRITE')")
  @PatchMapping("/{id}")
  public ApiResponse<TableEntity> update(@RequestBody UpdateTableRequest request) {
    return ApiResponse.success("Update table successfully", tableService.updateTable(request));
  }

  @PreAuthorize("hasAuthority('TABLE_DELETE')")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    tableService.deleteTable(id);
    return ApiResponse.success("Delete table successfully");
  }
}
