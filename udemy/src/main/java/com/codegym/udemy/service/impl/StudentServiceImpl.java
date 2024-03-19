package com.codegym.udemy.service.impl;

import com.codegym.udemy.dto.StudentDto;
import com.codegym.udemy.entity.AppUser;
import com.codegym.udemy.entity.Course;
import com.codegym.udemy.entity.Review;
import com.codegym.udemy.entity.Student;
import com.codegym.udemy.repository.AppUserRepository;
import com.codegym.udemy.repository.CourseRepository;
import com.codegym.udemy.repository.ReviewRepository;
import com.codegym.udemy.repository.StudentRepository;
import com.codegym.udemy.service.FirebaseStorageService;
import com.codegym.udemy.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ReviewRepository reviewRepository;
    private final AppUserRepository appUserRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final FirebaseStorageService firebaseStorageService;

    public StudentServiceImpl(StudentRepository studentRepository, ReviewRepository reviewRepository,
                              AppUserRepository appUserRepository, CourseRepository courseRepository,
                              ModelMapper modelMapper, FirebaseStorageService firebaseStorageService) {
        this.studentRepository = studentRepository;
        this.reviewRepository = reviewRepository;
        this.appUserRepository = appUserRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.firebaseStorageService = firebaseStorageService;
    }

    private StudentDto convertToStudentDto(Student student) {
        StudentDto studentDto = modelMapper.map(student, StudentDto.class);
        if(student.getCourses() != null && !student.getCourses().isEmpty()) {
            List<Long> coursesId = student.getCourses().stream()
                    .map(Course::getId)
                    .collect(Collectors.toList());
            studentDto.setCoursesId(coursesId);
        }

        if(student.getReviews() != null && !student.getReviews().isEmpty()) {
            List<Long> reviewsId = student.getReviews().stream()
                    .map(Review::getId)
                    .collect(Collectors.toList());
            studentDto.setReviewsId(reviewsId);
        }

        if(student.getAppUser() != null) {
            studentDto.setAppUserId(student.getAppUser().getId());
        }
        return studentDto;
    }

    private Student convertToStudent(StudentDto studentDto) {
        Student student = modelMapper.map(studentDto, Student.class);
        if(studentDto.getCoursesId() != null && !studentDto.getCoursesId().isEmpty()) {
            List<Course> courses = studentDto.getCoursesId().stream()
                    .map(courseId -> courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId)))
                    .collect(Collectors.toList());
            student.setCourses(courses);
        }
        if(studentDto.getReviewsId() != null && !studentDto.getReviewsId().isEmpty()) {
            List<Review> reviews = studentDto.getReviewsId().stream()
                    .map(reviewId -> reviewRepository.findById(reviewId).orElseThrow(()-> new IllegalArgumentException("Review not found with id:" + reviewId)))
                    .collect(Collectors.toList());
            student.setReviews(reviews);
        }
        if(studentDto.getAppUserId() != null) {
            Optional<AppUser> optionalAppUser = appUserRepository.findById(studentDto.getAppUserId());
            optionalAppUser.ifPresent(student::setAppUser);
        }
        return student;
    }

    @Override
    public boolean createStudent(Long userId, StudentDto studentDto, MultipartFile photo) {
        Optional<Student> existingStudent = studentRepository.getStudentsByAppUser_Id(userId);

        if(existingStudent.isPresent()) {
            throw new IllegalArgumentException("A student is already exist with provided app user Id");
        } else {

            try {
                String photoUrl = firebaseStorageService.uploadFile(photo);
                studentDto.setPhotoUrl(photoUrl);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
            studentDto.setAppUserId(userId);
            Student student = convertToStudent(studentDto);
            studentRepository.save(student);
            return student.getId() != null;
        }
    }

    @Override
    public StudentDto getStudentByUserId(Long userId) {
        Optional<Student> optionalStudent = studentRepository.getStudentsByAppUser_Id(userId);
        if(optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            return convertToStudentDto(student);
        } else {
            return null;
        }
    }

    @Override
    public boolean editStudent(Long studentId, StudentDto studentDto, MultipartFile photo) {
        Student existingStudent = studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found with Id:" + studentId));
        updateStudentField(existingStudent, studentDto);

        // Upload new profile picture if file is provided
        if (photo != null && !photo.isEmpty()) {
            try {
                String imageUrl = firebaseStorageService.uploadFile(photo);
                existingStudent.setPhotoUrl(imageUrl);
            } catch (IOException e) {
                throw new IllegalStateException("Can't upload profile picture");
            }
        }
        studentRepository.save(existingStudent);
        return true;
    }

    private void updateStudentField(Student existingStudent, StudentDto studentDto) {
        modelMapper.map(studentDto, existingStudent);
        if(studentDto.getAppUserId() != null) {
            AppUser appUser = appUserRepository.findById(studentDto.getAppUserId()).orElseThrow(() -> new IllegalArgumentException("Student not found with Id:" + studentDto.getAppUserId()));
            existingStudent.setAppUser(appUser);
        }

        if(studentDto.getCoursesId() != null && !studentDto.getCoursesId().isEmpty()) {
            List<Course> courses = studentDto.getCoursesId().stream()
                    .map(courseId -> courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("No course found with id:" + courseId)))
                    .collect(Collectors.toList());
            existingStudent.setCourses(courses);
        }
        if(studentDto.getReviewsId() != null && !studentDto.getReviewsId().isEmpty()) {
            List<Review> reviews = studentDto.getReviewsId().stream()
                    .map(reviewId -> reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("No review found with id:" + reviewId)))
                    .collect(Collectors.toList());
            existingStudent.setReviews(reviews);
        }
    }
    @Override
    public boolean deleteStudentByUserId(Long userId) {
        Optional<Student> optionalStudent = studentRepository.getStudentsByAppUser_Id(userId);
        if(optionalStudent.isPresent()){
            studentRepository.delete(optionalStudent.get());
            return true;
        } else {
            throw new IllegalArgumentException("Student not found with userId" + userId);
        }
    }
}
