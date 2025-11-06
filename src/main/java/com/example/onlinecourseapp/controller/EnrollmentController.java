package com.example.onlinecourseapp.controller;

import com.example.onlinecourseapp.dto.EnrollmentRequest;
import com.example.onlinecourseapp.exception.ForbiddenException;
import com.example.onlinecourseapp.model.Enrollment;
import com.example.onlinecourseapp.model.Role;
import com.example.onlinecourseapp.model.User;
import com.example.onlinecourseapp.service.EnrollmentService;
import com.example.onlinecourseapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;

    @PostMapping("/enroll")
    public ResponseEntity<Enrollment> enrollStudent(
            @Valid @RequestBody EnrollmentRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        Enrollment enrollment = enrollmentService.enrollStudent(request.getCourseId(), email);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    @GetMapping("/enrollments/{userId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByUser(
            @PathVariable Long userId,
            Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userService.findUserByEmail(email);
        
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You don't have permission to view other users' enrollments");
        }
        
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(userId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/enrollments")
    public ResponseEntity<List<Enrollment>> getMyEnrollments(Authentication authentication) {
        String email = authentication.getName();
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByEmail(email);
        return ResponseEntity.ok(enrollments);
    }
}
