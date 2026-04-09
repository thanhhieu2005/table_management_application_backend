package com.example.table_management_application.common;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  private int status;
  private String message;
  private T data;

  // Factory method for success with data
  public static <T> ApiResponse<T> success(String message, T data) {
    return ApiResponse.<T>builder()
        .status(HttpStatus.OK.value())
        .message(message)
        .data(data)
        .build();
  }

  // Factory method for success without data (e.g. delete)
  public static <T> ApiResponse<T> success(String message) {
    return ApiResponse.<T>builder()
        .status(HttpStatus.OK.value())
        .message(message)
        .build();
  }

  // Factory method for created (201)
  public static <T> ApiResponse<T> created(String message, T data) {
    return ApiResponse.<T>builder()
        .status(HttpStatus.CREATED.value())
        .message(message)
        .data(data)
        .build();
  }

  // Factory method for error responses
  public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message) {
    return ApiResponse.<T>builder()
        .status(httpStatus.value())
        .message(message)
        .build();
  }
}
