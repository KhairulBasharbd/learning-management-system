package com.qubex.learn_now.dto.request;

import com.qubex.learn_now.enums.CourseLevel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CourseSearchDTO {
    private String keyword;
    private UUID categoryId;
    private CourseLevel level;
    private BigDecimal maxPrice;
    private String sortBy = "createdAt"; // Default sort
    private String sortDirection = "DESC"; // Default direction
}