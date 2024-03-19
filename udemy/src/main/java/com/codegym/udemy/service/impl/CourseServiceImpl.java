package com.codegym.udemy.service.impl;

import com.codegym.udemy.dto.CourseDto;
import com.codegym.udemy.entity.Category;
import com.codegym.udemy.entity.Chapter;
import com.codegym.udemy.entity.Course;
import com.codegym.udemy.entity.Instructor;
import com.codegym.udemy.entity.Review;
import com.codegym.udemy.entity.Student;
import com.codegym.udemy.entity.Voucher;
import com.codegym.udemy.repository.AppUserRepository;
import com.codegym.udemy.repository.CategoryRepository;
import com.codegym.udemy.repository.ChapterRepository;
import com.codegym.udemy.repository.CourseRepository;
import com.codegym.udemy.repository.InstructorRepository;
import com.codegym.udemy.repository.ReviewRepository;
import com.codegym.udemy.repository.StudentRepository;
import com.codegym.udemy.repository.VoucherRepository;
import com.codegym.udemy.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final ChapterRepository chapterRepository;
    private final VoucherRepository voucherRepository;
    private final ReviewRepository reviewRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, CategoryRepository categoryRepository,
                             ChapterRepository chapterRepository, VoucherRepository voucherRepository,
                             ReviewRepository reviewRepository, InstructorRepository instructorRepository,
                             StudentRepository studentRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.chapterRepository = chapterRepository;
        this.voucherRepository = voucherRepository;
        this.reviewRepository = reviewRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<CourseDto> getCoursesByPage(int pageNumber, int pageSize) {
        Page<Course> courses = courseRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        return courses.map(this::convertToCourseDto);
    }

    private Course convertToCourse(CourseDto courseDto) {
        Course course = modelMapper.map(courseDto, Course.class);

        if(courseDto.getCategoriesId() != null && !courseDto.getCategoriesId().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(courseDto.getCategoriesId());
            course.setCategories(categories);
        }
        if(courseDto.getChaptersId() != null && !courseDto.getChaptersId().isEmpty()) {
            List<Chapter> chapters = chapterRepository.findAllById(courseDto.getChaptersId());
            course.setChapters(chapters);
        }
        if(courseDto.getInstructorId() != null) {
            Optional<Instructor> optionalInstructor = instructorRepository.findById(courseDto.getInstructorId());
            optionalInstructor.ifPresent(course::setInstructor);
        }
        if(courseDto.getVouchersId() != null && !courseDto.getVouchersId().isEmpty()) {
            List<Voucher> vouchers = voucherRepository.findAllById(courseDto.getVouchersId());
            course.setVouchers(vouchers);
        }
        if(courseDto.getStudentsId() != null && !courseDto.getStudentsId().isEmpty()) {
            List<Student> students = studentRepository.findAllById(courseDto.getStudentsId());
            course.setStudents(students);
        }
        if(courseDto.getReviewsId() != null && !courseDto.getReviewsId().isEmpty()) {
            List<Review> reviews = reviewRepository.findAllById(courseDto.getReviewsId());
            course.setReviews(reviews);
        }
        return course;
    }
    private CourseDto convertToCourseDto(Course course){
        CourseDto courseDto = modelMapper.map(course, CourseDto.class);

        if(course.getCategories() != null) {
            List<Long> categoriesId = course.getCategories()
                    .stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            courseDto.setCategoriesId(categoriesId);
        }

        if(course.getChapters() != null) {
            List<Long> chaptersId = course.getChapters()
                    .stream()
                    .map(Chapter::getId)
                    .collect(Collectors.toList());
            courseDto.setChaptersId(chaptersId);
        }

        if(course.getInstructor() != null) {
            courseDto.setInstructorId(course.getInstructor().getId());
        }

        if(course.getVouchers() != null) {
            List<Long> vouchersId = course.getVouchers()
                    .stream()
                    .map(Voucher::getId)
                    .collect(Collectors.toList());
            courseDto.setVouchersId(vouchersId);
        }

        if(course.getStudents() != null) {
            List<Long> studentsId = course.getStudents()
                    .stream()
                    .map(Student::getId)
                    .collect(Collectors.toList());
            courseDto.setStudentsId(studentsId);
        }
        if(course.getReviews() != null) {
            List<Long> reviewsId = course.getReviews()
                    .stream()
                    .map(Review::getId)
                    .collect(Collectors.toList());
            courseDto.setReviewsId(reviewsId);
        }

        return courseDto;
    }
}
