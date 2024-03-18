package com.codegym.udemy.controller;

import com.codegym.udemy.dto.InstructorDto;
import com.codegym.udemy.security.JwtService;
import com.codegym.udemy.service.InstructorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/instructor")
public class InstructorController {
    private final InstructorService instructorService;
    private final JwtService jwtService;
@Autowired
    public InstructorController(InstructorService instructorService, JwtService jwtService) {
        this.instructorService = instructorService;
        this.jwtService = jwtService;
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<InstructorDto> getInstructorByUserId(@PathVariable Long userId) {
        InstructorDto instructorDto = instructorService.getInstructorByUserId(userId);
        if (instructorDto != null) {
            return ResponseEntity.ok(instructorDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/create/{userId}",consumes = ("multipart/form-data")
    )
    public ResponseEntity<String> saveInstructor(
            @PathVariable Long userId,
            @ModelAttribute InstructorDto instructorDto,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        instructorService.saveInstructor(userId, instructorDto, file);
        return ResponseEntity.ok("Instructor saved successfully.");
    }

    @PutMapping("/{instructorId}")
    public ResponseEntity<String> editInstructor(@PathVariable Long instructorId, @RequestBody InstructorDto instructorDto, MultipartFile file) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    instructorService.editInstructor(instructorId, instructorDto, file);
        return ResponseEntity.ok("Instructor edited successfully.");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteInstructorByUserId(@PathVariable Long userId) {
        instructorService.deleteInstructorByUserId(userId);
        return ResponseEntity.ok("Instructor deleted successfully.");
    }
}
