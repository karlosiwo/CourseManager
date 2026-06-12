package com.coursemanager.controller.rest;

import com.coursemanager.dto.EnrollmentDto;
import com.coursemanager.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentRestController {
    private final EnrollmentService enrollmentService;

    @GetMapping
    public List<EnrollmentDto> list() {
        return enrollmentService.findAll().stream().map(enrollmentService::toDto).toList();
    }

    @GetMapping("/{id}")
    public EnrollmentDto get(@PathVariable Long id) {
        return enrollmentService.toDto(enrollmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Map<String, Long> body) {
        enrollmentService.enrollUser(body.get("userId"), body.get("courseId"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        enrollmentService.cancelEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
