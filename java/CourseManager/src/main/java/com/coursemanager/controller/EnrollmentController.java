package com.coursemanager.controller;

import com.coursemanager.model.entity.User;
import com.coursemanager.service.EnrollmentService;
import com.coursemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username);
    }

    @PostMapping("/enroll/{courseId}")
    public String enroll(@PathVariable Long courseId) {
        User user = getCurrentUser();
        enrollmentService.enrollUser(user.getId(), courseId);
        return "redirect:/courses";
    }

    @GetMapping("/my-enrollments")
    public String myEnrollments(Model model) {
        User user = getCurrentUser();
        model.addAttribute("enrollments", enrollmentService.getUserEnrollments(user));
        return "my-enrollments";
    }

    @PostMapping("/cancel/{enrollmentId}")
    public String cancel(@PathVariable Long enrollmentId) {
        enrollmentService.cancelEnrollment(enrollmentId);
        return "redirect:/my-enrollments";
    }
}