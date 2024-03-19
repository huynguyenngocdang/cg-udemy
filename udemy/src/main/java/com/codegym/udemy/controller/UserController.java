package com.codegym.udemy.controller;

import com.codegym.udemy.dto.AppUserDto;
import com.codegym.udemy.entity.AppUser;
import com.codegym.udemy.repository.AppUserRepository;
import com.codegym.udemy.service.AppUserService;
import com.codegym.udemy.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codegym.udemy.constant.VarConstant.AUTHORIZATION;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final AppUserService appUserService;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;

    @Autowired
    public UserController(AppUserService appUserService, JwtService jwtService,
                          AppUserRepository appUserRepository) {
        this.appUserService = appUserService;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/get")
    public ResponseEntity<AppUserDto> getUserByUserId(@RequestHeader(AUTHORIZATION) String token) {
        String username = jwtService.extractUsername(token);
        AppUser user = appUserRepository.findByUsername(username);
        AppUserDto appUserDto = appUserService.getUserById(user.getId());
        if (appUserDto != null) {
            return ResponseEntity.ok(appUserDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(
            @RequestHeader(AUTHORIZATION) String token,
            @RequestBody AppUserDto appUserDto
    ) {
        boolean created = appUserService.saveUser(appUserDto);
        return ResponseEntity.status(created ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<String> editUser(@RequestHeader(AUTHORIZATION) String token,
                                           @RequestBody AppUserDto appUserDto) {
        Long userId = jwtService.extractUserId(token);
        boolean edited = appUserService.editUser(userId, appUserDto);
        return ResponseEntity.status(edited ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserByUserId(@RequestHeader(AUTHORIZATION) String token) {
        Long userId = jwtService.extractUserId(token);
        boolean deleted = appUserService.deleteUser(userId);
        return ResponseEntity.status(deleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).build();
    }
}

