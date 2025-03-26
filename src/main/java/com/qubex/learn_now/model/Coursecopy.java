package com.qubex.learn_now.model;


import com.qubex.learn_now.enums.CourseVisibility;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "courses")
@Data
public class Coursecopy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn()
    private User instructor;


    @Column
    @Enumerated(EnumType.STRING)
    private CourseVisibility visibility = CourseVisibility.PRIVATE;
}
