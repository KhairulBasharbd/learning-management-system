package com.qubex.learning_management_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    @GetMapping("/")
    public String getLandingPage() {

        return "landingPage";
    }


}
