package com.example.table_management_application.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.table_management_application.auth.dto.AuthResponse;
import com.example.table_management_application.auth.dto.LoginRequest;
import com.example.table_management_application.auth.dto.RegisterRequest;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public AuthResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
    return authService.login(request, response);
  }

  @PostMapping("/register")
  public AuthResponse register(@RequestBody RegisterRequest request, HttpServletResponse response) {
    return authService.register(request, response);
  }

  @PostMapping("/logout")
  public void logout(HttpServletResponse response) {
    authService.logout(response);
  }
}
