package com.qubex.learn_now.service;

import com.qubex.learn_now.dto.request.CourseCreationRequestDTO;
import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.repository.CourseRepository;
import com.qubex.learn_now.util.FileStorageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


@Service
@RequiredArgsConstructor
public class InstructorService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final FileStorageUtil fileStorageUtil;

    @Transactional
    public void saveCourse(CourseCreationRequestDTO courseDTO) {
        Course course = modelMapper.map(courseDTO, Course.class);

        if (!courseDTO.getThumbnail().isEmpty()) {
            String thumbnailUrl = fileStorageUtil.uploadFile(courseDTO.getThumbnail(), "thumbnails");
            course.setThumbnailUrl(thumbnailUrl);
        }

        if (!courseDTO.getSyllabus().isEmpty()) {
            String syllabusUrl = fileStorageUtil.uploadFile(courseDTO.getSyllabus(), "syllabus");
            course.setSyllabusUrl(syllabusUrl);
        }

        courseRepository.save(course);
    }
}

