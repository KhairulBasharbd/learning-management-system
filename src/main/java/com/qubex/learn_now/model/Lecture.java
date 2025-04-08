package com.qubex.learn_now.model;


import com.qubex.learn_now.enums.LectureType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Lecture title is required")
    private String title;

    private String description;

    @Column(name = "lecture_order")
    private int order;

    @Enumerated(EnumType.STRING)
    private LectureType type;

    @Lob
    private String content; // URL or text content

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
