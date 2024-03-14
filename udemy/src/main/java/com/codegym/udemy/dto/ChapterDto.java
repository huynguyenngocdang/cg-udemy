package com.codegym.udemy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChapterDto {
    private Long id;
    private String name;
    private Long courseId;
    private List<Long> lessonsId = new ArrayList<>();
}
