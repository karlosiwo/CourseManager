package com.coursemanager.controller;

import com.coursemanager.dto.CourseDto;
import com.coursemanager.mapper.CourseMapper;
import com.coursemanager.model.entity.Course;
import com.coursemanager.service.CategoryService;
import com.coursemanager.service.CourseService;
import com.coursemanager.service.InstructorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping
    public String listCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @CookieValue(value = "courseSort", required = false) String cookieSort,
            @CookieValue(value = "courseDirection", required = false) String cookieDirection,
            HttpServletResponse response,
            Model model) {

        String sortField = courseService.normalizeSortField(sort != null ? sort : cookieSort);
        String normalizedDirection = courseService.normalizeDirection(direction != null ? direction : cookieDirection);
        LocalDate effectiveFromDate = fromDate != null ? fromDate : LocalDate.now();

        response.addCookie(buildCookie("courseSort", sortField));
        response.addCookie(buildCookie("courseDirection", normalizedDirection));

        Sort.Direction dir = Sort.Direction.fromString(normalizedDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortField));

        Specification<Course> spec = (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category").get("name"), category));
            }
            if (effectiveFromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), effectiveFromDate));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        Page<Course> coursePage = courseService.findAllCourses(spec, pageable);
        Page<CourseDto> courseDtoPage = coursePage.map(courseMapper::toDto);

        model.addAttribute("coursePage", courseDtoPage);
        model.addAttribute("categories", categoryService.findAllOrderByPopularity());
        model.addAttribute("currentSort", sortField);
        model.addAttribute("currentDirection", normalizedDirection);
        model.addAttribute("currentCategory", category);
        model.addAttribute("currentFromDate", effectiveFromDate);

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
        model.addAttribute("categories", categoryService.findAllOrderByPopularity());
        model.addAttribute("instructors", instructorService.findAll());
        return "course-form";
    }

    @PostMapping("/new")
    public String createCourse(@Valid @ModelAttribute CourseDto courseDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllOrderByPopularity());
            model.addAttribute("instructors", instructorService.findAll());
            return "course-form";
        }
        courseService.saveCourse(courseDto);
        redirectAttributes.addFlashAttribute("success", "Dodano kurs");
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var course = courseService.findCourseById(id);
        CourseDto dto = courseMapper.toDto(course);
        model.addAttribute("courseDto", dto);
        model.addAttribute("categories", categoryService.findAllOrderByPopularity());
        model.addAttribute("instructors", instructorService.findAll());
        return "course-form";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id, @Valid @ModelAttribute CourseDto courseDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllOrderByPopularity());
            model.addAttribute("instructors", instructorService.findAll());
            return "course-form";
        }
        courseService.updateCourse(id, courseDto);
        redirectAttributes.addFlashAttribute("success", "Zaktualizowano kurs");
        return "redirect:/courses";
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("success", "Usunięto kurs");
        return "redirect:/courses";
    }

    private Cookie buildCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
