package com.library.library_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.library.library_management_system.entity.Member;
import com.library.library_management_system.service.AuthService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AuthService authService;
    @GetMapping({"/", "/dashboard"})
    public String home() {
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        // Role is now set from the signup form; default to MEMBER only if missing
        if (member.getRole() == null) {
            member.setRole(Member.Role.MEMBER);
        }
        boolean registered = authService.register(member);
        if (registered) {
            redirectAttributes.addFlashAttribute("toast", "Signup successful! Please log in.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("toast", "Username or email already exists.");
            return "redirect:/signup";
        }
    }
}
