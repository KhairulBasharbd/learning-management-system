package com.qubex.learn_now.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard/studentDashboard";
    }



}
