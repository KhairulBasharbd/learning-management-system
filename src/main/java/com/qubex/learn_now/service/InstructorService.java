package com.qubex.learn_now.service;

import com.qubex.learn_now.dto.request.CourseCreationRequestDTO;
import com.qubex.learn_now.enums.CourseLevel;
import com.qubex.learn_now.enums.CourseVisibility;
import com.qubex.learn_now.exception.custom.NotFoundException;
import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.Instructor;
import com.qubex.learn_now.repository.CourseRepository;
import com.qubex.learn_now.repository.InstructorRepository;
import com.qubex.learn_now.util.AuthUtil;
import com.qubex.learn_now.util.FileStorageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class InstructorService {

    private final CourseRepository courseRepository;
    //private final ModelMapper modelMapper;
    private final FileStorageUtil fileStorageUtil;
    private final InstructorRepository instructorRepository;



    public Instructor getAuthenticatedInstructor() {
        String authenticatedInstructorEmail = AuthUtil.getAuthenticatedUsername();
        Optional<Instructor> instructor = instructorRepository.findByEmail(authenticatedInstructorEmail);
        return instructor.orElseThrow(() -> new NotFoundException("Instructor not Authenticated"));
    }

    @Transactional
    public Course saveCourse(CourseCreationRequestDTO courseDTO) {
        //Course course = modelMapper.map(courseDTO, Course.class);

        Course course = convertToEntity(courseDTO);

        Instructor instructor = getAuthenticatedInstructor();
        course.setInstructor(instructor);

        // this part is for Thumbnail and syllabus file upload
//        if (!courseDTO.getThumbnail().isEmpty()) {
//            String thumbnailUrl = fileStorageUtil.uploadFile(courseDTO.getThumbnail(), "thumbnails");
//            course.setThumbnailUrl(thumbnailUrl);
//        }
//
//        if (!courseDTO.getSyllabus().isEmpty()) {
//            String syllabusUrl = fileStorageUtil.uploadFile(courseDTO.getSyllabus(), "syllabus");
//            course.setSyllabusUrl(syllabusUrl);
//        }

        return courseRepository.save(course);
    }


    private Course convertToEntity(CourseCreationRequestDTO dto) {
        Course course = new Course();

        // Set basic properties
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());
        course.setTags(dto.getTags());
        course.setDuration(dto.getDuration());

        // Handle UUID conversion for CategoryId
        if (dto.getCategoryId() != null) {

            course.setCategoryId(dto.getCategoryId());
            //course.setCategoryId(dto.getCategoryId());
            // Option 2: Create a deterministic UUID from the Long value
//            course.setCategoryId(UUID.nameUUIDFromBytes(
//                    ("category-" + dto.getCategoryId()).getBytes()
//            ));
        }

        // Convert String enum values to actual enum types
        if (dto.getLevel() != null) {
            try {
                course.setLevel(CourseLevel.valueOf(dto.getLevel()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid course level: " + dto.getLevel());
            }
        }

        if (dto.getVisibility() != null) {
            try {
                course.setVisibility(CourseVisibility.valueOf(dto.getVisibility()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid visibility: " + dto.getVisibility());
            }
        }

        return course;
    }
}

