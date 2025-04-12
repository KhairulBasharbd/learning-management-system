package com.qubex.learn_now.repository;

import com.qubex.learn_now.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, UUID> {
    List<Lecture> findBySectionIdOrderByOrderAsc(UUID sectionId);




//    @Query("SELECT l FROM Lecture l WHERE l.section.course.id = :courseId ORDER BY l.section.orderIndex, l.orderIndex ASC")
//    List<Lecture> findAllByCourseIdOrderByOrder(@Param("courseId") UUID courseId);
//
//    @Query("SELECT l FROM Lecture l WHERE l.section.course.id = :courseId AND l.section.orderIndex = 0 AND l.orderIndex = 0")
//    Optional<Lecture> findFirstLessonInCourse(@Param("courseId") UUID courseId);

    @Query("SELECT COUNT(l) FROM Lecture l WHERE l.section.course.id = :courseId")
    int countLessonsByCourseId(@Param("courseId") UUID courseId);

}