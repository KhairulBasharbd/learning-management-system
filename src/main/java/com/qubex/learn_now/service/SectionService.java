package com.qubex.learn_now.service;


import com.qubex.learn_now.dto.request.SectionCreationRequestDTO;
import com.qubex.learn_now.exception.custom.NotFoundException;
import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.Section;
import com.qubex.learn_now.repository.CourseRepository;
import com.qubex.learn_now.repository.SectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;


    @Transactional
    public Section getSectionById(UUID sectionId) {

        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found"));

    }


    @Transactional
    public List<Section> getSectionsByCourseId(UUID courseId) {
        return sectionRepository.findByCourseIdOrderByOrderAsc(courseId);
    }

    @Transactional
    public Section createSection(SectionCreationRequestDTO sectionDTO) {
        Course course = courseRepository.findById(sectionDTO.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        Section section = new Section();
        section.setTitle(sectionDTO.getTitle());
        section.setDescription(sectionDTO.getDescription());
        section.setCourse(course);

        // Get max order and add 1
        int maxOrder = sectionRepository.findByCourseIdOrderByOrderAsc(course.getId())
                .stream().mapToInt(Section::getOrder).max().orElse(0);
        section.setOrder(maxOrder + 1);

        return sectionRepository.save(section);
    }


}
