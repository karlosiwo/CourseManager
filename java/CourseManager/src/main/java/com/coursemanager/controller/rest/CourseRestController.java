package com.coursemanager.controller.rest;

import com.coursemanager.dto.CourseDto;
import com.coursemanager.mapper.CourseMapper;
import com.coursemanager.model.entity.Course;
import com.coursemanager.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseRestController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @GetMapping
    public Page<CourseDto> list(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size,
                                @RequestParam(defaultValue = "startDate") String sort,
                                @RequestParam(defaultValue = "asc") String direction) {
        String safeSort = courseService.normalizeSortField(sort);
        String safeDirection = courseService.normalizeDirection(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(safeDirection), safeSort));
        return courseService.findAllCourses(Specification.where(null), pageable).map(courseMapper::toDto);
    }

    @GetMapping("/{id}")
    public CourseDto get(@PathVariable Long id) {
        return courseMapper.toDto(courseService.findCourseById(id));
    }

    @PostMapping
    public ResponseEntity<CourseDto> create(@Valid @RequestBody CourseDto dto) {
        Course saved = courseService.saveCourse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public CourseDto update(@PathVariable Long id, @Valid @RequestBody CourseDto dto) {
        Course saved = courseService.updateCourse(id, dto);
        return courseMapper.toDto(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
