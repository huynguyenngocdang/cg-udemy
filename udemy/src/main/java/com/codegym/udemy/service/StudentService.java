package com.codegym.udemy.service;

import com.codegym.udemy.dto.StudentDto;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
    boolean createStudent(Long userId, StudentDto studentDto, MultipartFile photo);
    StudentDto getStudentByUserId(Long userId);
    boolean editStudent(Long studentId, StudentDto studentDto, MultipartFile photo);
    boolean deleteStudentByUserId(Long userId);
}
