package com.skkuaicapston.DepressionChatBot.controller;

import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import com.skkuaicapston.DepressionChatBot.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 생성 엔드포인트
     **/
    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam Long userId,
                                                   @RequestParam String title) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(userId, title);
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 특정 사용자의 채팅방 목록 조회 엔드포인트
     **/
    @GetMapping
    public ResponseEntity<List<ChatRoom>> getChatRoomsByUser(@RequestParam Long userId) {
        List<ChatRoom> chatRooms = chatRoomService.getChatRoomsByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }

    /**
     * 채팅방 삭제 엔드포인트
     **/
    @DeleteMapping("/delete/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.ok().build();
    }
}