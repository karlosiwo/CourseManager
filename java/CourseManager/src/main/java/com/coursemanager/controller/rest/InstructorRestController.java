package com.coursemanager.controller.rest;

import com.coursemanager.dto.InstructorDto;
import com.coursemanager.model.entity.Instructor;
import com.coursemanager.service.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorRestController {
    private final InstructorService instructorService;

    @GetMapping
    public List<InstructorDto> list() {
        return instructorService.findAll().stream().map(instructorService::toDto).toList();
    }

    @GetMapping("/{id}")
    public InstructorDto get(@PathVariable Long id) {
        return instructorService.toDto(instructorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<InstructorDto> create(@Valid @RequestBody InstructorDto dto) {
        Instructor saved = instructorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(instructorService.toDto(saved));
    }

    @PutMapping("/{id}")
    public InstructorDto update(@PathVariable Long id, @Valid @RequestBody InstructorDto dto) {
        return instructorService.toDto(instructorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        instructorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
