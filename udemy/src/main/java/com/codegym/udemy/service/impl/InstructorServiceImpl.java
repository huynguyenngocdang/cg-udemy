package com.codegym.udemy.service.impl;

import com.codegym.udemy.dto.InstructorDto;
import com.codegym.udemy.entity.AppUser;
import com.codegym.udemy.entity.Course;
import com.codegym.udemy.entity.Instructor;
import com.codegym.udemy.repository.AppUserRepository;
import com.codegym.udemy.repository.CourseRepository;
import com.codegym.udemy.repository.InstructorRepository;
import com.codegym.udemy.service.FirebaseStorageService;
import com.codegym.udemy.service.InstructorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final AppUserRepository appUserRepository;
    private final ModelMapper modelMapper;
    private final FirebaseStorageService firebaseStorageService;

    public InstructorServiceImpl(InstructorRepository instructorRepository, CourseRepository courseRepository,
                                 AppUserRepository appUserRepository, ModelMapper modelMapper,
                                 FirebaseStorageService firebaseStorageService) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.appUserRepository = appUserRepository;
        this.modelMapper = modelMapper;
        this.firebaseStorageService = firebaseStorageService;
    }

    private Instructor convertToInstructor(InstructorDto instructorDto) {
        Instructor instructor = modelMapper.map(instructorDto, Instructor.class);
        if (instructorDto.getAppUserId() != null) {
            Optional<AppUser> optionalAppUser = appUserRepository.findById(instructorDto.getAppUserId());
            optionalAppUser.ifPresent(instructor::setAppUser);
        }

        if (instructorDto.getCoursesId() != null && !instructorDto.getCoursesId().isEmpty()) {
            List<Course> courses = courseRepository.findAllById(instructorDto.getCoursesId());
            instructor.setCourses(courses);
        }
        return instructor;
    }

    private InstructorDto convertToInstructorDto(Instructor instructor) {
        InstructorDto instructorDto = modelMapper.map(instructor, InstructorDto.class);
        if(instructor.getAppUser() != null) {
            instructorDto.setAppUserId(instructor.getAppUser().getId());
        }

        if(instructor.getCourses() != null && !instructor.getCourses().isEmpty()) {
            List<Long> coursesId = instructor.getCourses().stream()
                    .map(Course::getId)
                    .collect(Collectors.toList());

            instructorDto.setCoursesId(coursesId);
        }
        return instructorDto;
    }

    @Override
    public void saveInstructor(Long appUserId, InstructorDto instructorDto, MultipartFile file) {
        // Check if there is already an Instructor associated with the provided AppUser
        Optional<Instructor> existingInstructor = instructorRepository.getInstructorByAppUser_Id(appUserId);

        if (existingInstructor.isPresent()) {
            throw new IllegalStateException("An Instructor is already associated with the provided AppUser");
        } else {
            // If no existing Instructor is found, proceed to save the new Instructor
            try {
                String imageUrl = firebaseStorageService.uploadFile(file);
                instructorDto.setAppUserId(appUserId);
                instructorDto.setPhotoUrl(imageUrl);
            } catch (IOException e) {
                throw new IllegalStateException("Can't upload profile picture");
            }
            Instructor instructor = convertToInstructor(instructorDto);
            instructorRepository.save(instructor);
        }
    }

    @Override
    public void editInstructor(Long instructorId, InstructorDto instructorDto, MultipartFile file) {
        // Fetch the existing Instructor from the database
        Instructor existingInstructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found with ID: " + instructorId));

        // Update fields of the existing Instructor with data from InstructorDto
       Instructor updateInstructor = updateInstructorFields(instructorId, instructorDto);

        // Upload new profile picture if file is provided
        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = firebaseStorageService.uploadFile(file);
                updateInstructor.setPhotoUrl(imageUrl);
            } catch (IOException e) {
                throw new IllegalStateException("Can't upload profile picture");
            }
        }

        // Save the updated Instructor back to the database
        instructorRepository.save(updateInstructor);
    }

    private Instructor updateInstructorFields(Long instructorId, InstructorDto instructorDto) {
        // Update common fields using ModelMapper
       Instructor instructor = modelMapper.map(instructorDto, Instructor.class);
       instructor.setId(instructorId);

        // Update AppUser if AppUserId is provided
        if (instructorDto.getAppUserId() != null) {
            AppUser appUser = appUserRepository.findById(instructorDto.getAppUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid AppUserId: " + instructorDto.getAppUserId()));
            instructor.setAppUser(appUser);
        }

        // Update Courses if CoursesId is provided
        if (instructorDto.getCoursesId() != null && !instructorDto.getCoursesId().isEmpty()) {
            List<Course> courses = courseRepository.findAllById(instructorDto.getCoursesId());
            instructor.setCourses(courses);
        }

        return instructor;
    }

    @Override
    public InstructorDto getInstructorByUserId(Long userId) {
        Optional<Instructor> optionalInstructor = instructorRepository.getInstructorByAppUser_Id(userId);
       if(optionalInstructor.isPresent()) {
           Instructor instructor = optionalInstructor.get();
           return convertToInstructorDto(instructor);
       } else {
           return null;
       }
    }

    @Override
    public void deleteInstructorByUserId(Long userId) {
        // Fetch the Instructor based on the userId
        Optional<Instructor> optionalInstructor = instructorRepository.getInstructorByAppUser_Id(userId);

        // Check if the Instructor exists
        if (optionalInstructor.isPresent()) {
            // Delete the Instructor
            instructorRepository.delete(optionalInstructor.get());
        } else {
            // Handle the case where the Instructor does not exist
            throw new IllegalArgumentException("Instructor not found with userId: " + userId);
        }
    }
}
