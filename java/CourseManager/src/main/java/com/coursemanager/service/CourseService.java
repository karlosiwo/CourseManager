package com.coursemanager.service;

import com.coursemanager.dto.CourseDto;
import com.coursemanager.model.entity.Course;
import com.coursemanager.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CategoryService categoryService;
    private final InstructorService instructorService;

    // Metoda z paginacją i specyfikacją (używana w nowym kontrolerze)
    public Page<Course> findAllCourses(Specification<Course> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }

    // Stara metoda (zachowana dla kompatybilności z ewentualnymi innymi kontrolerami)
    public List<Course> findAllCourses(String sortBy, String direction, String categoryName, LocalDate fromDate) {
        Sort.Direction dir = direction != null && direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(dir, sortBy != null ? sortBy : "startDate");

        Specification<Course> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (categoryName != null && !categoryName.isEmpty()) {
                predicates.add(cb.equal(root.get("category").get("name"), categoryName));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), fromDate));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return courseRepository.findAll(spec, sort);
    }

    public Course findCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public void saveCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setStartDate(courseDto.getStartDate());
        course.setMaxSeats(courseDto.getMaxSeats());
        course.setCategory(categoryService.findById(courseDto.getCategoryId()));
        course.setInstructor(instructorService.findById(courseDto.getInstructorId()));
        courseRepository.save(course);
    }

    public void updateCourse(Long id, CourseDto courseDto) {
        Course course = findCourseById(id);
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setStartDate(courseDto.getStartDate());
        course.setMaxSeats(courseDto.getMaxSeats());
        course.setCategory(categoryService.findById(courseDto.getCategoryId()));
        course.setInstructor(instructorService.findById(courseDto.getInstructorId()));
        courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public long countCourses() {
        return courseRepository.count();
    }
}