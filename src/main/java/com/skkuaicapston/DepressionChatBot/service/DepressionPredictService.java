package com.skkuaicapston.DepressionChatBot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DepressionPredictService {

    private final RestTemplate restTemplate = new RestTemplate();

    public double getPrediction(String text) {
        // Flask API URL
        String flaskApiUrl = "http://localhost:5001/predict";

        try {
            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 데이터 생성
            Map<String, String> request = Map.of("text", text);

            // HTTP 엔티티 생성
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

            // Flask API 호출
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskApiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // JSON 응답 파싱
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);

            // `prediction` 필드에서 숫자 추출
            JsonNode predictionNode = root.path("prediction");
            if (predictionNode.isArray() && predictionNode.size() > 0) {
                JsonNode firstArray = predictionNode.get(0); // 첫 번째 배열
                if (firstArray.isArray() && firstArray.size() > 0) {
                    return firstArray.get(1).asDouble(); // 두 번째 숫자 반환
                }
            }

            throw new RuntimeException("Invalid response format: " + responseBody);

        } catch (Exception e) {
            // 예외 처리
            throw new RuntimeException("Error occurred while calling Flask API: " + e.getMessage());
        }
    }
}

