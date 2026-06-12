package com.coursemanager.controller;

import com.coursemanager.service.CourseService;
import com.coursemanager.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CourseService courseService;
    private final InstructorService instructorService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activeCoursesCount", courseService.countActiveCourses());
        model.addAttribute("instructorsCount", instructorService.countInstructors());
        return "home";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
