package com.qubex.learn_now.repository;

import com.qubex.learn_now.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.server.UID;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {


}
