package com.codegym.udemy.repository;

import com.codegym.udemy.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> getStudentsByAppUser_Id(Long appUserId);
}
