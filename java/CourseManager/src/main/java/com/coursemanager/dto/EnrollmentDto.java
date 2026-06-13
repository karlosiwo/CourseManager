package com.coursemanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    private Long id;

    @NotNull(message = "Użytkownik jest wymagany")
    private Long userId;

    private String username;

    @NotNull(message = "Kurs jest wymagany")
    private Long courseId;

    private String courseTitle;

    @Pattern(regexp = "AKTYWNY|ANULOWANY", message = "Status musi mieć wartość AKTYWNY albo ANULOWANY")
    private String status;

    private LocalDateTime enrollmentDate;
}
