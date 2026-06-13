package com.coursemanager.service;

import com.coursemanager.dto.EnrollmentDto;
import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.Course;
import com.coursemanager.model.entity.Enrollment;
import com.coursemanager.model.entity.User;
import com.coursemanager.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            throw new BusinessException("Nie udało się zapisać na kurs: " + extractDatabaseMessage(e));
        }
    }

    @Transactional
    public void cancelEnrollment(Long enrollmentId) {
        String sql = "CALL anuluj_zapis(?)";
        try {
            jdbcTemplate.update(sql, enrollmentId);
        } catch (Exception e) {
            throw new BusinessException("Nie udało się anulować zapisu: " + extractDatabaseMessage(e));
        }
    }

    @Transactional
    public Enrollment updateEnrollmentStatus(Long enrollmentId, String status) {
        if (!"AKTYWNY".equals(status) && !"ANULOWANY".equals(status)) {
            throw new BusinessException("Nieprawidłowy status zapisu");
        }
        Enrollment enrollment = findById(enrollmentId);
        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void cancelEnrollmentForUser(Long enrollmentId, User user) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BusinessException("Nie znaleziono zapisu"));
        boolean isOwner = enrollment.getUser() != null && enrollment.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() != null && user.getRole().equals("ROLE_ADMIN");
        if (!isOwner && !isAdmin) {
            throw new BusinessException("Nie możesz anulować cudzego zapisu");
        }
        cancelEnrollment(enrollmentId);
    }

    public int getAvailableSeats(Long courseId) {
        String sql = "SELECT liczba_wolnych_miejsc(?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, courseId);
    }

    public List<Enrollment> getUserEnrollments(User user) {
        return enrollmentRepository.findByUser(user);
    }

    public Page<Enrollment> getUserEnrollments(User user, Pageable pageable) {
        return enrollmentRepository.findByUser(user, pageable);
    }

    public List<Enrollment> getCourseParticipants(Course course) {
        return enrollmentRepository.findByCourse(course);
    }

    public List<Map<String, Object>> findParticipantsByCourseProcedure(Long courseId) {
        return jdbcTemplate.queryForList("SELECT user_id, username, to_char(enrollment_date, 'DD.MM.YYYY HH24:MI') AS enrollment_date FROM lista_uczestnikow_kursu(?)", courseId);
    }

    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Nie znaleziono zapisu"));
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public Page<Enrollment> findAll(Pageable pageable) {
        return enrollmentRepository.findAll(pageable);
    }

    public EnrollmentDto toDto(Enrollment enrollment) {
        return new EnrollmentDto(
                enrollment.getId(),
                enrollment.getUser() != null ? enrollment.getUser().getId() : null,
                enrollment.getUser() != null ? enrollment.getUser().getUsername() : null,
                enrollment.getCourse() != null ? enrollment.getCourse().getId() : null,
                enrollment.getCourse() != null ? enrollment.getCourse().getTitle() : null,
                enrollment.getStatus(),
                enrollment.getEnrollmentDate()
        );
    }

    public long countEnrollments() {
        return enrollmentRepository.count();
    }

    public List<Map<String, Object>> findMostPopularCourses() {
        String sql = """
            SELECT c.id as courseId, c.title, COUNT(e.id) as enrollmentCount
            FROM courses c
            LEFT JOIN enrollments e ON c.id = e.course_id AND e.status = 'AKTYWNY'
            GROUP BY c.id, c.title
            ORDER BY enrollmentCount DESC, c.title ASC
            LIMIT 10
        """;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> findCoursesWithFreeSeats() {
        String sql = """
            SELECT c.id as courseId,
                   c.title,
                   c.max_seats as maxSeats,
                   liczba_wolnych_miejsc(c.id) as availableSeats
            FROM courses c
            ORDER BY c.id ASC
        """;
        return jdbcTemplate.queryForList(sql);
    }

    private String extractDatabaseMessage(Exception e) {
        Throwable current = e;
        String message = e.getMessage();
        while (current != null) {
            if (current.getMessage() != null) {
                message = current.getMessage();
            }
            current = current.getCause();
        }
        if (message == null || message.isBlank()) {
            return "nieznany błąd bazy danych";
        }
        int errorIndex = message.indexOf("ERROR:");
        if (errorIndex >= 0) {
            message = message.substring(errorIndex + "ERROR:".length()).trim();
        }
        int whereIndex = message.indexOf("Where:");
        if (whereIndex >= 0) {
            message = message.substring(0, whereIndex).trim();
        }
        return message;
    }
}
