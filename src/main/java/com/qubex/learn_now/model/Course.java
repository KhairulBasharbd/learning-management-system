package com.qubex.learn_now.model;

import com.qubex.learn_now.enums.CourseLevel;
import com.qubex.learn_now.enums.CourseVisibility;
import com.qubex.learn_now.model.Instructor;
import com.qubex.learn_now.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Title is required")
    private String title;

    @Lob
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Category is required")
    private UUID categoryId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Level is required")
    private CourseLevel level;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private BigDecimal price;

    private String thumbnailUrl;
    private String syllabusUrl;

    @NotNull(message = "Visibility is required")
    @Enumerated(EnumType.STRING)
    private CourseVisibility visibility;

    @ManyToOne
    @JoinColumn()
    private Instructor instructor;

    private String tags;
    private String duration;

}
