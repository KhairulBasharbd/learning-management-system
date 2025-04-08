package com.qubex.learn_now.repository;

import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.server.UID;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {


    List<Course> findByInstructor(Instructor instructor);

    Optional<Course> findById(UUID courseId);


}
