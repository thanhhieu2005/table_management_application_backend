package com.example.table_management_application.auth;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.table_management_application.auth.dto.AuthResponse;
import com.example.table_management_application.auth.dto.LoginRequest;
import com.example.table_management_application.auth.dto.RegisterRequest;
import com.example.table_management_application.exception.ResourceNotFoundException;
import com.example.table_management_application.role_permission.RoleEnity;
import com.example.table_management_application.role_permission.RoleRepository;
import com.example.table_management_application.security.custom.CustomUserDetails;
import com.example.table_management_application.security.jwt.JwtService;
import com.example.table_management_application.user.UserEntity;
import com.example.table_management_application.user.UserRepository;
import com.example.table_management_application.user.dto.UserResponse;
import com.example.table_management_application.util.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthResponse login(LoginRequest request, HttpServletResponse response) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
        request.getUsername(),
        request.getPassword()
      )
    );

    UserEntity user = userRepository.findByUsername(request.getUsername())
      .orElseThrow(() -> new IllegalArgumentException("User not found"));

    String token = generateTokenForUser(user);

    CookieUtil.addJwtCookie(response, token);

    return AuthResponse.builder()
      .token(token)
      .username(user.getUsername())
      .build();
  }

  public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
    if (!request.getPassword().equals(request.getConfirmPassword())) {
      throw new IllegalArgumentException("Password and confirm password do not match");
    }

    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }

    UserEntity user = new UserEntity();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    RoleEnity staffRole = roleRepository.findByName("ROLE_STAFF")
        .orElseThrow(() -> new IllegalStateException("ROLE_STAFF not found. Ensure DataInitializer has run."));
    user.setRoles(Set.of(staffRole));

    userRepository.save(user);

    String token = generateTokenForUser(user);

    CookieUtil.addJwtCookie(response, token);

    return AuthResponse.builder()
        .token(token)
        .username(user.getUsername())
        .build();
  }
  
  private String generateTokenForUser(UserEntity user) {
    CustomUserDetails userDetails = new CustomUserDetails(user);
    return jwtService.generateToken(userDetails);
  }

  public void logout(HttpServletResponse response) {
    CookieUtil.clearJwtCookie(response);
  }

  public UserResponse getMe(CustomUserDetails userDetails) {
    UserEntity user = userRepository.findByUsername(userDetails.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    
    Set<String> roles = user.getRoles().stream()
        .map(RoleEnity::getName)
        .collect(Collectors.toSet());

    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .fullName(user.getFullName())
        .roles(roles)
        .build();
  }
}
