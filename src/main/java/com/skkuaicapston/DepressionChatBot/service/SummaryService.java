package com.skkuaicapston.DepressionChatBot.service;

import com.skkuaicapston.DepressionChatBot.domain.Summary;
import com.skkuaicapston.DepressionChatBot.domain.User;
import com.skkuaicapston.DepressionChatBot.repository.SummaryRepository;
import com.skkuaicapston.DepressionChatBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final DepressionPredictService depressionPredictService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.key}")
    private String chatGptApiKey;

    private final String chatGptApiUrl = "https://api.openai.com/v1/chat/completions";

    /** 사용자의 특정 날짜 요약을 가져오거나 없으면 생성 **/
    public Summary getOrCreateDailySummary(Long userId, LocalDate date) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 해당 날짜의 기존 요약을 찾기
        Summary existingSummary = summaryRepository.findByUserAndDate(user, date).orElse(null);

        // 해당 날짜의 유저 채팅 내용 가져오기
        List<String> userChats = messageService.getUserChatsForDate(userId, date);
        List<String> allChats = messageService.getAllChatsForDate(userId, date);

        if (existingSummary != null) {
            // 오늘 날짜이면 요약 업데이트
            if (date.equals(LocalDate.now())) {
                return updateSummary(existingSummary, userChats, allChats);
            }
            return existingSummary;
        } else {
            // 기존 요약이 없으면 새로 생성
            return createSummary(user, date, userChats, allChats);
        }
    }

    /** 요약 생성 **/
    private Summary createSummary(User user, LocalDate date, List<String> userChats, List<String> allChats) {
        Summary summary = new Summary();
        summary.setUser(user);
        summary.setDate(date);

        if (userChats.isEmpty()) {
            // 날짜 형식을 지정하여 문자열에 포함
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(formatter);

            summary.setSummary("There is no chat history for " + formattedDate + ".");
            summary.setDepressionLevel(10);
        } else {
            summary.setDepressionLevel(calculateDepressionLevel(userChats));
            summary.setSummary(generateConversationSummary(allChats));
        }

        return summaryRepository.save(summary);
    }

    /** 요약 업데이트 **/
    private Summary updateSummary(Summary summary, List<String> userChats, List<String> allChats) {
        summary.setDepressionLevel(calculateDepressionLevel(userChats));
        summary.setSummary(generateConversationSummary(allChats));
        return summaryRepository.save(summary);
    }

    /** AI 모델을 통해 우울감 지수 계산 **/
    private double calculateDepressionLevel(List<String> userChats) {
        if (userChats.isEmpty()) {
            return -1; // 채팅 데이터가 없을 경우 -1 반환
        }

        // 각 채팅 데이터에 대해 우울감 지수를 계산
        double minDepressionScore = 1;
        for (String chat : userChats) {
            try {
                double depressionScore = depressionPredictService.getPrediction(chat);

                // 모든 채팅 순회 전에 확실한 우울증이 감지된 경우, 순회 중단
                if (depressionScore < 0.001) {
                    minDepressionScore =  depressionScore;
                    break;
                }

                // 가장 우울감 지수가 높은 채팅의 우울감 지수를 찾기
                if (depressionScore < minDepressionScore) {
                    minDepressionScore = depressionScore;
                }
            } catch (Exception e) {
                System.err.println("Error while processing chat: " + chat + " - " + e.getMessage());
            }
        }

        return minDepressionScore;
    }

    /** AI 모델을 통해 대화 요약 생성 **/
    public String generateConversationSummary(List<String> allChats) {
        try {
            // ChatGPT 요청 메시지 설정
            List<Map<String, String>> messages = new ArrayList<>();
//            String prompt = "Summarize the following conversation between a user and a chatbot. Do not print out your personal opinions other than the summary. Write in English within 150 characters.";
//            String prompt = "You are a bot that summarizes user conversations. Do not print out your personal opinions other than the summary.  Write in English within 150 characters.";
            String prompt = "You are a bot that summarizes the conversation between the user and the chatbot. Based on the conversation, summarize what the user experienced and what their emotional state was. Do not print out your personal opinions other than the summary.  Write in English within 150 characters.";
            messages.add(Map.of("role", "system", "content", prompt));

            // 대화 내용 추가
            for (String chat : allChats) {
                messages.add(Map.of("role", "user", "content", chat));
            }

            // ChatGPT API 호출
            return chatGptApiRequest(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return "요약을 생성하는 중 오류가 발생했습니다.";
        }
    }

    /** ChatGPT API와 통신하여 요약을 가져오는 메서드 **/
    private String chatGptApiRequest(List<Map<String, String>> messages) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + chatGptApiKey);
            headers.add("Content-Type", "application/json");

            Map<String, Object> body = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", messages
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(chatGptApiUrl, HttpMethod.POST, request, Map.class);

            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");
        } catch (Exception e) {
            e.printStackTrace();
            return "요약을 생성하는 중 오류가 발생했습니다.";
        }
    }
}
