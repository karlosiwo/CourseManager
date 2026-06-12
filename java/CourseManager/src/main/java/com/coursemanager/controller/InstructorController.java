package com.coursemanager.controller;

import com.coursemanager.dto.InstructorDto;
import com.coursemanager.exception.BusinessException;
import com.coursemanager.service.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instructors")
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("instructors", instructorService.findAll());
        return "instructors";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("instructorDto", new InstructorDto());
        return "instructor-form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("instructorDto") InstructorDto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "instructor-form";
        }
        try {
            instructorService.create(dto);
            redirectAttributes.addFlashAttribute("success", "Dodano prowadzącego");
            return "redirect:/instructors";
        } catch (BusinessException e) {
            result.reject("instructor.exists", e.getMessage());
            return "instructor-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("instructorDto", instructorService.toDto(instructorService.findById(id)));
        return "instructor-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("instructorDto") InstructorDto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        dto.setId(id);
        if (result.hasErrors()) {
            return "instructor-form";
        }
        try {
            instructorService.update(id, dto);
            redirectAttributes.addFlashAttribute("success", "Zaktualizowano prowadzącego");
            return "redirect:/instructors";
        } catch (BusinessException e) {
            result.reject("instructor.exists", e.getMessage());
            return "instructor-form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            instructorService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Usunięto prowadzącego");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/instructors";
    }
}
