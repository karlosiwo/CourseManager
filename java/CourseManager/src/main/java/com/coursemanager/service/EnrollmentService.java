package com.coursemanager.service;

import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.Course;
import com.coursemanager.model.entity.Enrollment;
import com.coursemanager.model.entity.User;
import com.coursemanager.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final JdbcTemplate jdbcTemplate;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public void enrollUser(Long userId, Long courseId) {
        String sql = "CALL zapisz_na_kurs(?, ?)";
        try {
            jdbcTemplate.update(sql, userId, courseId);
        } catch (Exception e) {
            throw new BusinessException("Nie udało się zapisać na kurs: " + e.getMessage());
        }
    }

    @Transactional
    public void cancelEnrollment(Long enrollmentId) {
        String sql = "CALL anuluj_zapis(?)";
        try {
            jdbcTemplate.update(sql, enrollmentId);
        } catch (Exception e) {
            throw new BusinessException("Nie udało się anulować zapisu: " + e.getMessage());
        }
    }

    public int getAvailableSeats(Long courseId) {
        String sql = "SELECT liczba_wolnych_miejsc(?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, courseId);
    }

    public List<Enrollment> getUserEnrollments(User user) {
        return enrollmentRepository.findByUser(user);
    }

    public List<Enrollment> getCourseParticipants(Course course) {
        return enrollmentRepository.findByCourse(course);
    }

    public long countEnrollments() {
        return enrollmentRepository.count();
    }

    /**
     * Zwraca listę najpopularniejszych kursów (max 10) z liczbą zapisów.
     * Każdy element mapy zawiera: "courseId", "title", "enrollmentCount".
     */
    public List<Map<String, Object>> findMostPopularCourses() {
        String sql = """
            SELECT c.id as courseId, c.title, COUNT(e.id) as enrollmentCount
            FROM courses c
            LEFT JOIN enrollments e ON c.id = e.course_id
            GROUP BY c.id, c.title
            ORDER BY enrollmentCount DESC
            LIMIT 10
        """;
        return jdbcTemplate.queryForList(sql);
    }
}