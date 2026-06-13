package com.coursemanager.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    @NotBlank(message = "Login jest wymagany")
    @Size(min = 3, max = 50, message = "Login musi mieć od 3 do 50 znaków")
    @Pattern(regexp = "^[a-z0-9_.-]+$", message = "Login może zawierać małe litery, cyfry oraz znaki . _ -")
    private String username;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 5, max = 100, message = "Hasło musi mieć od 5 do 100 znaków")
    private String password;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Nieprawidłowy format email")
    @Size(max = 100, message = "Email może mieć maksymalnie 100 znaków")
    private String email;
}
