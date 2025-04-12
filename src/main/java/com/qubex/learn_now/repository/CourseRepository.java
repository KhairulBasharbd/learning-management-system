package com.qubex.learn_now.repository;

import com.qubex.learn_now.enums.CourseLevel;
import com.qubex.learn_now.enums.CourseVisibility;
import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.rmi.server.UID;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {


    List<Course> findByInstructor(Instructor instructor);

    Optional<Course> findById(UUID courseId);


    Page<Course> findByVisibility(CourseVisibility visibility, Pageable pageable);



    @Query("""
        SELECT c FROM Course c
        WHERE c.visibility = :visibility
        AND (:keyword IS NULL OR 
             c.title = :keyword)
        AND (:categoryId IS NULL OR c.categoryId = :categoryId)
        AND (:level IS NULL OR c.level = :level)
        AND (:maxPrice IS NULL OR c.price <= :maxPrice)
        """)


    Page<Course> searchCourses(
            @Param("visibility") CourseVisibility visibility,
            @Param("keyword") String keyword,
            @Param("categoryId") UUID categoryId,
            @Param("level") CourseLevel level,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );


    List<Course> findAllByVisibilityOrderByTitleAsc(CourseVisibility visibility);


}
