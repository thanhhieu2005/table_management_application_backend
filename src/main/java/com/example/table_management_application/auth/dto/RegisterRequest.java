package com.example.table_management_application.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
  
  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Password is required")
  @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
    message = "Password must be at least 8 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one special character"
  )
  private String password;

  @NotBlank(message = "Confirm password is required")
  private String confirmPassword;

}
