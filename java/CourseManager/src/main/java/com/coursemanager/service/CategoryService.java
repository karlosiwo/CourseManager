package com.coursemanager.service;

import com.coursemanager.dto.CategoryDto;
import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.Category;
import com.coursemanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findAllOrderByPopularity() {
        return categoryRepository.findAllOrderByPopularity();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new BusinessException("Nie znaleziono kategorii"));
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByNameIgnoreCase(name);
    }

    public CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public Category create(CategoryDto dto) {
        if (existsByName(dto.getName())) {
            throw new BusinessException("Kategoria o takiej nazwie już istnieje");
        }
        Category category = new Category();
        category.setName(dto.getName().trim());
        return categoryRepository.save(category);
    }

    public Category update(Long id, CategoryDto dto) {
        Category category = findById(id);
        String newName = dto.getName().trim();
        categoryRepository.findByNameIgnoreCase(newName)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new BusinessException("Kategoria o takiej nazwie już istnieje"); });
        category.setName(newName);
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Nie można usunąć kategorii używanej przez kursy");
        }
    }

    public void addCategory(String name) {
        CategoryDto dto = new CategoryDto(null, name);
        create(dto);
    }
}
