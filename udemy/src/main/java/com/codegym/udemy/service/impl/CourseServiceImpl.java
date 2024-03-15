package com.codegym.udemy.service.impl;

import com.codegym.udemy.dto.CourseDto;
import com.codegym.udemy.entity.Category;
import com.codegym.udemy.entity.Chapter;
import com.codegym.udemy.entity.Course;
import com.codegym.udemy.entity.Student;
import com.codegym.udemy.entity.Voucher;
import com.codegym.udemy.repository.AppUserRepository;
import com.codegym.udemy.repository.ChapterRepository;
import com.codegym.udemy.repository.CourseRepository;
import com.codegym.udemy.repository.VoucherRepository;
import com.codegym.udemy.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final VoucherRepository voucherRepository;
    private final AppUserRepository appUserRepository;
    private final ModelMapper modelMapper;

    public CourseServiceImpl(CourseRepository courseRepository, ChapterRepository chapterRepository, VoucherRepository voucherRepository, AppUserRepository appUserRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
        this.voucherRepository = voucherRepository;
        this.appUserRepository = appUserRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<CourseDto> getCoursesByPage(int pageNumber, int pageSize) {
        Page<Course> courses = courseRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        return courses.map(this::mapToCourseDto);
    }
    private CourseDto mapToCourseDto(Course course){
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

        if(course.getStudents() != null) {
            List<Long> studentsId = course.getStudents()
                    .stream()
                    .map(Student::getId)
                    .collect(Collectors.toList());
            courseDto.setStudentsId(studentsId);
        }

        if(course.getVouchers() != null) {
            List<Long> vouchersId = course.getVouchers()
                    .stream()
                    .map(Voucher::getId)
                    .collect(Collectors.toList());
            courseDto.setVouchersId(vouchersId);
        }
        return courseDto;
    }
}
