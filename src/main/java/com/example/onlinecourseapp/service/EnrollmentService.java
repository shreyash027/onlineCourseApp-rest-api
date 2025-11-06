package com.example.onlinecourseapp.service;

import com.example.onlinecourseapp.exception.BadRequestException;
import com.example.onlinecourseapp.exception.ResourceNotFoundException;
import com.example.onlinecourseapp.model.Course;
import com.example.onlinecourseapp.model.Enrollment;
import com.example.onlinecourseapp.model.User;
import com.example.onlinecourseapp.repository.CourseRepository;
import com.example.onlinecourseapp.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;

    public Enrollment enrollStudent(Long courseId, String email) {
        User student = userService.findUserByEmail(email);
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (enrollmentRepository.existsByStudentAndCourseId(student, courseId)) {
            throw new BadRequestException("Student is already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsByUser(Long userId) {
        User user = userService.findUserById(userId);
        return enrollmentRepository.findByStudent(user);
    }

    public List<Enrollment> getEnrollmentsByEmail(String email) {
        User user = userService.findUserByEmail(email);
        return enrollmentRepository.findByStudent(user);
    }
}
