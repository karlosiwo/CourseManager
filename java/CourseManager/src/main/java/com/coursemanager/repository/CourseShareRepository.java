package com.coursemanager.repository;

import com.coursemanager.model.entity.CourseShare;
import com.coursemanager.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseShareRepository extends JpaRepository<CourseShare, Long> {
    List<CourseShare> findByOwnerOrderByCreatedAtDesc(User owner);
    List<CourseShare> findByTargetUserAndActiveTrueOrderByCreatedAtDesc(User targetUser);
    Optional<CourseShare> findByTokenAndActiveTrue(String token);
}
