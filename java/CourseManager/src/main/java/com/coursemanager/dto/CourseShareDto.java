package com.coursemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseShareDto {
    private Long id;
    private Long courseId;
    private String courseTitle;
    private Long ownerId;
    private String ownerUsername;
    private Long targetUserId;
    private String targetUsername;
    private String token;
    private boolean active;
    private LocalDateTime createdAt;
}
