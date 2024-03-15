package com.codegym.udemy.service;

import com.codegym.udemy.dto.StudentDto;

public interface StudentService {
    void saveStudent(Long userId, StudentDto studentDto);
    StudentDto getStudentByUserId(Long userId);
    void editStudent(Long studentId, StudentDto studentDto);
    void deleteStudentByUserId(Long userId);
}
