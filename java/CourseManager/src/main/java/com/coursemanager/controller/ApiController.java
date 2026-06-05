package com.coursemanager.controller;

import com.coursemanager.dto.AvailabilityDto;
import com.coursemanager.service.CategoryService;
import com.coursemanager.service.EnrollmentService;
import com.coursemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;   // do sprawdzenia połączenia

    @Autowired
    private PasswordEncoder passwordEncoder; // do testowania hashowania

    @GetMapping("/categories/validate")
    public ResponseEntity<?> validateCategory(@RequestParam String name) {
        boolean exists = categoryService.existsByName(name);
        return ResponseEntity.ok(Map.of("valid", exists));
    }

    @GetMapping("/courses/{id}/availability")
    public ResponseEntity<AvailabilityDto> getAvailability(@PathVariable Long id) {
        int available = enrollmentService.getAvailableSeats(id);
        return ResponseEntity.ok(new AvailabilityDto(available));
    }

    // Endpoint testowy – liczba użytkowników w bazie
    @GetMapping("/db-status")
    public String dbStatus() {
        long count = userRepository.count();
        return "Liczba użytkowników w bazie: " + count;
    }

    // Endpoint testowy – generowanie hasha BCrypt dla podanego hasła
    @GetMapping("/test-hash")
    public String testHash(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        return "Hash dla '" + password + "': " + hash;
    }
}