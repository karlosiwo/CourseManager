package com.coursemanager.controller.rest;

import com.coursemanager.dto.CategoryDto;
import com.coursemanager.model.entity.Category;
import com.coursemanager.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> list() {
        return categoryService.findAll().stream().map(categoryService::toDto).toList();
    }

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable Long id) {
        return categoryService.toDto(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto dto) {
        Category saved = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.toDto(saved));
    }

    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id, @Valid @RequestBody CategoryDto dto) {
        return categoryService.toDto(categoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
