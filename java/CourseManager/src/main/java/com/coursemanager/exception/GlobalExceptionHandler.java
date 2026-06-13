package com.coursemanager.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Object handleBusinessException(BusinessException ex, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request, Model model) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        if (isApiRequest(request)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Błąd walidacji danych",
                    "fields", fieldErrors
            ));
        }
        model.addAttribute("error", "Błąd walidacji danych: " + fieldErrors);
        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request, Model model) {
        String message = "Naruszenie więzów spójności bazy danych: " + ex.getMostSpecificCause().getMessage();
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", message));
        }
        model.addAttribute("error", message);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public Object handleGenericException(Exception ex, HttpServletRequest request, Model model) {
        String message = "Wystąpił błąd: " + ex.getMessage();
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", message));
        }
        model.addAttribute("error", message);
        return "error";
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return request != null && request.getRequestURI() != null && request.getRequestURI().startsWith("/api/");
    }
}
