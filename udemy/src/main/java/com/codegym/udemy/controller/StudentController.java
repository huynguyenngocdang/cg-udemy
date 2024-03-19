package com.codegym.udemy.controller;

import com.codegym.udemy.dto.StudentDto;
import com.codegym.udemy.service.JwtService;
import com.codegym.udemy.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.codegym.udemy.constant.VarConstant.AUTHORIZATION;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;
    private final JwtService jwtService;

    public StudentController(StudentService studentService, JwtService jwtService) {
        this.studentService = studentService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<String> createStudent(@RequestHeader(AUTHORIZATION) String token,
                                           @ModelAttribute StudentDto studentDto,
                                           @RequestParam("file") MultipartFile file) {
        Long userId = jwtService.extractUserId(token);
        boolean created = studentService.createStudent(userId, studentDto, file);
        return ResponseEntity.status(created ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/get")
    public ResponseEntity<StudentDto> getStudentByUserId(@RequestHeader(AUTHORIZATION) String token) {
        Long userId = jwtService.extractUserId(token);
        StudentDto studentDto = studentService.getStudentByUserId(userId);
        if (studentDto != null) {
            return ResponseEntity.ok(studentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editStudent(@RequestHeader(AUTHORIZATION) String token,
                                         @RequestBody StudentDto studentDto,
                                         @RequestParam(value = "file", required = false) MultipartFile file) {
        Long userId = jwtService.extractUserId(token);
        StudentDto existingStudentDto = studentService.getStudentByUserId(userId);
        boolean edited = studentService.editStudent(existingStudentDto.getId(), studentDto, file);
        return ResponseEntity.status(edited ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStudentByUserId(@RequestHeader(AUTHORIZATION) String token) {
        Long userId = jwtService.extractUserId(token);
        boolean deleted = studentService.deleteStudentByUserId(userId);
        return ResponseEntity.status(deleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).build();
    }
}
