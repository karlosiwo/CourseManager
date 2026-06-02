package com.coursemanager.service;

import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.Course;
import com.coursemanager.model.entity.Enrollment;
import com.coursemanager.model.entity.User;
import com.coursemanager.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

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
}