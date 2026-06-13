package com.coursemanager.controller;

import com.coursemanager.model.entity.User;
import com.coursemanager.service.EnrollmentService;
import com.coursemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MyEnrollmentController {
    private final EnrollmentService enrollmentService;
    private final UserService userService;

    @GetMapping("/my-enrollments")
    public String myEnrollments(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(userDetails.getUsername());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "enrollmentDate"));
        model.addAttribute("enrollmentPage", enrollmentService.getUserEnrollments(user, pageable));
        return "my-enrollments";
    }
}
