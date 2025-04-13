package com.qubex.learn_now.repository;

import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.CourseProgress;
import com.qubex.learn_now.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, UUID> {

    List<CourseProgress> findByStudentOrderByLastAccessedDateDesc(Student student);

    List<CourseProgress> findByStudentEmailOrderByLastAccessedDateDesc(String studentEmail);

    Optional<CourseProgress> findByStudentIdAndCourseId(UUID studentId, UUID courseId);

    Optional<CourseProgress> findByStudentEmailAndCourseId(String studentEmail, UUID courseId);

    boolean existsByStudentEmailAndCourseId(String email, UUID courseId);

    boolean existsByStudentIdAndCourseId(UUID studentId, UUID courseId);

}
