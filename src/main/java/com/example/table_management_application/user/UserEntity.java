package com.example.table_management_application.user;

import java.util.Set;

import com.example.table_management_application.role_permission.RoleEnity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<RoleEnity> roles;

    @PrePersist
    public void setDefaultRole() {
      this.roles = Set.of(RoleEnity.builder().name("ROLE_STAFF").build());
    }
}
