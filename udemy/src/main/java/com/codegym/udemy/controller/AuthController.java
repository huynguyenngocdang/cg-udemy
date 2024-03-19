package com.codegym.udemy.controller;

import com.codegym.udemy.dto.AppUserDto;
import com.codegym.udemy.dto.AuthRequestDto;
import com.codegym.udemy.dto.JwtResponseDto;
import com.codegym.udemy.service.impl.JwtServiceImpl;
import com.codegym.udemy.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final JwtServiceImpl jwtServiceImpl;
@Autowired
    public AuthController(AuthenticationManager authenticationManager, AppUserService appUserService, JwtServiceImpl jwtServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.appUserService = appUserService;
        this.jwtServiceImpl = jwtServiceImpl;
    }

    @PostMapping("/login")
    public JwtResponseDto AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            String accessToken = jwtServiceImpl.GenerateToken(authRequestDTO.getUsername());
            return JwtResponseDto.builder()
                    .accessToken(accessToken)
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUserDto appUserDto) {
        appUserService.saveUser(appUserDto);
        return ResponseEntity.ok("User registered successfully.");
    }
}
