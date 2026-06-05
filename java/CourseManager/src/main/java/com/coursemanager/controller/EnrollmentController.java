package com.coursemanager.controller;

import com.coursemanager.model.entity.User;
import com.coursemanager.service.EnrollmentService;
import com.coursemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enroll")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @PostMapping("/{courseId}")
    public String enroll(@PathVariable Long courseId,
                         @AuthenticationPrincipal UserDetails userDetails,
                         RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return "redirect:/login";
        }
        try {
            enrollmentService.enrollUser(user.getId(), courseId);
            redirectAttributes.addFlashAttribute("success", "Zapisano na kurs!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Nie udało się zapisać: " + e.getMessage());
        }
        return "redirect:/courses";
    }
}