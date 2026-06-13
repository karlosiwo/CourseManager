package com.coursemanager.controller;

import com.coursemanager.dto.AvailabilityDto;
import com.coursemanager.service.CategoryService;
import com.coursemanager.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/categories/validate")
    public ResponseEntity<?> validateCategory(@RequestParam String name,
                                              @RequestParam(required = false) Long currentId) {
        boolean available = categoryService.isNameAvailable(name, currentId);
        return ResponseEntity.ok(Map.of("available", available));
    }

    @GetMapping("/courses/{id}/availability")
    public ResponseEntity<AvailabilityDto> getAvailability(@PathVariable Long id) {
        int available = enrollmentService.getAvailableSeats(id);
        return ResponseEntity.ok(new AvailabilityDto(available));
    }
}
