package com.coursemanager.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-z0-9_.-]+$", message = "Login powinien zawierać małe litery, cyfry oraz znaki . _ -")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank
    @Size(min = 5, max = 255)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank
    @Pattern(regexp = "ROLE_ADMIN|ROLE_FULL_USER|ROLE_LIMITED_USER")
    @Column(nullable = false, length = 30)
    private String role;
}
