package com.skkuaicapston.DepressionChatBot.service;

import com.skkuaicapston.DepressionChatBot.domain.Message;
import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import com.skkuaicapston.DepressionChatBot.repository.MessageRepository;
import com.skkuaicapston.DepressionChatBot.repository.ChatRoomRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    // application.properties에서 API 키를 가져옵니다
    @Value("${openai.api.key}")
    private String chatGptApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String chatGptApiUrl = "https://api.openai.com/v1/chat/completions";

    /** 특정 채팅방(chatRoomId)에 대한 모든 메시지 가져오기 **/
    public List<Message> getMessagesForChatRoom(Long chatRoomId) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        return messageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
    }

    /** 해당 날짜의 채팅 중 유저가 입력한 내용만 가져오는 메서드 **/
    public List<String> getUserChatsForDate(Long userId, LocalDate date) {
        return messageRepository.findMessagesByUserIdAndDateAndSenderType(userId, date, "USER")
                .stream()
                .map(message -> message.getContent())
                .collect(Collectors.toList());
    }

    /** 해당 날짜의 채팅내용(USER + BOT)을 가져오는 메서드 **/
    public List<String> getAllChatsForDate(Long userId, LocalDate date) {
        return messageRepository.findMessagesByUserIdAndDate(userId, date)
                .stream()
                .map(message -> message.getContent())
                .collect(Collectors.toList());
    }

    /** 사용자의 메시지를 저장하고 챗봇의 응답을 생성하는 메서드 **/
    public List<Message> sendMessage(Long chatRoomId, String senderType, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        // 사용자의 메시지 생성 및 저장
        Message userMessage = new Message();
        userMessage.setChatRoom(chatRoom);
        userMessage.setSenderType(senderType);
        userMessage.setContent(content);
        userMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(userMessage);

        // 챗봇의 응답 생성 및 저장
        Message botMessage = null;
        if ("USER".equals(senderType)) {
            String botResponse = generateBotResponse(content, chatRoom);

            botMessage = new Message();
            botMessage.setChatRoom(chatRoom);
            botMessage.setSenderType("BOT");
            botMessage.setContent(botResponse);
            botMessage.setTimestamp(LocalDateTime.now());
            messageRepository.save(botMessage);
        }

        return Arrays.asList(userMessage, botMessage);
    }

    /** ChatGPT API를 통한 챗봇 응답 생성 **/
    private String generateBotResponse(String userMessage, ChatRoom chatRoom) {
        List<Message> previousMessages = messageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);

        // ChatGPT API 요청 메시지 구성
        List<Map<String, String>> messages = new ArrayList<>();

        // ChatGPT 응답에 대한 프롬포트 설정
        String prompt = "You are a chatbot that leads natural conversations like a real close friend. However, you should talk to people who appear to have depression in a more empathetic manner and, in rare cases, provide appropriate medical solutions depending on the severity of the depression. And don't try to chat for too long. It's okay to give a short, friendly answer.";
        messages.add(Map.of("role", "system", "content", prompt));

        // 이전 대화 내용 추가
        for (Message message : previousMessages) {
            messages.add(Map.of("role", message.getSenderType().equals("USER") ? "user" : "assistant", "content", message.getContent()));
        }
        messages.add(Map.of("role", "user", "content", userMessage));

        // ChatGPT API 호출 및 응답 반환
        return chatGptApiRequest(messages);
    }

    /** ChatGPT API와 통신하여 응답을 가져오는 메서드 **/
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

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, an error occurred while processing your response.";
        }
    }
}

