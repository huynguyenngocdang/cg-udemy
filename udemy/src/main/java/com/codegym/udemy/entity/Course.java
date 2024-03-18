package com.codegym.udemy.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String thumbnailUrl;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_student",
            joinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "id")}
    )
    private List<Student> students = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_category",
            joinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")}
    )
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_voucher",
            joinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "voucher_id", referencedColumnName = "id")}
    )
    private List<Voucher> vouchers = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Review> reviews = new ArrayList<>();

}
