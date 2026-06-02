package com.coursemanager.controller;

import com.coursemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam String role) {
        userService.changeUserRole(id, "ROLE_" + role.toUpperCase());
        return "redirect:/admin/users";
    }

    @GetMapping("/reports/popular-courses")
    public String popularCourses(Model model) {
        // W rzeczywistości pobieramy dane z serwisu – tu uproszczenie
        model.addAttribute("reportTitle", "Najpopularniejsze kursy");
        model.addAttribute("reportData", "Przykładowe dane (do implementacji)");
        return "admin/reports";
    }
}