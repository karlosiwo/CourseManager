package com.coursemanager.repository;

import com.coursemanager.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);

    @Query(value = """
            SELECT c.*
            FROM categories c
            LEFT JOIN courses co ON co.category_id = c.id
            LEFT JOIN enrollments e ON e.course_id = co.id AND e.status = 'AKTYWNY'
            GROUP BY c.id, c.name
            ORDER BY COUNT(e.id) DESC, c.name ASC
            """, nativeQuery = true)
    List<Category> findAllOrderByPopularity();
}
