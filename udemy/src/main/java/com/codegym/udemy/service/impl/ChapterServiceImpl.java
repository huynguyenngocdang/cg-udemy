package com.codegym.udemy.service.impl;

import com.codegym.udemy.dto.ChapterDto;
import com.codegym.udemy.dto.CourseDto;
import com.codegym.udemy.entity.Chapter;
import com.codegym.udemy.entity.Course;
import com.codegym.udemy.entity.Lesson;
import com.codegym.udemy.repository.ChapterRepository;
import com.codegym.udemy.repository.CourseRepository;
import com.codegym.udemy.repository.LessonRepository;
import com.codegym.udemy.service.ChapterService;
import com.codegym.udemy.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChapterServiceImpl implements ChapterService {
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ChapterServiceImpl(CourseRepository courseRepository, ChapterRepository chapterRepository, LessonRepository lessonRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
        this.lessonRepository = lessonRepository;
        this.modelMapper = modelMapper;
    }
    private ChapterDto convertToChapterDto(Chapter chapter) {
        ChapterDto chapterDto = modelMapper.map(chapter, ChapterDto.class);
        if(chapter.getCourse() != null) {
            chapterDto.setCourseId(chapter.getCourse().getId());
        }

        List<Long> lessonsId = chapter.getLessons().stream().map(Lesson::getId).collect(Collectors.toList());
        chapterDto.setLessonsId(lessonsId);
        return chapterDto;
    }

    private Chapter convertToChapter(ChapterDto chapterDto) {
        Chapter chapter = modelMapper.map(chapterDto, Chapter.class);
        if(chapterDto.getCourseId() != null) {
            Optional<Course> optionalCourse = courseRepository.findById(chapterDto.getCourseId());
            optionalCourse.ifPresent(chapter::setCourse);
        }

        if(chapterDto.getLessonsId() != null && !chapterDto.getLessonsId().isEmpty()) {
            List<Lesson> lessons = lessonRepository.findAllById(chapterDto.getLessonsId());
            chapter.setLessons(lessons);
        }
        return chapter;
    }

    @Override
    public List<ChapterDto> getChaptersByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        List<Chapter> chapters = chapterRepository.findAllByCourse_Id(course.getId());
        return chapters.stream()
                .map(this::convertToChapterDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createChaptersByCourseId(ChapterDto chapterDto) {
        Chapter chapter = convertToChapter(chapterDto);
        chapterRepository.save(chapter);
    }

    @Override
    public void updateChapter(Long chapterId, ChapterDto chapterDto) {
        Chapter existingChapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with ID: " + chapterId));

        // Update the existing chapter entity with data from the provided ChapterDto
        existingChapter.setName(chapterDto.getName());
        // Update other properties as needed

        // Update the lessons associated with the chapter if necessary
        if (chapterDto.getLessonsId() != null && !chapterDto.getLessonsId().isEmpty()) {
            List<Lesson> lessons = lessonRepository.findAllById(chapterDto.getLessonsId());
            existingChapter.setLessons(lessons);
        }

        // Save the updated chapter entity back to the database
        chapterRepository.save(existingChapter);
    }
    @Override
    public boolean deleteChapter(Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElse(null); // If chapter is not found, chapter will be null

        if (chapter == null) {
            return false; // Return false indicating that deletion failed
        }

        // Delete the associated lessons (optional, depending on your requirements)
        chapter.getLessons().forEach(lessonRepository::delete);

        // Delete the chapter itself
        chapterRepository.delete(chapter);

        return true; // Return true indicating that deletion was successful
    }
}
