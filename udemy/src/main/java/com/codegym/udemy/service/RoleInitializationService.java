package com.codegym.udemy.service;

import jakarta.annotation.PostConstruct;

public interface RoleInitializationService {
    @PostConstruct
    void init() ;
}
