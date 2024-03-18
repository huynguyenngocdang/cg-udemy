package com.codegym.udemy.service.impl;

import com.codegym.udemy.constant.VarConstant;
import com.codegym.udemy.dto.AppUserDto;
import com.codegym.udemy.dto.InstructorDto;
import com.codegym.udemy.entity.AppUser;
import com.codegym.udemy.entity.Course;
import com.codegym.udemy.entity.Instructor;
import com.codegym.udemy.entity.Role;
import com.codegym.udemy.repository.AppUserRepository;
import com.codegym.udemy.repository.RoleRepository;
import com.codegym.udemy.service.AppUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    private AppUser convertToAppUser(AppUserDto appUserDto) {
        AppUser appUser = modelMapper.map(appUserDto, AppUser.class);
        if (appUserDto.getId() != null) {
            Optional<AppUser> optionalAppUser = appUserRepository.findById(appUserDto.getId());
            if (optionalAppUser.isPresent()) {
                appUser = optionalAppUser.get();
            }
        }
        return appUser;
    }

    private AppUserDto convertToAppUserDto(AppUser appUser) {
        AppUserDto appUserDto = modelMapper.map(appUser, AppUserDto.class);
        if (appUser.getId() != null) {
            appUserDto.setId(appUser.getId());
        }
        return appUserDto;
    }

    @Override
    public void saveUser(AppUserDto appUserDto) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserDto.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUserDto.getPassword()));
        appUser.setEmail(appUserDto.getEmail());

        Role role = roleRepository.findByRoleType(VarConstant.ROLE_TYPE_USER);
        appUser.setRoles(Arrays.asList(role));
        appUser.setActive(true);
        appUserRepository.save(appUser);
    }

//    @Override
//    public void saveUser(AppUserDto appUserDto) {
//        AppUser appUser = convertToAppUser(appUserDto);
//        Role role = roleRepository.findByRoleType(VarConstant.ROLE_TYPE_USER);
//        appUser.setRoles(Arrays.asList(role));
//        appUser.setActive(true);
//        appUserRepository.save(appUser);
//    }

    @Override
    public AppUser findByUsername(String username) {
        return null;
    }

    @Override
    public AppUserDto getUserById(Long userId) {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(userId);
        if ((optionalAppUser.isPresent())) {
            AppUser appUser = optionalAppUser.get();
            return convertToAppUserDto(appUser);
        } else {
            return null;
        }
    }

    @Override
    public void editUser(Long userId, AppUserDto appUserDto) {
        AppUser existingAppUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("AppUser not found with userId: " + userId));

        AppUser updatedUser = convertToAppUser(appUserDto);
        updatedUser.setId(existingAppUser.getId());
        appUserRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(userId);
        if (optionalAppUser.isPresent()) {
            appUserRepository.deleteById(userId);
        } else {
            throw new IllegalArgumentException("AppUser not found with userId: " + userId);
        }
    }
}
