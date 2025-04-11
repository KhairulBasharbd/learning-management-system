package com.qubex.learn_now.dto;

import com.qubex.learn_now.enums.CourseLevel;
import com.qubex.learn_now.enums.CourseVisibility;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CourseDTO {
    private UUID id;
    private String title;
    private String description;
    private UUID categoryId;
    private String categoryName;
    private CourseLevel level;
    private BigDecimal price;
    private String thumbnailUrl;
    private String syllabusUrl;
    private CourseVisibility visibility;
    private String instructorName;
    private UUID instructorId;
    private String tags;
    private String duration;
    private int lessonCount;
    private List<SectionDTO> sections;

}