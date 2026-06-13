package com.coursemanager.controller.rest;

import com.coursemanager.dto.CourseShareDto;
import com.coursemanager.model.entity.User;
import com.coursemanager.service.CourseShareService;
import com.coursemanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shares")
@RequiredArgsConstructor
public class CourseShareRestController {
    private final CourseShareService courseShareService;
    private final UserService userService;

    @GetMapping
    public Map<String, List<CourseShareDto>> myShares(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        return Map.of(
                "created", courseShareService.findCreatedBy(currentUser).stream().map(courseShareService::toDto).toList(),
                "received", courseShareService.findSharedWith(currentUser).stream().map(courseShareService::toDto).toList()
        );
    }

    @GetMapping("/{id}")
    public CourseShareDto get(@PathVariable Long id) {
        return courseShareService.toDto(courseShareService.findById(id));
    }

    @PostMapping("/user")
    public ResponseEntity<CourseShareDto> shareWithUser(@Valid @RequestBody CourseShareDto dto,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        var saved = courseShareService.shareWithUser(dto.getCourseId(), currentUser.getId(), dto.getTargetUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseShareService.toDto(saved));
    }

    @PostMapping("/public")
    public ResponseEntity<CourseShareDto> createPublicShare(@Valid @RequestBody CourseShareDto dto,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        var saved = courseShareService.createPublicShare(dto.getCourseId(), currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseShareService.toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        courseShareService.deactivate(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
