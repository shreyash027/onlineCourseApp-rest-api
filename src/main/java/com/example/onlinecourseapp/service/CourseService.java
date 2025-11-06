package com.example.onlinecourseapp.service;

import com.example.onlinecourseapp.dto.CourseRequest;
import com.example.onlinecourseapp.exception.ForbiddenException;
import com.example.onlinecourseapp.exception.ResourceNotFoundException;
import com.example.onlinecourseapp.model.Course;
import com.example.onlinecourseapp.model.Role;
import com.example.onlinecourseapp.model.User;
import com.example.onlinecourseapp.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserService userService;

    public Course createCourse(CourseRequest request, String email) {
        User instructor = userService.findUserByEmail(email);
        
        if (instructor.getRole() != Role.INSTRUCTOR && instructor.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Only instructors or admins can create courses");
        }

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .instructor(instructor)
                .build();

        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseRequest request, String email) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        User user = userService.findUserByEmail(email);

        if (!course.getInstructor().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You don't have permission to update this course");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id, String email) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        User user = userService.findUserByEmail(email);

        if (!course.getInstructor().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You don't have permission to delete this course");
        }

        courseRepository.delete(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        User instructor = userService.findUserById(instructorId);
        return courseRepository.findByInstructor(instructor);
    }
}
