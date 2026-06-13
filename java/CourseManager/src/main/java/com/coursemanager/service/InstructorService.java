package com.coursemanager.service;

import com.coursemanager.dto.InstructorDto;
import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.Instructor;
import com.coursemanager.repository.InstructorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Instructor> findAll(Pageable pageable) {
        return instructorRepository.findAll(pageable);
    }

    public Instructor findById(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Nie znaleziono prowadzącego"));
    }

    public InstructorDto toDto(Instructor instructor) {
        return new InstructorDto(
                instructor.getId(),
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getEmail(),
                instructor.getSpecialization(),
                instructor.getBio(),
                instructor.getCreatedAt()
        );
    }

    public Instructor create(InstructorDto dto) {
        validateUniqueness(dto, null);
        Instructor instructor = new Instructor();
        fillInstructor(instructor, dto);
        return instructorRepository.save(instructor);
    }

    public Instructor update(Long id, InstructorDto dto) {
        Instructor instructor = findById(id);
        validateUniqueness(dto, id);
        fillInstructor(instructor, dto);
        return instructorRepository.save(instructor);
    }

    private void validateUniqueness(InstructorDto dto, Long currentId) {
        instructorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(dto.getFirstName(), dto.getLastName())
                .filter(existing -> currentId == null || !existing.getId().equals(currentId))
                .ifPresent(existing -> { throw new BusinessException("Taki prowadzący już istnieje"); });
        instructorRepository.findByEmailIgnoreCase(dto.getEmail())
                .filter(existing -> currentId == null || !existing.getId().equals(currentId))
                .ifPresent(existing -> { throw new BusinessException("Prowadzący z takim adresem email już istnieje"); });
    }

    private void fillInstructor(Instructor instructor, InstructorDto dto) {
        instructor.setFirstName(dto.getFirstName().trim());
        instructor.setLastName(dto.getLastName().trim());
        instructor.setEmail(dto.getEmail().trim().toLowerCase());
        instructor.setSpecialization(dto.getSpecialization().trim());
        instructor.setBio(dto.getBio() != null ? dto.getBio().trim() : null);
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
