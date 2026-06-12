package com.coursemanager.controller;

import com.coursemanager.model.entity.User;
import com.coursemanager.service.EnrollmentService;
import com.coursemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MyEnrollmentController {
    private final EnrollmentService enrollmentService;
    private final UserService userService;

    @GetMapping("/my-enrollments")
    public String myEnrollments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("enrollments", enrollmentService.getUserEnrollments(user));
        return "my-enrollments";
    }
}
