package com.example.table_management_application.config;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.table_management_application.role_permission.Permission;
import com.example.table_management_application.role_permission.RoleEnity;
import com.example.table_management_application.role_permission.RoleRepository;
import com.example.table_management_application.user.UserEntity;
import com.example.table_management_application.user.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * DataInitializer — runs once on every application startup.
 * Creates default roles (ROLE_ADMIN, ROLE_STAFF) with their permissions,
 * and seeds one Admin user and one Staff user if they don't already exist.
 *
 * All operations are idempotent: safe to run on every restart.
 *
 * Default credentials can be overridden in application.properties:
 *   app.init.admin-username / app.init.admin-password
 *   app.init.staff-username / app.init.staff-password
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // --- Configurable defaults (override in application.properties) ---
  @Value("${app.init.admin-username:admin}")
  private String adminUsername;

  @Value("${app.init.admin-password:admin@123}")
  private String adminPassword;

  @Value("${app.init.staff-username:staff}")
  private String staffUsername;

  @Value("${app.init.staff-password:staff@123}")
  private String staffPassword;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    log.info("=== DataInitializer: seeding roles and default users ===");

    // 1. Upsert ROLE_ADMIN (full permissions)
    RoleEnity adminRole = upsertRole("ROLE_ADMIN", new HashSet<>(Set.of(
        Permission.TABLE_READ,
        Permission.TABLE_WRITE,
        Permission.TABLE_DELETE,
        Permission.USER_READ,
        Permission.USER_WRITE,
        Permission.USER_DELETE
    )));

    // 2. Upsert ROLE_STAFF (read table and user)
    RoleEnity staffRole = upsertRole("ROLE_STAFF", new HashSet<>(Set.of(
        Permission.TABLE_READ,
        Permission.USER_READ,
        Permission.USER_WRITE
    )));

    // 3. Seed admin user
    upsertUser(adminUsername, adminPassword, new HashSet<>(Set.of(adminRole)));

    // 4. Seed staff user
    upsertUser(staffUsername, staffPassword, new HashSet<>(Set.of(staffRole)));

    log.info("=== DataInitializer: done ===");
  }

  /** Finds or creates a role, updating its permissions if it already exists. */
  private RoleEnity upsertRole(String name, Set<Permission> permissions) {
    return roleRepository.findByName(name).map(existing -> {
      existing.setPermissions(permissions);
      RoleEnity saved = roleRepository.save(existing);
      log.info("Role '{}' already exists — permissions updated.", name);
      return saved;
    }).orElseGet(() -> {
      RoleEnity newRole = RoleEnity.builder()
          .name(name)
          .permissions(permissions)
          .build();
      RoleEnity saved = roleRepository.save(newRole);
      log.info("Role '{}' created with permissions: {}", name, permissions);
      return saved;
    });
  }

  /** Creates a user only if the username doesn't already exist. */
  private void upsertUser(String username, String rawPassword, Set<RoleEnity> roles) {
    if (userRepository.existsByUsername(username)) {
      log.info("User '{}' already exists — skipping.", username);
      return;
    }
    UserEntity user = UserEntity.builder()
        .username(username)
        .password(passwordEncoder.encode(rawPassword))
        .roles(roles)
        .build();
    userRepository.save(user);
    log.info("User '{}' created with roles: {}", username,
        roles.stream().map(RoleEnity::getName).toList());
  }
}
