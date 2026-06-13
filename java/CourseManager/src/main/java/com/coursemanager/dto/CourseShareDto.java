package com.coursemanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseShareDto {
    private Long id;

    @NotNull(message = "Kurs jest wymagany")
    private Long courseId;

    private String courseTitle;
    private Long ownerId;
    private String ownerUsername;
    private Long targetUserId;
    private String targetUsername;

    @Size(max = 80, message = "Token może mieć maksymalnie 80 znaków")
    private String token;

    private boolean active;
    private LocalDateTime createdAt;
}
