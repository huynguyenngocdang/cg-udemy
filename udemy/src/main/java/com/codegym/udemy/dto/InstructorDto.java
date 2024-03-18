package com.codegym.udemy.dto;

import com.codegym.udemy.entity.AppUser;
import com.codegym.udemy.entity.Course;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InstructorDto {
    private Long id;
    private int age;
    private String expertise;
    private String bio;
    private String photoUrl;
    private Long appUserId;
    private List<Long> coursesId = new ArrayList<>();
}
