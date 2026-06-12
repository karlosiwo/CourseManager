package com.coursemanager.service;

import com.coursemanager.dto.CourseShareDto;
import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.Course;
import com.coursemanager.model.entity.CourseShare;
import com.coursemanager.model.entity.User;
import com.coursemanager.repository.CourseShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseShareService {
    private final CourseShareRepository courseShareRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final SecureRandom secureRandom = new SecureRandom();

    public List<CourseShare> findCreatedBy(User owner) {
        return courseShareRepository.findByOwnerOrderByCreatedAtDesc(owner);
    }

    public List<CourseShare> findSharedWith(User user) {
        return courseShareRepository.findByTargetUserAndActiveTrueOrderByCreatedAtDesc(user);
    }

    public CourseShare findByPublicToken(String token) {
        return courseShareRepository.findByTokenAndActiveTrue(token)
                .orElseThrow(() -> new BusinessException("Link udostępnienia jest nieaktywny albo nie istnieje"));
    }

    @Transactional
    public CourseShare shareWithUser(Long courseId, Long ownerId, Long targetUserId) {
        if (ownerId.equals(targetUserId)) {
            throw new BusinessException("Nie ma sensu udostępniać kursu samemu sobie");
        }
        Course course = courseService.findCourseById(courseId);
        User owner = userService.findUserById(ownerId);
        User targetUser = userService.findUserById(targetUserId);
        CourseShare share = CourseShare.builder()
                .course(course)
                .owner(owner)
                .targetUser(targetUser)
                .token(generateToken())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        return courseShareRepository.save(share);
    }

    @Transactional
    public CourseShare createPublicShare(Long courseId, Long ownerId) {
        Course course = courseService.findCourseById(courseId);
        User owner = userService.findUserById(ownerId);
        CourseShare share = CourseShare.builder()
                .course(course)
                .owner(owner)
                .targetUser(null)
                .token(generateToken())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        return courseShareRepository.save(share);
    }

    @Transactional
    public void deactivate(Long shareId, User currentUser) {
        CourseShare share = courseShareRepository.findById(shareId)
                .orElseThrow(() -> new BusinessException("Nie znaleziono udostępnienia"));
        boolean isOwner = share.getOwner() != null && share.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() != null && currentUser.getRole().equals("ROLE_ADMIN");
        if (!isOwner && !isAdmin) {
            throw new BusinessException("Nie możesz usunąć tego udostępnienia");
        }
        share.setActive(false);
        courseShareRepository.save(share);
    }

    public CourseShareDto toDto(CourseShare share) {
        return new CourseShareDto(
                share.getId(),
                share.getCourse() != null ? share.getCourse().getId() : null,
                share.getCourse() != null ? share.getCourse().getTitle() : null,
                share.getOwner() != null ? share.getOwner().getId() : null,
                share.getOwner() != null ? share.getOwner().getUsername() : null,
                share.getTargetUser() != null ? share.getTargetUser().getId() : null,
                share.getTargetUser() != null ? share.getTargetUser().getUsername() : null,
                share.getToken(),
                share.isActive(),
                share.getCreatedAt()
        );
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
