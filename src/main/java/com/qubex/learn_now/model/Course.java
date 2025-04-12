package com.qubex.learn_now.model;

import com.qubex.learn_now.enums.CourseLevel;
import com.qubex.learn_now.enums.CourseVisibility;
import com.qubex.learn_now.model.Instructor;
import com.qubex.learn_now.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Title is required")
    private String title;

    @Column(columnDefinition = "TEXT")
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


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private List<Section> sections = new ArrayList<>();



    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
