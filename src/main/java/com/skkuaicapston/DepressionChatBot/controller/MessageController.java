package com.skkuaicapston.DepressionChatBot.controller;

import com.skkuaicapston.DepressionChatBot.domain.Message;
import com.skkuaicapston.DepressionChatBot.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /** 특정 채팅방의 모든 메시지를 조회하는 엔드포인트 **/
    @GetMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<List<Message>> getMessagesForChatRoom(@PathVariable Long chatRoomId) {
        List<Message> messages = messageService.getMessagesForChatRoom(chatRoomId);
        return ResponseEntity.ok(messages);
    }

    /** 메시지 전송 엔드포인트 (사용자 메시지와 챗봇 응답) **/
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestParam Long chatRoomId,
                                               @RequestParam String senderType,
                                               @RequestParam String content) {
        Message sentMessage = messageService.sendMessage(chatRoomId, senderType, content);
        return ResponseEntity.ok(sentMessage);
    }
}
