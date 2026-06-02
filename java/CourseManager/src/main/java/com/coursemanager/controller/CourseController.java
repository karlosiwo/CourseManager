package com.coursemanager.controller;

import com.coursemanager.dto.CourseDto;
import com.coursemanager.service.CategoryService;
import com.coursemanager.service.CourseService;
import com.coursemanager.service.InstructorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private InstructorService instructorService;

    @GetMapping
    public String listCourses(@RequestParam(required = false) String sort,
                              @RequestParam(required = false) String direction,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                              Model model) {
        model.addAttribute("courses", courseService.findAllCourses(sort, direction, category, fromDate));
        model.addAttribute("categories", categoryService.findAll());
        return "courses";
    }

    @GetMapping("/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.findCourseById(id));
        return "course-details";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("courseDto", new CourseDto());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("instructors", instructorService.findAll());
        return "course-form";
    }

    @PostMapping("/new")
    public String createCourse(@Valid @ModelAttribute CourseDto courseDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("instructors", instructorService.findAll());
            return "course-form";
        }
        courseService.saveCourse(courseDto);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var course = courseService.findCourseById(id);
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setStartDate(course.getStartDate());
        dto.setMaxSeats(course.getMaxSeats());
        dto.setCategoryId(course.getCategory().getId());
        dto.setInstructorId(course.getInstructor().getId());
        model.addAttribute("courseDto", dto);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("instructors", instructorService.findAll());
        return "course-form";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id, @Valid @ModelAttribute CourseDto courseDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("instructors", instructorService.findAll());
            return "course-form";
        }
        courseService.updateCourse(id, courseDto);
        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}