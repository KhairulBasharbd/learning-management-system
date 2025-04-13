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


}
