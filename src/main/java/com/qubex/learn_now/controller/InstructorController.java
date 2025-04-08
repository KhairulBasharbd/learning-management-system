package com.qubex.learn_now.controller;


import com.qubex.learn_now.dto.request.CourseCreationRequestDTO;
import com.qubex.learn_now.dto.request.LectureCreationRequestDTO;
import com.qubex.learn_now.dto.request.SectionCreationRequestDTO;
import com.qubex.learn_now.model.Course;
import com.qubex.learn_now.model.Section;
import com.qubex.learn_now.repository.CourseCategoryRepository;
import com.qubex.learn_now.repository.CourseRepository;
import com.qubex.learn_now.service.CourseService;
import com.qubex.learn_now.service.InstructorService;
import com.qubex.learn_now.service.LectureService;
import com.qubex.learn_now.service.SectionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {

    private  final LectureService lectureService;
    private final InstructorService instructorService;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CourseRepository courseRepository;
    private  final CourseService courseService;
    private  final SectionService sectionService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "instructor/dashboard-content";
    }


    @GetMapping("/courses/create")
    public String createCourse(Model model) {

        model.addAttribute("categories", courseCategoryRepository.findAll());
        model.addAttribute("course", new CourseCreationRequestDTO());

        return "instructor/create-course-form";
    }

    @PostMapping("/courses/create")
    public String handleCourseCreation(@Valid @ModelAttribute("course") CourseCreationRequestDTO dto,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error");

            // Collect all validation error messages
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()) // Get default messages from dto
                    .collect(Collectors.toList());

            // Add error messages to the model
            model.addAttribute("validationErrors", errorMessages);

            return "instructor/create-course-form";
        }

        try {

            Course savedCourse = instructorService.saveCourse(dto);

            redirectAttributes.addFlashAttribute("success", "Course Creation Successful"); // Add flash attribute
            //return "redirect:/rider/onboarding/" + savedRider.getId();

            return "redirect:/instructor/dashboard";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "instructor/create-course-form";
        }


    }


    @GetMapping("/my-courses")
    public String myCourses(Model model) {

        List<Course> courses = courseService.getCoursesByInstructor();

        model.addAttribute("courses", courses);
        return "instructor/instructor-all-courses";
    }


    @Transactional
    @GetMapping("/courses/{courseId}/content")
    public String showCourseContent(@PathVariable UUID courseId, Model model) {

        Course course = courseService.getCourseById(courseId);
        List<Section> sections = sectionService.getSectionsByCourseId(courseId);

        // Initialize lectures for each section
        for (Section section : sections) {
            Hibernate.initialize(section.getLectures());
        }

        model.addAttribute("course", course);
        model.addAttribute("sections", sections);
        model.addAttribute("newSection", new SectionCreationRequestDTO());
        model.addAttribute("newLecture", new LectureCreationRequestDTO());

        return "instructor/course-content";
    }


    // Add new section
    @PostMapping("/courses/{courseId}/sections")
    public String addSection(@PathVariable UUID courseId,
                             @ModelAttribute SectionCreationRequestDTO sectionDTO,
                             RedirectAttributes redirectAttributes) {
        sectionDTO.setCourseId(courseId);
        sectionService.createSection(sectionDTO);
        redirectAttributes.addFlashAttribute("success", "Section added successfully");
        return "redirect:/instructor/courses/" + courseId + "/content";
    }

    // Add new lecture
    @PostMapping("/courses/sections/{sectionId}/lectures")
    public String addLecture(@PathVariable UUID sectionId,
                             @ModelAttribute LectureCreationRequestDTO lectureDTO,
                             RedirectAttributes redirectAttributes) {

        lectureDTO.setSectionId(sectionId);

        // Get course ID for redirect
        Section section = sectionService.getSectionById(sectionId);
        UUID courseId = section.getCourse().getId();

        lectureService.createLecture(lectureDTO);
        redirectAttributes.addFlashAttribute("success", "Lecture added successfully");
        return "redirect:/instructor/courses/" + courseId + "/content";
    }

}
