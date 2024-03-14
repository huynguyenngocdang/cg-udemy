package com.codegym.udemy.service;

import com.codegym.udemy.dto.ChapterDto;

import java.util.List;

public interface ChapterService {
    List<ChapterDto> getChaptersByCourseId(Long courseId);
    void createChaptersByCourseId(ChapterDto chapterDto);
    void updateChapter(Long chapterId, ChapterDto chapterDto);
    boolean deleteChapter(Long chapterId);
}
