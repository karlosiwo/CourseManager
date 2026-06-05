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
    @Size(max = 100, message = "Tytuł może mieć maksymalnie 100 znaków")
    private String title;

    @Size(max = 2000, message = "Opis może mieć maksymalnie 2000 znaków")
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

    // Dodatkowe pola do wyświetlania w widoku (nie są mapowane w formularzu)
    private String categoryName;
    private String instructorName;
}