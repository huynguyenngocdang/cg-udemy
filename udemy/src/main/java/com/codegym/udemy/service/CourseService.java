package com.codegym.udemy.service;

import com.codegym.udemy.dto.CourseDto;
import org.springframework.data.domain.Page;

public interface CourseService {
    Page<CourseDto> getCoursesByPage(int pageNumber, int pageSize);
    boolean createCourse(Long userId, CourseDto courseDto);
    CourseDto getCourseById(Long courseId);
    boolean editCourse(Long userId, Long courseId, CourseDto courseDto);
    boolean deleteCourse(Long userId, Long courseId);
}
