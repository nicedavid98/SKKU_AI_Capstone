package com.skkuaicapston.DepressionChatBot.service;

import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import com.skkuaicapston.DepressionChatBot.domain.User;
import com.skkuaicapston.DepressionChatBot.repository.ChatRoomRepository;
import com.skkuaicapston.DepressionChatBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    /** 채팅방 생성 **/
    public ChatRoom createChatRoom(Long userId, String title) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle(title);
        chatRoom.setCreator(user);
        chatRoom.setDate(LocalDate.now());

        return chatRoomRepository.save(chatRoom);
    }

    /** 특정 사용자의 채팅방 목록 조회 **/
    public List<ChatRoom> getChatRoomsByUserId(Long userId) {
        return chatRoomRepository.findByCreatorId(userId);
    }

    /** 채팅방 삭제 **/
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        chatRoomRepository.delete(chatRoom);
    }
}
