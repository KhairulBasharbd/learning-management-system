package com.qubex.learn_now.service;


import com.qubex.learn_now.exception.custom.AlreadyExistsException;
import com.qubex.learn_now.exception.custom.NotFoundException;
import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.CourseProgress;
import com.qubex.learn_now.model.Student;
import com.qubex.learn_now.repository.CourseProgressRepository;
import com.qubex.learn_now.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseProgressService {

    private final CourseProgressRepository courseProgressRepository;
    private final StudentRepository studentRepository;
    private final CourseService courseService;

    public boolean hasCourseProgress(String studentEmail, UUID courseId) {
        return courseProgressRepository.existsByStudentEmailAndCourseId(studentEmail, courseId);
    }

    @Transactional
    public List<Course> getEnrolledCourseByStudent(String email){

        List<CourseProgress> CourseProgresses =  courseProgressRepository.findByStudentEmailOrderByLastAccessedDateDesc(email);

        return CourseProgresses.stream()
                .map(CourseProgress::getCourse)
                .collect(Collectors.toList());

    }

    @Transactional
    public CourseProgress createCourseProgress(String studentEmail, UUID courseId) {
        if (hasCourseProgress(studentEmail, courseId)) {
            throw new AlreadyExistsException("Student is already enrolled in this course");
        }

        Student student = studentRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        Course course = courseService.getCourseById(courseId);

        // Calculate total lessons in the course
        int totalLessons = course.getSections().stream()
                .mapToInt(section -> section.getLectures().size())
                .sum();

        CourseProgress progress = new CourseProgress();
        progress.setStudent(student);
        progress.setCourse(course);
        progress.setTotalLessons(totalLessons);
        progress.setCompletedLessons(0);
        progress.setCompleted(false);
        progress.setEnrollmentDate(LocalDateTime.now());
        progress.setLastAccessedDate(LocalDateTime.now());

        return courseProgressRepository.save(progress);
    }

    public CourseProgress getCourseProgress(String studentEmail, UUID courseId) {
        return courseProgressRepository.findByStudentEmailAndCourseId(studentEmail, courseId)
                .orElseThrow(() -> new NotFoundException("Course progress not found"));
    }

    public double getCourseProgressPercentage(String studentEmail, UUID courseId) {
        CourseProgress progress = getCourseProgress(studentEmail, courseId);
        return progress.getProgressPercentage();
    }
}
