package com.example.table_management_application.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.table_management_application.common.ApiResponse;
import com.example.table_management_application.user.dto.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<List<UserEntity>>> getAllUsers() {
    return ResponseEntity.ok(ApiResponse.success("Get all users successfully", userService.getAllUsers()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<UserEntity>> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.success("Get user by id successfully", userService.getUserById(id)));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<UserEntity>> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
    return ResponseEntity.ok(ApiResponse.success("Update user successfully", userService.updateUser(id, request)));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
