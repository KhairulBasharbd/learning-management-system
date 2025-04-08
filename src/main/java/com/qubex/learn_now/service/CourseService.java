package com.qubex.learn_now.service;

import com.qubex.learn_now.exception.custom.NotFoundException;
import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.Instructor;
import com.qubex.learn_now.repository.CourseRepository;
import com.qubex.learn_now.repository.InstructorRepository;
import com.qubex.learn_now.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;


    @Transactional
    public List<Course> getCoursesByInstructor() {

        String authenticatedEmail = AuthUtil.getAuthenticatedUsername();

        Instructor instructor = instructorRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Instructor"));


        return courseRepository.findByInstructor(instructor);
    }

    @Transactional
    public Course getCourseById(UUID courseId) {

        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

}
