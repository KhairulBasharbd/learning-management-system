package com.qubex.learn_now.service;

import com.qubex.learn_now.dto.SectionDTO;
import com.qubex.learn_now.dto.request.CourseSearchDTO;
import com.qubex.learn_now.dto.CourseDTO;
import com.qubex.learn_now.dto.response.LectureDTO;
import com.qubex.learn_now.enums.CourseVisibility;
import com.qubex.learn_now.exception.custom.NotFoundException;
import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.Instructor;
import com.qubex.learn_now.model.Lecture;
import com.qubex.learn_now.model.Section;
import com.qubex.learn_now.repository.CourseRepository;
import com.qubex.learn_now.repository.InstructorRepository;
import com.qubex.learn_now.repository.LectureRepository;
import com.qubex.learn_now.repository.SectionRepository;
import com.qubex.learn_now.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final SectionRepository sectionRepository;


    @Transactional
    public List<Course> getCoursesByInstructor() {

        String authenticatedEmail = AuthUtil.getAuthenticatedUsername();

        Instructor instructor = instructorRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Instructor"));


        return courseRepository.findByInstructor(instructor);
    }

    @Transactional
    public Course getCourseById(UUID courseId) {

        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }


    public List<CourseDTO> getCourses() {

        List<Course> courses = courseRepository.findAllByVisibilityOrderByTitleAsc(CourseVisibility.PUBLIC);

        return courses.stream()
                .map(course -> {
                    CourseDTO dto = new CourseDTO();

                    dto.setId(course.getId());
                    dto.setTitle(course.getTitle());
                    dto.setDescription(course.getDescription());
                    dto.setLevel(course.getLevel());
                    dto.setPrice(course.getPrice());
                    dto.setThumbnailUrl(course.getThumbnailUrl());
                    dto.setDuration(course.getDuration());
                    int lessonCount = lectureRepository.countLessonsByCourseId(course.getId());
                    dto.setLessonCount(lessonCount);

                    return dto;
                })
                .collect(Collectors.toList());

    }

//    public Page<CourseDTO> searchCourses(CourseSearchDTO searchDTO, int page, int size) {
//        String sortBy = searchDTO.getSortBy() != null ? searchDTO.getSortBy() : "title";
//        String sortDir = searchDTO.getSortDirection() != null ? searchDTO.getSortDirection() : "ASC";
//        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//
//        BigDecimal maxPrice = searchDTO.getMaxPrice();
//
//        Page<Course> coursesPage = courseRepository.searchCourses(
//                CourseVisibility.PUBLIC,
//                searchDTO.getKeyword(),
//                searchDTO.getCategoryId(),
//                searchDTO.getLevel(),
//                maxPrice,
//                pageable
//        );
//
//        return coursesPage.map(this::convertToDTO);
//    }

//    private CourseDTO convertToDTO(Course course) {
//        return convertToDTO(course, false);
//    }

//    private CourseDTO convertToDTO(Course course, boolean includeSections) {
//        CourseDTO dto = new CourseDTO();
//
//        dto.setId(course.getId());
//        dto.setTitle(course.getTitle());
//        dto.setDescription(course.getDescription());
//        dto.setCategoryId(course.getCategoryId());
//        dto.setLevel(course.getLevel());
//        dto.setPrice(course.getPrice());
//        dto.setThumbnailUrl(course.getThumbnailUrl());
//        dto.setSyllabusUrl(course.getSyllabusUrl());
//        dto.setVisibility(course.getVisibility());
//        dto.setTags(course.getTags());
//        dto.setDuration(course.getDuration());
//
//        if (course.getInstructor() != null) {
//            dto.setInstructorId(course.getInstructor().getId());
//
//            //dto.setInstructorName(course.getInstructor().getFirstName() + " " + course.getInstructor().getLastName());
//            dto.setInstructorName(course.getInstructor().getFullName());
//        }
//
//        int lessonCount = lectureRepository.countLessonsByCourseId(course.getId());
//        dto.setLessonCount(lessonCount);
//
//        if (includeSections) {
//            List<Section> sections = sectionRepository.findByCourseIdOrderByOrderAsc(course.getId());
//            dto.setSections(sections.stream()
//                    .map(section -> {
//                        SectionDTO sectionDTO = new SectionDTO();
//                        sectionDTO.setId(section.getId());
//                        sectionDTO.setTitle(section.getTitle());
//                        sectionDTO.setDescription(section.getDescription());
//                        sectionDTO.setOrder(section.getOrder());
//
//                        List<Lecture> lessons = lectureRepository.findBySectionIdOrderByOrderAsc(section.getId());
//                        sectionDTO.setLessons(lessons.stream()
//                                .map(lesson -> {
//                                    LectureDTO lessonDTO = new LectureDTO();
//                                    lessonDTO.setId(lesson.getId());
//                                    lessonDTO.setTitle(lesson.getTitle());
//                                    lessonDTO.setContent(lesson.getContent());
//                                    lessonDTO.setVideoUrl(lesson.getContent());
//                                    lessonDTO.setOrder(lesson.getOrder());
//                                    return lessonDTO;
//                                })
//                                .collect(Collectors.toList()));
//
//                        return sectionDTO;
//                    })
//                    .collect(Collectors.toList()));
//        }
//
//        return dto;
//    }

}
