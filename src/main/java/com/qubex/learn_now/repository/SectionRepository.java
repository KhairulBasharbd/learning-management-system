package com.qubex.learn_now.repository;


import com.qubex.learn_now.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {
    List<Section> findByCourseIdOrderByOrderAsc(UUID courseId);


}