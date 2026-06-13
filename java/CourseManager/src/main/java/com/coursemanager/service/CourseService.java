package com.coursemanager.service;

import com.coursemanager.dto.CourseDto;
import com.coursemanager.exception.BusinessException;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseService {
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "startDate", "title", "category.name");

    private final CourseRepository courseRepository;
    private final CategoryService categoryService;
    private final InstructorService instructorService;

    public Page<Course> findAllCourses(Specification<Course> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }

    public List<Course> findAll() {
        return courseRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Course> findAllCourses(String sortBy, String direction, String categoryName, LocalDate fromDate) {
        String safeSort = normalizeSortField(sortBy);
        Sort.Direction dir = direction != null && direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(dir, safeSort);

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

    public String normalizeSortField(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "id";
        }
        return ALLOWED_SORT_FIELDS.contains(sortBy) ? sortBy : "id";
    }

    public String normalizeDirection(String direction) {
        return direction != null && direction.equalsIgnoreCase("desc") ? "desc" : "asc";
    }

    public Course findCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new BusinessException("Nie znaleziono kursu"));
    }

    public Course saveCourse(CourseDto courseDto) {
        Course course = new Course();
        fillCourse(course, courseDto);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseDto courseDto) {
        Course course = findCourseById(id);
        fillCourse(course, courseDto);
        return courseRepository.save(course);
    }

    private void fillCourse(Course course, CourseDto courseDto) {
        course.setTitle(courseDto.getTitle().trim());
        course.setDescription(courseDto.getDescription());
        course.setStartDate(courseDto.getStartDate());
        course.setMaxSeats(courseDto.getMaxSeats());
        course.setCategory(categoryService.findById(courseDto.getCategoryId()));
        course.setInstructor(instructorService.findById(courseDto.getInstructorId()));
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public long countCourses() {
        return courseRepository.count();
    }

    public long countActiveCourses() {
        return courseRepository.countByStartDateGreaterThanEqual(LocalDate.now());
    }
}

