package com.qubex.learn_now.controller;


import com.qubex.learn_now.dto.request.CourseCreationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "instructor/dashboard-content";
    }


    @GetMapping("/courses/create")
    public String createCourse(Model model) {

        model.addAttribute("course", new CourseCreationRequestDTO());

        return "instructor/create-course-form";
    }




}
