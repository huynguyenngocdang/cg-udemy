package com.codegym.udemy.service;

import com.codegym.udemy.dto.AppUserDto;
import com.codegym.udemy.entity.AppUser;

public interface AppUserService {
    boolean saveUser(AppUserDto appUserDto);
    AppUser findByUsername(String username);
    AppUserDto getUserById(Long userId);
    boolean editUser(Long userId, AppUserDto appUserDto);
    boolean deleteUser(Long userId);
}
