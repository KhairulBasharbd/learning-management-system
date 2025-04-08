package com.qubex.learn_now.repository;

import com.qubex.learn_now.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface LectureRepository extends JpaRepository<Lecture, UUID> {
    List<Lecture> findBySectionIdOrderByOrderAsc(UUID sectionId);
}