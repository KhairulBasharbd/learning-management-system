package com.qubex.learn_now.service;


import com.qubex.learn_now.exception.custom.NotFoundException;
import com.qubex.learn_now.model.LectureCompletion;
import com.qubex.learn_now.repository.CourseRepository;
import com.qubex.learn_now.repository.LectureCompletionRepository;
import com.qubex.learn_now.repository.LectureRepository;
import com.qubex.learn_now.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureCompletionService {

    private final LectureCompletionRepository completionRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;



    public List<LectureCompletion> findByStudentId(UUID studentId) {
        return completionRepository.findByStudentId(studentId);
    }


    @Transactional
    public void markLectureAsComplete(UUID studentId, UUID lectureId) {
        // Check if already marked as complete
        if (completionRepository.existsByStudentIdAndLessonId(studentId, lectureId)) {
            return; // Already completed
        }

        // Create new completion record
        LectureCompletion completion = new LectureCompletion();
        completion.setStudent(studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found")));

        completion.setLesson(lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NotFoundException("Lecture not found")));

        completion.setCompletionDate(LocalDateTime.now());

        completionRepository.save(completion);
    }

    @Transactional
    public void markLectureAsIncomplete(UUID studentId, UUID lectureId) {
        completionRepository.deleteByStudentIdAndLessonId(studentId, lectureId);
    }



}
