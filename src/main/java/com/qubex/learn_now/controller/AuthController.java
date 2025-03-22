package com.qubex.learn_now.controller;


import com.qubex.learn_now.dto.request.UserRegistrationRequestDTO;
import com.qubex.learn_now.model.User;
import com.qubex.learn_now.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("userRegistrationRequestDTO", new UserRegistrationRequestDTO());

        return "auth/register";
    }

    @PostMapping("/register")
    public String submitRegistrationForm(@Valid @ModelAttribute("userRegistrationRequestDTO") UserRegistrationRequestDTO userRegistrationRequestDTO,
                                         BindingResult bindingResult,
                                         Model model,
                                         RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error");

            // Collect all validation error messages
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()) // Get default messages from dto
                    .collect(Collectors.toList());

            // Add error messages to the model
            model.addAttribute("validationErrors", errorMessages);

            return "auth/register";
        }

        try {
            User savedUser = userService.registerUser(userRegistrationRequestDTO);
            redirectAttributes.addFlashAttribute("success", true); // Add flash attribute
            return "redirect:/auth/login";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }



    @GetMapping("/login")
    public String login(Model model) {
        //if(AuthUtil.isAuthenticated()) return "redirect:/user/dashboard";
        return "auth/login";
    }




}
