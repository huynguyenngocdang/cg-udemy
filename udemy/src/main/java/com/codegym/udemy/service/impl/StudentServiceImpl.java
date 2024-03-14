package com.codegym.udemy.service.impl;

import com.codegym.udemy.dto.StudentDto;
import com.codegym.udemy.entity.Student;
import com.codegym.udemy.repository.StudentRepository;
import com.codegym.udemy.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    private Student convertToStudent(StudentDto studentDto) {
        Student student = modelMapper.map(studentDto, Student.class);
        if (studentDto.getId() != null) {
            Optional<Student> optionalStudent = studentRepository.findById(studentDto.getId());
            if (optionalStudent.isPresent()) {
                student = optionalStudent.get();
            }
        }
        return student;
    }

    private StudentDto convertToStudentDto(Student student) {
        StudentDto studentDto = modelMapper.map(student, StudentDto.class);
        if (student.getId() != null) {
            studentDto.setId(student.getId());
        }
        return studentDto;
    }

    @Override
    public void saveStudent(StudentDto studentDto) {
        Student student = convertToStudent(studentDto);
        studentRepository.save(student);
    }

    @Override
    public StudentDto getStudentById(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if ((optionalStudent.isPresent())) {
            Student student = optionalStudent.get();
            return convertToStudentDto(student);
        } else {
            return null;
        }
    }

    @Override
    public void editStudent(Long studentId, StudentDto studentDto) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with studentId: " + studentId));

        Student updatedStudent = convertToStudent(studentDto);
        updatedStudent.setId(existingStudent.getId());
        studentRepository.save(updatedStudent);
    }

    @Override
    public void deleteStudent(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            studentRepository.deleteById(studentId);
        } else {
            throw new IllegalArgumentException("Student not found with studentId: " + studentId);
        }
    }
}

