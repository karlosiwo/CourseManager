package com.coursemanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto {
    private Long id;

    @NotBlank(message = "Imię jest wymagane")
    @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźżA-ZĄĆĘŁŃÓŚŹŻ -]*$", message = "Imię powinno zaczynać się wielką literą i zawierać litery")
    private String firstName;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Size(min = 2, max = 50, message = "Nazwisko musi mieć od 2 do 50 znaków")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźżA-ZĄĆĘŁŃÓŚŹŻ -]*$", message = "Nazwisko powinno zaczynać się wielką literą i zawierać litery")
    private String lastName;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Podaj poprawny email")
    @Size(max = 100, message = "Email może mieć maksymalnie 100 znaków")
    private String email;

    @NotBlank(message = "Specjalizacja jest wymagana")
    @Size(min = 3, max = 100, message = "Specjalizacja musi mieć od 3 do 100 znaków")
    private String specialization;

    @Size(max = 1000, message = "Biogram może mieć maksymalnie 1000 znaków")
    private String bio;

    private LocalDateTime createdAt;
}
