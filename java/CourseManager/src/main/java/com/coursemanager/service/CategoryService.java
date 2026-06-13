package com.coursemanager.service;

import com.coursemanager.dto.CategoryDto;
import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.Category;
import com.coursemanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
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

    public boolean isNameAvailable(String name, Long currentCategoryId) {
        if (name == null || name.isBlank()) {
            return false;
        }
        String normalizedName = name.trim();
        return categoryRepository.findByNameIgnoreCase(normalizedName)
                .map(existing -> currentCategoryId != null && existing.getId().equals(currentCategoryId))
                .orElse(true);
    }

    public CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getActive(),
                category.getCreatedAt()
        );
    }

    public Category create(CategoryDto dto) {
        if (!isNameAvailable(dto.getName(), null)) {
            throw new BusinessException("Kategoria o takiej nazwie już istnieje");
        }
        Category category = new Category();
        fillCategory(category, dto);
        return categoryRepository.save(category);
    }

    public Category update(Long id, CategoryDto dto) {
        Category category = findById(id);
        if (!isNameAvailable(dto.getName(), id)) {
            throw new BusinessException("Kategoria o takiej nazwie już istnieje");
        }
        fillCategory(category, dto);
        return categoryRepository.save(category);
    }

    private void fillCategory(Category category, CategoryDto dto) {
        category.setName(dto.getName().trim());
        category.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        category.setActive(dto.getActive() != null ? dto.getActive() : true);
    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Nie można usunąć kategorii używanej przez kursy");
        }
    }

    public void addCategory(String name) {
        CategoryDto dto = new CategoryDto();
        dto.setName(name);
        dto.setActive(true);
        create(dto);
    }
}
