package com.example.table_management_application.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.table_management_application.auth.dto.AuthResponse;
import com.example.table_management_application.auth.dto.LoginRequest;
import com.example.table_management_application.auth.dto.RegisterRequest;
import com.example.table_management_application.role_permission.RoleRepository;
import com.example.table_management_application.security.custom.CustomUserDetails;
import com.example.table_management_application.security.jwt.JwtService;
import com.example.table_management_application.user.UserEntity;
import com.example.table_management_application.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthResponse login(LoginRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
        request.getUsername(),
        request.getPassword()
      )
    );

    UserEntity user = userRepository.findByUsername(request.getUsername())
      .orElseThrow(() -> new IllegalArgumentException("User not found"));

    CustomUserDetails userDetails = new CustomUserDetails(user);

    String token = jwtService.generateToken(userDetails);

    return AuthResponse.builder()
      .token(token)
      .username(user.getUsername())
      .build();
  }

  public AuthResponse register(RegisterRequest request) {
    if (!request.getPassword().equals(request.getConfirmPassword())) {
      throw new IllegalArgumentException("Password and confirm password do not match");
    }

    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }

    UserEntity user = new UserEntity();
    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());
    user.setDefaultRole();
    
    userRepository.save(user);

    CustomUserDetails userDetails = new CustomUserDetails(user);
    String token = jwtService.generateToken(userDetails);

    return AuthResponse.builder()
            .token(token)
            .username(user.getUsername())
            .build();
  }
}
