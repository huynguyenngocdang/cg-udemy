package com.codegym.udemy.service.impl;

import com.codegym.udemy.dto.VoucherDto;
import com.codegym.udemy.entity.Course;
import com.codegym.udemy.entity.Voucher;
import com.codegym.udemy.repository.CourseRepository;
import com.codegym.udemy.repository.VoucherRepository;
import com.codegym.udemy.service.VoucherService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final ModelMapper modelMapper;
    private final VoucherRepository voucherRepository;
    private final CourseRepository courseRepository;

    public VoucherServiceImpl(ModelMapper modelMapper, VoucherRepository voucherRepository, CourseRepository courseRepository) {
        this.modelMapper = modelMapper;
        this.voucherRepository = voucherRepository;
        this.courseRepository = courseRepository;
    }
    private VoucherDto convertToVoucherDto(Voucher voucher) {
        VoucherDto voucherDto = modelMapper.map(voucher, VoucherDto.class);
        if(voucher.getCourses() != null && !voucher.getCourses().isEmpty()) {
            List<Long> coursesId = voucher.getCourses().stream()
                    .map(Course::getId).collect(Collectors.toList());
            voucherDto.setCoursesId(coursesId);
        }
        return voucherDto;
    }

    private Voucher convertToVoucher(VoucherDto voucherDto) {
        Voucher voucher = modelMapper.map(voucherDto, Voucher.class);
        if (voucherDto.getCoursesId() != null && !voucherDto.getCoursesId().isEmpty()) {
            List<Course> courses = voucherDto.getCoursesId().stream()
                    .map(courseId -> courseRepository.findById(courseId)
                            .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId)))
                    .collect(Collectors.toList());
            voucher.setCourses(courses);
        }
        return voucher;
    }
    @Override
    public void saveVoucher(Long userId, VoucherDto voucherDto) {
        Voucher voucher = convertToVoucher(voucherDto);
        voucherRepository.save(voucher);
    }

    @Override
    public VoucherDto getVoucherById(Long voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found with id: " + voucherId));
        return convertToVoucherDto(voucher);
    }

    @Override
    public void editVoucher(Long voucherId, VoucherDto voucherDto) {
        Voucher existingVoucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found with id: " + voucherId));
        VoucherDto existingVoucherDto = convertToVoucherDto(existingVoucher);
        modelMapper.map(voucherDto, existingVoucherDto);
        Voucher updatedVoucher = convertToVoucher(existingVoucherDto);
        voucherRepository.save(updatedVoucher);
    }
    @Override
    public boolean deleteVoucher(Long voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        if(voucher == null) {
            return false;
        } else {
            voucherRepository.deleteById(voucherId);
            return true;
        }
    }
}
