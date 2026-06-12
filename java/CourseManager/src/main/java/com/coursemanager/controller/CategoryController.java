package com.coursemanager.controller;

import com.coursemanager.dto.CategoryDto;
import com.coursemanager.exception.BusinessException;
import com.coursemanager.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("categoryDto", new CategoryDto());
        return "category-form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("categoryDto") CategoryDto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "category-form";
        }
        try {
            categoryService.create(dto);
            redirectAttributes.addFlashAttribute("success", "Dodano kategorię");
            return "redirect:/categories";
        } catch (BusinessException e) {
            result.rejectValue("name", "category.exists", e.getMessage());
            return "category-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("categoryDto", categoryService.toDto(categoryService.findById(id)));
        return "category-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("categoryDto") CategoryDto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        dto.setId(id);
        if (result.hasErrors()) {
            return "category-form";
        }
        try {
            categoryService.update(id, dto);
            redirectAttributes.addFlashAttribute("success", "Zaktualizowano kategorię");
            return "redirect:/categories";
        } catch (BusinessException e) {
            result.rejectValue("name", "category.exists", e.getMessage());
            return "category-form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Usunięto kategorię");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categories";
    }
}
