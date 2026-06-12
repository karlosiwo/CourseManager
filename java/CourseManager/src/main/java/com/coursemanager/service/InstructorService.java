package com.coursemanager.service;

import com.coursemanager.dto.InstructorDto;
import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.Instructor;
import com.coursemanager.repository.InstructorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }

    public Instructor findById(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Nie znaleziono prowadzącego"));
    }

    public InstructorDto toDto(Instructor instructor) {
        return new InstructorDto(instructor.getId(), instructor.getFirstName(), instructor.getLastName());
    }

    public Instructor create(InstructorDto dto) {
        if (instructorRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(dto.getFirstName(), dto.getLastName())) {
            throw new BusinessException("Taki prowadzący już istnieje");
        }
        Instructor instructor = new Instructor();
        instructor.setFirstName(dto.getFirstName().trim());
        instructor.setLastName(dto.getLastName().trim());
        return instructorRepository.save(instructor);
    }

    public Instructor update(Long id, InstructorDto dto) {
        Instructor instructor = findById(id);
        instructorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(dto.getFirstName(), dto.getLastName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new BusinessException("Taki prowadzący już istnieje"); });
        instructor.setFirstName(dto.getFirstName().trim());
        instructor.setLastName(dto.getLastName().trim());
        return instructorRepository.save(instructor);
    }

    public void delete(Long id) {
        try {
            instructorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Nie można usunąć prowadzącego przypisanego do kursu");
        }
    }

    public Instructor save(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public long countInstructors() {
        return instructorRepository.count();
    }
}

