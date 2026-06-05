package com.coursemanager.controller;

import com.coursemanager.service.UserService;
import com.coursemanager.service.CourseService;
import com.coursemanager.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * Lista wszystkich użytkowników (dla administratora)
     */
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    /**
     * Zmiana roli użytkownika
     */
    @GetMapping("/users/change-role/{id}")
    public String changeRoleForm(@PathVariable Long id, Model model) {
        var user = userService.findUserById(id); // zakładając, że masz taką metodę w UserService
        model.addAttribute("user", user);
        // przekaż dostępne role
        model.addAttribute("roles", new String[]{"ROLE_LIMITED_USER", "ROLE_FULL_USER", "ROLE_ADMIN"});
        return "admin/change-role";
    }

    @PostMapping("/users/change-role/{id}")
    public String changeRole(@PathVariable Long id, @RequestParam String role) {
        userService.changeUserRole(id, role);
        return "redirect:/admin/users";
    }

    /**
     * Panel raportów – widok główny (NAPRAWA BŁĘDU)
     */
    @GetMapping("/reports")
    public String reportsDashboard(Model model) {
        // Opcjonalnie dodaj statystyki ogólne
        long userCount = userService.countUsers();
        long courseCount = courseService.countCourses();
        long enrollmentCount = enrollmentService.countEnrollments();

        model.addAttribute("userCount", userCount);
        model.addAttribute("courseCount", courseCount);
        model.addAttribute("enrollmentCount", enrollmentCount);
        model.addAttribute("pageTitle", "Panel Raportów");
        return "admin/reports";
    }

    /**
     * Przykładowy raport – najpopularniejsze kursy
     * (już może istnieć, dodaję dla kompletności)
     */
    @GetMapping("/reports/popular-courses")
    public String popularCourses(Model model) {
        // Tu możesz pobrać dane z serwisu
        model.addAttribute("popularCourses", enrollmentService.findMostPopularCourses());
        return "admin/popular-courses";
    }
}