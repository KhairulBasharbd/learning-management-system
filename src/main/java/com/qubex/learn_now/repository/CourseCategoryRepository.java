package com.qubex.learn_now.repository;

import com.qubex.learn_now.model.CourseCategory;
import com.qubex.learn_now.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, UUID> {



}
