package com.qubex.learn_now.service;


import com.qubex.learn_now.exception.custom.AlreadyExistsException;
import com.qubex.learn_now.exception.custom.NotFoundException;
import com.qubex.learn_now.model.*;
import com.qubex.learn_now.repository.CourseProgressRepository;
import com.qubex.learn_now.repository.CourseRepository;
import com.qubex.learn_now.repository.LectureCompletionRepository;
import com.qubex.learn_now.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseProgressService {

    private final CourseProgressRepository courseProgressRepository;
    private final StudentRepository studentRepository;
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final LectureCompletionRepository completionRepository;

    public boolean hasCourseProgress(String studentEmail, UUID courseId) {
        return courseProgressRepository.existsByStudentEmailAndCourseId(studentEmail, courseId);
    }

    public boolean isStudentEnrolled(UUID studentId, UUID courseId) {

        return courseProgressRepository.existsByStudentIdAndCourseId(studentId, courseId);
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


//    ------------------------------------------------------------------------------

    public CourseProgress save(CourseProgress progress) {
        return courseProgressRepository.save(progress);
    }


    @Transactional
    public CourseProgress getOrCreateProgress(UUID studentId, UUID courseId) {
        Optional<CourseProgress> existingProgress = courseProgressRepository.findByStudentIdAndCourseId(studentId, courseId);

        if (existingProgress.isPresent()) {
            return existingProgress.get();
        } else {
            // Create new progress record
            CourseProgress newProgress = new CourseProgress();
            newProgress.setStudent(studentRepository.findById(studentId)
                    .orElseThrow(() -> new NotFoundException("Student not found")));
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NotFoundException("Course not found"));
            newProgress.setCourse(course);

            // Calculate total lessons
            int totalLessons = course.getSections().stream()
                    .mapToInt(section -> section.getLectures().size())
                    .sum();

            newProgress.setTotalLessons(totalLessons);
            newProgress.setCompletedLessons(0);
            newProgress.setEnrollmentDate(LocalDateTime.now());
            newProgress.setLastAccessedDate(LocalDateTime.now());
            newProgress.setCompleted(false);

            return courseProgressRepository.save(newProgress);
        }
    }


    @Transactional
    public void updateProgress(UUID studentId, UUID courseId) {
        CourseProgress progress = getOrCreateProgress(studentId, courseId);
        Course course = progress.getCourse();

        // Get all lectures for this course
        Set<UUID> allLectureIds = course.getSections().stream()
                .flatMap(section -> section.getLectures().stream())
                .map(Lecture::getId)
                .collect(Collectors.toSet());

        // Get completed lectures for this student and course
        List<LectureCompletion> completions = completionRepository.findByStudentIdAndLessonIn(
                studentId,
                course.getSections().stream()
                        .flatMap(section -> section.getLectures().stream())
                        .collect(Collectors.toList())
        );

        // Update completed count
        progress.setCompletedLessons(completions.size());

        // Check if course is completed
        if (progress.getCompletedLessons() == progress.getTotalLessons()) {
            progress.setCompleted(true);
        } else {
            progress.setCompleted(false);
        }

        courseProgressRepository.save(progress);
    }

}
