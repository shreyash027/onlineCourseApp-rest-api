package com.example.onlinecourseapp.repository;

import com.example.onlinecourseapp.model.Enrollment;
import com.example.onlinecourseapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(User student);
    boolean existsByStudentAndCourseId(User student, Long courseId);
}
