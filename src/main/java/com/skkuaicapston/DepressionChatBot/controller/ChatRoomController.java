package com.skkuaicapston.DepressionChatBot.controller;

import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import com.skkuaicapston.DepressionChatBot.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /** 채팅방 생성 엔드포인트 **/
    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam Long userId,
                                                   @RequestParam String title) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(userId, title);
        return ResponseEntity.ok(chatRoom);
    }

    /** 채팅방 삭제 엔드포인트 **/
    @DeleteMapping("/delete/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.ok().build();
    }
}
