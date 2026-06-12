package com.coursemanager.controller;

import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.CourseShare;
import com.coursemanager.model.entity.User;
import com.coursemanager.service.CourseService;
import com.coursemanager.service.CourseShareService;
import com.coursemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CourseShareController {
    private final CourseShareService courseShareService;
    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/shares")
    public String shares(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User currentUser = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("createdShares", courseShareService.findCreatedBy(currentUser));
        model.addAttribute("receivedShares", courseShareService.findSharedWith(currentUser));
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("users", userService.findAllUsers().stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .toList());
        return "shares";
    }

    @PostMapping("/shares/user")
    public String shareWithUser(@RequestParam Long courseId,
                                @RequestParam Long targetUserId,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        try {
            courseShareService.shareWithUser(courseId, currentUser.getId(), targetUserId);
            redirectAttributes.addFlashAttribute("success", "Udostępniono kurs wskazanemu użytkownikowi");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/shares";
    }

    @PostMapping("/shares/public")
    public String createPublicShare(@RequestParam Long courseId,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    RedirectAttributes redirectAttributes) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        try {
            CourseShare share = courseShareService.createPublicShare(courseId, currentUser.getId());
            redirectAttributes.addFlashAttribute("success", "Utworzono publiczny link: /public/share/" + share.getToken());
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/shares";
    }

    @PostMapping("/shares/delete/{id}")
    public String deactivate(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        try {
            courseShareService.deactivate(id, currentUser);
            redirectAttributes.addFlashAttribute("success", "Wyłączono udostępnienie");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/shares";
    }

    @GetMapping("/public/share/{token}")
    public String publicShare(@PathVariable String token, Model model) {
        CourseShare share = courseShareService.findByPublicToken(token);
        model.addAttribute("share", share);
        model.addAttribute("course", share.getCourse());
        return "public-share";
    }
}
