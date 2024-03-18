package com.codegym.udemy.service.impl;

import com.codegym.udemy.entity.Role;
import com.codegym.udemy.repository.RoleRepository;
import com.codegym.udemy.service.RoleInitializationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleInitializationServiceImpl implements RoleInitializationService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleInitializationServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        initializeRoles();
    }

    private void initializeRoles() {
        // Check if "USER" role exists, if not, create and save it
        if (!roleRepository.existsByRoleType("USER")) {
            Role userRole = new Role();
            userRole.setRoleType("USER");
            roleRepository.save(userRole);
        }

        // Check if "ADMIN" role exists, if not, create and save it
        if (!roleRepository.existsByRoleType("ADMIN")) {
            Role adminRole = new Role();
            adminRole.setRoleType("ADMIN");
            roleRepository.save(adminRole);
        }
    }
}
