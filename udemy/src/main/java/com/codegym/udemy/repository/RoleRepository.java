package com.codegym.udemy.repository;

import com.codegym.udemy.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(String roleType);
    boolean existsByRoleType(String roleType);
}
