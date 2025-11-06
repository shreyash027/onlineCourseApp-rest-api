package com.example.onlinecourseapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AISummaryRequest {
    
    @NotBlank(message = "Text is required")
    private String text;
}
