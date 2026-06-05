package com.coursemanager.controller;

import com.coursemanager.dto.CourseDto;
import com.coursemanager.mapper.CourseMapper;
import com.coursemanager.service.CategoryService;
import com.coursemanager.service.CourseService;
import com.coursemanager.service.InstructorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.coursemanager.model.entity.Course;
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

    @Autowired
    private CourseMapper courseMapper;

    // Lista kursów z paginacją, sortowaniem i filtrowaniem
    @GetMapping
    public String listCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            Model model) {

        Sort.Direction dir = (direction != null && direction.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = (sort != null && !sort.isEmpty()) ? sort : "startDate";
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortField));

        // Budujemy specyfikację na podstawie filtrów
        Specification<Course> spec = (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category").get("name"), category));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), fromDate));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        Page<Course> coursePage = courseService.findAllCourses(spec, pageable);
        Page<CourseDto> courseDtoPage = coursePage.map(courseMapper::toDto);

        model.addAttribute("coursePage", courseDtoPage);
        model.addAttribute("categories", categoryService.findAll());
        // Przekazujemy też aktualne filtry do zachowania w linkach paginacji
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentDirection", direction);
        model.addAttribute("currentCategory", category);
        model.addAttribute("currentFromDate", fromDate);

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
        CourseDto dto = courseMapper.toDto(course);
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