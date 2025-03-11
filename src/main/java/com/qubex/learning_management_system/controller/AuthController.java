package com.qubex.learning_management_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/register")
    public String getRegisterPage(Model model) {

        return "auth/register";
    }

    @GetMapping("/login")
    public String getLoginPage(){

        return "auth/login";

    }


}
