package com.coursemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    private Long id;
    private Long userId;
    private String username;
    private Long courseId;
    private String courseTitle;
    private String status;
    private LocalDateTime enrollmentDate;
}
