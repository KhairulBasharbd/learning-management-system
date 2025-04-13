package com.qubex.learn_now.controller;


import com.qubex.learn_now.exception.custom.NotFoundException;
import com.qubex.learn_now.repository.StudentRepository;
import com.qubex.learn_now.service.LectureCompletionService;
import org.springframework.security.core.Authentication;
import com.qubex.learn_now.dto.CourseDTO;
import com.qubex.learn_now.enums.LectureType;
import com.qubex.learn_now.model.*;
import com.qubex.learn_now.repository.CourseCategoryRepository;
import com.qubex.learn_now.service.CourseProgressService;
import com.qubex.learn_now.service.CourseService;
import com.qubex.learn_now.service.SectionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final CourseService courseService;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CourseProgressService courseProgressService;
    private  final SectionService sectionService;
    private final LectureCompletionService lectureCompletionService;
    private final StudentRepository studentRepository;

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



//---------------------------------------------------------------------------------------------------------



    @GetMapping("/courses/{courseId}/learn")
    @Transactional
    public String learnCourse(@PathVariable UUID courseId,
                              @RequestParam(required = false) UUID lectureId,
                              Model model,
                              Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        // Then fetch your Student from repository
        Student student = studentRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("Student not found"));

       // Student student = (Student) authentication.getPrincipal();

        Course course = courseService.getCourseById(courseId);

        // Check if the student is enrolled in this course
        if (!courseProgressService.isStudentEnrolled(student.getId(), courseId)) {
            return "redirect:/student/courses/" + courseId;
        }

        // Get course progress
        CourseProgress courseProgress = courseProgressService.getOrCreateProgress(student.getId(), courseId);

        // Update last accessed date
        courseProgress.setLastAccessedDate(LocalDateTime.now());
        courseProgressService.save(courseProgress);

        // Get all sections and lectures for the course
        List<Section> sections = course.getSections();

        // Initialize lectures for each section
//        for (Section section : sections) {
//            Hibernate.initialize(section.getLectures());
//        }

        // Get completed lectures ID
        Set<UUID> completedLectures = lectureCompletionService
                .findByStudentId(student.getId())
                .stream()
                .map(completion -> completion.getLesson().getId())
                .collect(Collectors.toSet());

        // Find the current lecture
        Lecture currentLecture = null;
        if (lectureId != null) {
            // If lecture ID is provided, find that lecture
            for (Section section : sections) {
                for (Lecture lecture : section.getLectures()) {
                    if (lecture.getId().equals(lectureId)) {
                        currentLecture = lecture;
                        break;
                    }
                }
                if (currentLecture != null) break;
            }
        } else if (courseProgress.getCurrentLesson() != null) {
            // If no lecture ID provided, use the current lesson from progress
            currentLecture = courseProgress.getCurrentLesson();
        } else if (!sections.isEmpty() && !sections.get(0).getLectures().isEmpty()) {
            // If no current lesson tracked, default to first lecture
            currentLecture = sections.get(0).getLectures().get(0);
        }

        // If a current lecture was found, update it in the progress
        if (currentLecture != null && !currentLecture.equals(courseProgress.getCurrentLesson())) {
            courseProgress.setCurrentLesson(currentLecture);
            courseProgressService.save(courseProgress);
        }

        // Find previous and next lectures
        Lecture previousLecture = null;
        Lecture nextLecture = null;

        if (currentLecture != null) {
            // Create a flat list of all lectures in order
            List<Lecture> allLectures = new ArrayList<>();
            for (Section section : sections) {
                allLectures.addAll(section.getLectures());
            }

            // Find current lecture index
            int currentIndex = -1;
            for (int i = 0; i < allLectures.size(); i++) {
                if (allLectures.get(i).getId().equals(currentLecture.getId())) {
                    currentIndex = i;
                    break;
                }
            }

            // Set previous and next lectures
            if (currentIndex > 0) {
                previousLecture = allLectures.get(currentIndex - 1);
            }

            if (currentIndex < allLectures.size() - 1) {
                nextLecture = allLectures.get(currentIndex + 1);
            }
        }

        // Add all required data to the model
        model.addAttribute("course", course);
        model.addAttribute("sections", sections);
        model.addAttribute("currentLecture", currentLecture);
        model.addAttribute("previousLecture", previousLecture);
        model.addAttribute("nextLecture", nextLecture);
        model.addAttribute("courseProgress", courseProgress);
        model.addAttribute("completedLectures", completedLectures);

        return "student/learning-course-details";
    }



    @PostMapping("/courses/{courseId}/lectures/{lectureId}/mark-complete")
    public String markLectureAsComplete(@PathVariable UUID courseId,
                                        @PathVariable UUID lectureId,
                                        Authentication authentication,
                                        HttpServletRequest request) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        // Then fetch your Student from repository
        Student student = studentRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("Student not found"));


       // Student student = (Student) authentication.getPrincipal();

        // Mark the lecture as complete
        lectureCompletionService.markLectureAsComplete(student.getId(), lectureId);

        // Update course progress
        courseProgressService.updateProgress(student.getId(), courseId);

        // Redirect back to the same page
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/student/courses/" + courseId + "/learn");
    }

    @PostMapping("/courses/{courseId}/lectures/{lectureId}/mark-incomplete")
    public String markLectureAsIncomplete(@PathVariable UUID courseId,
                                          @PathVariable UUID lectureId,
                                          Authentication authentication,
                                          HttpServletRequest request) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        // Then fetch your Student from repository
        Student student = studentRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        //Student student = (Student) authentication.getPrincipal();

        // Mark the lecture as incomplete
        lectureCompletionService.markLectureAsIncomplete(student.getId(), lectureId);

        // Update course progress
        courseProgressService.updateProgress(student.getId(), courseId);

        // Redirect back to the same page
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/student/courses/" + courseId + "/learn");
    }

}
