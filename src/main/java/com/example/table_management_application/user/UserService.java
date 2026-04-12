package com.example.table_management_application.user;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.table_management_application.role_permission.RoleEnity;
import com.example.table_management_application.role_permission.RoleRepository;
import com.example.table_management_application.user.dto.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public List<UserEntity> getAllUsers() {
    return userRepository.findAll();
  }

  public UserEntity getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
  }

  public UserEntity updateUser(Long id, UserUpdateRequest request) {
    UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    user.setFullName(request.getFullName());

    RoleEnity staffRole = roleRepository.findByName(request.getRole())
        .orElseThrow(() -> new IllegalStateException("Role not found."));
    user.setRoles(Set.of(staffRole));
    return userRepository.save(user);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
