package com.coursemanager.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;

    @NotBlank(message = "Tytuł jest wymagany")
    @Size(max = 100)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull(message = "Data rozpoczęcia jest wymagana")
    @FutureOrPresent(message = "Data musi być dzisiejsza lub przyszła")
    private LocalDate startDate;

    @NotNull(message = "Maksymalna liczba miejsc jest wymagana")
    @Min(1)
    @Max(200)
    private Integer maxSeats;

    @NotNull(message = "Wybierz kategorię")
    private Long categoryId;

    @NotNull(message = "Wybierz prowadzącego")
    private Long instructorId;
}