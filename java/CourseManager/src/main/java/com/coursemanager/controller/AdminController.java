package com.coursemanager.controller;

import com.coursemanager.service.UserService;
import com.coursemanager.service.CourseService;
import com.coursemanager.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/users")
    public String listUsers(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "username"));
        model.addAttribute("userPage", userService.findAllUsers(pageable));
        return "admin/users";
    }

    @GetMapping("/users/change-role/{id}")
    public String changeRoleForm(@PathVariable Long id, Model model) {
        var user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", new String[]{"ROLE_LIMITED_USER", "ROLE_FULL_USER", "ROLE_ADMIN"});
        return "admin/change-role";
    }

    @PostMapping("/users/change-role/{id}")
    public String changeRole(@PathVariable Long id, @RequestParam String role) {
        userService.changeUserRole(id, role);
        return "redirect:/admin/users";
    }

    @GetMapping("/reports")
    public String reportsDashboard(Model model) {
        long userCount = userService.countUsers();
        long courseCount = courseService.countCourses();
        long enrollmentCount = enrollmentService.countEnrollments();

        model.addAttribute("userCount", userCount);
        model.addAttribute("courseCount", courseCount);
        model.addAttribute("enrollmentCount", enrollmentCount);
        model.addAttribute("pageTitle", "Panel Raportów");
        return "admin/reports";
    }

    @GetMapping("/reports/popular-courses")
    public String popularCourses(Model model) {
        model.addAttribute("popularCourses", enrollmentService.findMostPopularCourses());
        return "admin/popular-courses";
    }

    @GetMapping("/reports/free-seats")
    public String freeSeats(Model model) {
        model.addAttribute("freeSeats", enrollmentService.findCoursesWithFreeSeats());
        return "admin/free-seats";
    }

    @GetMapping("/reports/course-participants")
    public String courseParticipants(@RequestParam(required = false) Long courseId, Model model) {
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("selectedCourseId", courseId);
        if (courseId != null) {
            model.addAttribute("selectedCourse", courseService.findCourseById(courseId));
            model.addAttribute("participants", enrollmentService.findParticipantsByCourseProcedure(courseId));
        }
        return "admin/course-participants";
    }
}
