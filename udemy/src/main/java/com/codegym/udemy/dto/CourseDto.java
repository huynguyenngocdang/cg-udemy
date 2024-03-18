package com.codegym.udemy.dto;

import com.codegym.udemy.entity.Category;
import com.codegym.udemy.entity.Chapter;
import com.codegym.udemy.entity.Instructor;
import com.codegym.udemy.entity.Student;
import com.codegym.udemy.entity.Voucher;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String thumbnailUrl;

    private List<Long> categoriesId = new ArrayList<>();
    private List<Long> chaptersId = new ArrayList<>();
    private List<Long> vouchersId = new ArrayList<>();
    private List<Long> studentsId = new ArrayList<>();
    private List<Long> reviewsId = new ArrayList<>();
    private Long instructorId;
}
