package com.example.table_management_application.dto.table;

public class UpdateTableRequest {
  private Long id;
  private String tableName;
  private String category;
  private Double pricePerHour;

  public Long getId() {
    return id;
  }

  public String getTableName() {
    return tableName;
  }

  public String getCategory() {
    return category;
  }

  public Double getPricePerHour() {
    return pricePerHour;
  }
}
