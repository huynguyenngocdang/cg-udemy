package com.codegym.udemy.service;

import com.codegym.udemy.dto.StudentDto;

public interface StudentService {
    void saveStudent(StudentDto studentDto);
    void editStudent(Long studentId, StudentDto studentDto);
    void deleteStudent(Long studentId);
    StudentDto getStudentById(Long id);
}
