package com.coursemanager.controller.rest;

import com.coursemanager.dto.EnrollmentDto;
import com.coursemanager.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentRestController {
    private final EnrollmentService enrollmentService;

    @GetMapping
    public Page<EnrollmentDto> list(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "enrollmentDate"));
        return enrollmentService.findAll(pageable).map(enrollmentService::toDto);
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

    @PutMapping("/{id}")
    public EnrollmentDto updateStatus(@PathVariable Long id, @RequestBody EnrollmentDto dto) {
        String status = dto.getStatus() != null ? dto.getStatus() : "AKTYWNY";
        return enrollmentService.toDto(enrollmentService.updateEnrollmentStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        enrollmentService.cancelEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
