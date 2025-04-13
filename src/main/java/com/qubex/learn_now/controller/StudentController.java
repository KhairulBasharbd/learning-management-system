package com.qubex.learn_now.controller;


import com.qubex.learn_now.dto.request.CourseSearchDTO;
import com.qubex.learn_now.dto.CourseDTO;
import com.qubex.learn_now.enums.LectureType;
import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.Section;
import com.qubex.learn_now.repository.CourseCategoryRepository;
import com.qubex.learn_now.service.CourseProgressService;
import com.qubex.learn_now.service.CourseService;
import com.qubex.learn_now.service.SectionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final CourseService courseService;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CourseProgressService courseProgressService;
    private  final SectionService sectionService;


    @GetMapping("/dashboard")
    public String dashboard(Model model,Principal principal) {


        List<Course> enrolledCourses = courseProgressService.getEnrolledCourseByStudent(principal.getName());
        //model.addAttribute(enrolledCourses);
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "student/dashboard-content";
    }

    @GetMapping("/my-courses")
    public String studentCourses(Model model,Principal principal) {


        List<Course> enrolledCourses = courseProgressService.getEnrolledCourseByStudent(principal.getName());
        //model.addAttribute(enrolledCourses);
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "student/dashboard-content";
    }


    @GetMapping("/courses")
    public String listCourses(Model model) {

        try {

            List<CourseDTO> coursePage = courseService.getCourses();

            model.addAttribute("courses", coursePage);

            return "student/course-listing";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unable to load courses. Please try again later.");
            return "student/course-listing";

        }
    }


    @Transactional
    @GetMapping("/courses/{courseId}")
    public String viewCourseDetails(@PathVariable UUID courseId,
                                    Principal principal,
                                    Model model) {
        Course course = courseService.getCourseById(courseId);
        boolean hasProgress = courseProgressService.hasCourseProgress(principal.getName(), courseId);

        List<Section> sections = sectionService.getSectionsByCourseId(courseId);

        // Initialize lectures for each section
        for (Section section : sections) {
            Hibernate.initialize(section.getLectures());
        }

        model.addAttribute("sections", sections);
        model.addAttribute("videoType", LectureType.VIDEO);
        model.addAttribute("pdfType", LectureType.PDF);
        model.addAttribute("textType", LectureType.TEXT);


        model.addAttribute("course", course);
        model.addAttribute("isEnrolled", hasProgress);
        return "student/course-details";
    }


    @PostMapping("/courses/{courseId}/enroll")
    public String enrollInCourse(@PathVariable UUID courseId,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            courseProgressService.createCourseProgress(principal.getName(), courseId);
            redirectAttributes.addFlashAttribute("success", "Successfully enrolled in the course!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error enrolling in course: " + e.getMessage());
        }
        return "redirect:/student/courses/" + courseId;
    }



}
