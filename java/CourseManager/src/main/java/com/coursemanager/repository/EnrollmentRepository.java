package com.coursemanager.repository;

import com.coursemanager.model.entity.Enrollment;
import com.coursemanager.model.entity.User;
import com.coursemanager.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUser(User user);
    Page<Enrollment> findByUser(User user, Pageable pageable);
    List<Enrollment> findByCourse(Course course);
    Optional<Enrollment> findByUserAndCourseAndStatus(User user, Course course, String status);
    long countByCourseAndStatus(Course course, String status);
}
