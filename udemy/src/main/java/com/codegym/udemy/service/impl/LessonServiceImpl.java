package com.codegym.udemy.service.impl;

import com.codegym.udemy.dto.LessonDto;
import com.codegym.udemy.entity.Chapter;
import com.codegym.udemy.entity.Lesson;
import com.codegym.udemy.repository.ChapterRepository;
import com.codegym.udemy.repository.LessonRepository;
import com.codegym.udemy.service.FirebaseStorageService;
import com.codegym.udemy.service.LessonService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final ModelMapper modelMapper;
    private final FirebaseStorageService firebaseStorageService;

    public LessonServiceImpl(LessonRepository lessonRepository, ChapterRepository chapterRepository,
                             ModelMapper modelMapper, FirebaseStorageService firebaseStorageService) {
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.modelMapper = modelMapper;
        this.firebaseStorageService = firebaseStorageService;
    }

    private LessonDto convertToLessonDto(Lesson lesson) {
        LessonDto lessonDto = modelMapper.map(lesson, LessonDto.class);
        if(lesson.getChapter() != null) {
            lessonDto.setChapterId(lesson.getChapter().getId());
        }
        return lessonDto;
    }

    private Lesson convertToLesson(LessonDto lessonDto) {
        Lesson lesson = modelMapper.map(lessonDto, Lesson.class);
        if(lessonDto.getChapterId() != null) {
            Optional<Chapter> optionalChapter = chapterRepository.findById(lessonDto.getChapterId());
            optionalChapter.ifPresent(lesson::setChapter);
        }
        return lesson;
    }
    @Override
    public LessonDto getLesson(Long lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if(optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();
            return convertToLessonDto(lesson);
        } else {
            throw new IllegalArgumentException("No lesson with lessonId: " + lessonId);
        }
    }

    @Override
    public List<LessonDto> getLessonByChapter_Id(Long chapterId) {
        List<Lesson> lessons = lessonRepository.findAllByChapter_Id(chapterId);
        List<LessonDto> lessonsDto = new ArrayList<>();
        for (Lesson lesson: lessons
             ) {
            lessonsDto.add(convertToLessonDto(lesson));
        }
        return lessonsDto;
    }

    @Override
    public boolean createLesson(LessonDto lessonDto, MultipartFile video) {
        try {
            String videoUrl = firebaseStorageService.uploadFile(video);
            lessonDto.setVideoUrl(videoUrl);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Lesson lesson = convertToLesson(lessonDto);
        lessonRepository.save(lesson);
        return lesson.getId() != null;
    }

    @Override
    public boolean editLesson(Long lessonId, LessonDto lessonDto, MultipartFile video) {
        // Fetch the existing lesson by ID
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Can't find lesson with lessonId: " + lessonId));

        // Update existing lesson with data from the provided LessonDto
        updateExistingLesson(existingLesson, lessonDto);

        // Upload new video if provided
        if (video != null && !video.isEmpty()) {
            try {
                String videoUrl = firebaseStorageService.uploadFile(video);
                existingLesson.setVideoUrl(videoUrl);
            } catch (IOException e) {
                throw new IllegalStateException("Can't upload lesson video");
            }
        }

        // Save the updated lesson
        lessonRepository.save(existingLesson);

        // Return true indicating successful editing
        return true;
    }

    private void updateExistingLesson(Lesson existingLesson, LessonDto lessonDto) {
        // Map data from LessonDto to existingLesson
        modelMapper.map(lessonDto, existingLesson);

        // If chapter ID is provided in LessonDto, update the chapter of the existing lesson
        if (lessonDto.getChapterId() != null) {
            Chapter chapter = chapterRepository.findById(lessonDto.getChapterId())
                    .orElseThrow(() -> new IllegalArgumentException("Can't find chapter with chapter id: " + lessonDto.getChapterId()));
            existingLesson.setChapter(chapter);
        }
    }

    @Override
    public boolean deleteLesson(Long lessonId) {
        // Fetch the lesson by its ID
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);

        // Check if the lesson exists
        if (optionalLesson.isPresent()) {
            // If the lesson exists, delete it
            lessonRepository.deleteById(lessonId);
            return true; // Return true to indicate successful deletion
        } else {
            // If the lesson does not exist, throw an exception or handle it accordingly
            throw new IllegalArgumentException("No lesson with lessonId: " + lessonId);
        }
    }
}
