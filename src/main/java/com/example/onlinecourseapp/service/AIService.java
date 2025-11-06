package com.example.onlinecourseapp.service;

import com.example.onlinecourseapp.exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    @Value("${openai.api.key:}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateSummary(String text) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new BadRequestException("OpenAI API key is not configured. Please set OPENAI_API_KEY environment variable.");
        }

        try {
            Map<String, Object> requestPayload = new HashMap<>();
            requestPayload.put("model", "gpt-3.5-turbo");
            requestPayload.put("max_tokens", 150);
            requestPayload.put("temperature", 0.7);
            
            List<Map<String, String>> messages = new ArrayList<>();
            
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a helpful assistant that summarizes course descriptions concisely.");
            messages.add(systemMessage);
            
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", "Please provide a concise summary of the following course description: " + text);
            messages.add(userMessage);
            
            requestPayload.put("messages", messages);

            String requestBody = objectMapper.writeValueAsString(requestPayload);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPENAI_API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new BadRequestException("OpenAI API request failed with status: " + response.statusCode() + ", body: " + response.body());
            }

            JsonNode jsonResponse = objectMapper.readTree(response.body());
            return jsonResponse.get("choices").get(0).get("message").get("content").asText();

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Error calling OpenAI API: " + e.getMessage());
        }
    }
}
