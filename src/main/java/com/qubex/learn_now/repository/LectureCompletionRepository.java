package com.qubex.learn_now.repository;

import com.qubex.learn_now.model.Lecture;
import com.qubex.learn_now.model.LectureCompletion;
import com.qubex.learn_now.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LectureCompletionRepository extends JpaRepository<LectureCompletion, UUID> {

    List<LectureCompletion> findByStudent(Student student);

    Optional<LectureCompletion> findByStudentAndLesson(Student student, Lecture lesson);

    List<LectureCompletion> findByStudentAndLessonSectionCourseId(Student student, UUID courseId);
}
