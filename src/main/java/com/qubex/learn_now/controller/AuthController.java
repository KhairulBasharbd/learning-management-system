package com.qubex.learn_now.controller;


import com.qubex.learn_now.dto.request.UserRegistrationRequestDTO;
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
        model.addAttribute("userRegistrationRequestDTO", new UserRegistrationRequestDTO());

        return "auth/register";
    }


    @GetMapping("/login")
    public String login(Model model) {
        //if(AuthUtil.isAuthenticated()) return "redirect:/user/dashboard";
        return "auth/login";
    }


}
