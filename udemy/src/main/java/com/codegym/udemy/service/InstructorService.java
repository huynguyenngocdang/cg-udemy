package com.codegym.udemy.service;

import com.codegym.udemy.dto.InstructorDto;
import org.springframework.web.multipart.MultipartFile;

public interface InstructorService {
    boolean createInstructor(Long userId, InstructorDto instructorDto, MultipartFile file);
    InstructorDto getInstructorByUserId(Long userId);
    boolean editInstructor(Long instructorId, InstructorDto instructorDto, MultipartFile file);
    boolean deleteInstructorByUserId(Long userId);

}
