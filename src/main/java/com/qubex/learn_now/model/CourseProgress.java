package com.qubex.learn_now.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "courseprogresses")
@Entity
public class CourseProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "current_lesson_id")
    private Lecture currentLesson;

    private int completedLessons;

    private int totalLessons;

    private LocalDateTime lastAccessedDate;

    private boolean completed;

    private LocalDateTime enrollmentDate;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public double getProgressPercentage() {
        if (totalLessons == 0) return 0;
        return ((double) completedLessons / totalLessons) * 100;
    }
}

