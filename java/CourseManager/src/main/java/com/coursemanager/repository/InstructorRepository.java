package com.coursemanager.repository;

import com.coursemanager.model.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
    Optional<Instructor> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
