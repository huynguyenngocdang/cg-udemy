package com.codegym.udemy.service;

import com.codegym.udemy.dto.InstructorDto;
import org.springframework.web.multipart.MultipartFile;

public interface InstructorService {
    void saveInstructor(Long userId, InstructorDto instructorDto, MultipartFile file);
    InstructorDto getInstructorByUserId(Long userId);
    void editInstructor(Long instructorId, InstructorDto instructorDto, MultipartFile file);
    void deleteInstructorByUserId(Long userId);

}
