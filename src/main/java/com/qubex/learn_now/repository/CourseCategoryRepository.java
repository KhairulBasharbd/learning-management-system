package com.qubex.learn_now.repository;

import com.qubex.learn_now.model.CourseCategory;
import com.qubex.learn_now.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, UUID> {



}
