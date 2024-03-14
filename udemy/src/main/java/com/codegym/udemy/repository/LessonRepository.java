package com.codegym.udemy.repository;

import com.codegym.udemy.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByChapter_Id(Long chapterId);
}