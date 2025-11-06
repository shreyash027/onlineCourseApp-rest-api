package com.example.onlinecourseapp.controller;

import com.example.onlinecourseapp.dto.AISummaryRequest;
import com.example.onlinecourseapp.dto.AISummaryResponse;
import com.example.onlinecourseapp.service.AIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/summary")
    public ResponseEntity<AISummaryResponse> generateSummary(@Valid @RequestBody AISummaryRequest request) {
        String summary = aiService.generateSummary(request.getText());
        AISummaryResponse response = AISummaryResponse.builder()
                .summary(summary)
                .build();
        return ResponseEntity.ok(response);
    }
}
