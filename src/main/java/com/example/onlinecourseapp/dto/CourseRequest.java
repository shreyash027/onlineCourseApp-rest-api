package com.example.onlinecourseapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private String category;
}
