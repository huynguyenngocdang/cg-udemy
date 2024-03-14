package com.codegym.udemy.repository;

import com.codegym.udemy.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findAllByCourse_Id(Long courseId);
}
