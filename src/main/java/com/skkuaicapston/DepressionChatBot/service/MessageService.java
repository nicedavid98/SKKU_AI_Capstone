package com.skkuaicapston.DepressionChatBot.service;

import com.skkuaicapston.DepressionChatBot.domain.Message;
import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import com.skkuaicapston.DepressionChatBot.repository.MessageRepository;
import com.skkuaicapston.DepressionChatBot.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    /** 특정 채팅방(chatRoomId)에 대한 모든 메시지 가져오기 **/
    public List<Message> getMessagesForChatRoom(Long chatRoomId) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        return messageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
    }

    /** 해당 날짜의 채팅 중 유저가 입력한 내용만 가져오는 메서드 **/
    public List<String> getUserChatsForDate(Long userId, LocalDate date) {
        // 사용자가 보낸 메시지 중 오늘 날짜에 해당하는 메시지 조회 (senderType이 "USER"인 메시지만)
        return messageRepository.findMessagesByUserIdAndDateAndSenderType(userId, date, "USER")
                .stream()
                .map(message -> message.getContent())  // 메시지 내용만 추출
                .collect(Collectors.toList());
    }

    /** 해당 날짜의 채팅내용(USER + BOT)을 가져오는 메서드 **/
    public List<String> getAllChatsForDate(Long userId, LocalDate date) {
        // 사용자가 오늘 날짜에 주고받은 모든 메시지 조회 (USER와 BOT 모두 포함)
        return messageRepository.findMessagesByUserIdAndDate(userId, date)
                .stream()
                .map(message -> message.getContent())  // 메시지 내용만 추출
                .collect(Collectors.toList());
    }


    /** 사용자의 메시지를 저장하고 챗봇의 응답을 생성하는 메서드 **/
    public Message sendMessage(Long chatRoomId, String senderType, String content) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        // 사용자의 메시지 저장
        Message userMessage = new Message();
        userMessage.setChatRoom(chatRoom);
        userMessage.setSenderType(senderType);  // "USER"
        userMessage.setContent(content);
        userMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(userMessage);

        // 챗봇 응답 생성 및 저장 (사용자 메시지를 기반으로 자동 생성)
        if ("USER".equals(senderType)) {
            String botResponse = generateBotResponse(content);  // 응답 생성

            // 챗봇의 메시지 저장
            Message botMessage = new Message();
            botMessage.setChatRoom(chatRoom);
            botMessage.setSenderType("BOT");  // "BOT"
            botMessage.setContent(botResponse);
            botMessage.setTimestamp(LocalDateTime.now());

            messageRepository.save(botMessage);
        }

        return userMessage;  // 사용자의 메시지를 반환
    }



    /** 챗봇 응답 생성, 추후 수정 예정 **/
    private String generateBotResponse(String userMessage) {
        // 간단한 규칙으로 응답 생성
        return "안녕하세용";
    }
}

