package com.codegym.udemy.service;

import com.codegym.udemy.dto.LessonDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LessonService {
    LessonDto getLesson(Long lessonId);
    List<LessonDto> getLessonByChapter_Id(Long chapterId);
    boolean createLesson(LessonDto lessonDto, MultipartFile video);
    boolean editLesson(Long lessonId, LessonDto lessonDto, MultipartFile video);
    boolean deleteLesson(Long lessonId);
}
