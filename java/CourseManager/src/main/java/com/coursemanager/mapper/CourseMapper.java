package com.coursemanager.mapper;

import com.coursemanager.dto.CourseDto;
import com.coursemanager.model.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDto toDto(Course course) {
        if (course == null) return null;
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setStartDate(course.getStartDate());
        dto.setMaxSeats(course.getMaxSeats());
        // Zakładając, że Category ma pole 'name'
        if (course.getCategory() != null) {
            dto.setCategoryId(course.getCategory().getId());
            dto.setCategoryName(course.getCategory().getName());
        }
        if (course.getInstructor() != null) {
            dto.setInstructorId(course.getInstructor().getId());
            dto.setInstructorName(course.getInstructor().getName());
        }
        return dto;
    }

    public Course toEntity(CourseDto dto) {
        if (dto == null) return null;
        Course course = new Course();
        course.setId(dto.getId());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setStartDate(dto.getStartDate());
        course.setMaxSeats(dto.getMaxSeats());
        // Uwaga: kategoria i prowadzący muszą być ustawione osobno (np. przez serwis)
        return course;
    }
}