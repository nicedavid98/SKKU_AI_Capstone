package com.skkuaicapston.DepressionChatBot.service;

import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import com.skkuaicapston.DepressionChatBot.domain.User;
import com.skkuaicapston.DepressionChatBot.repository.ChatRoomRepository;
import com.skkuaicapston.DepressionChatBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    /** 채팅방 생성 **/
    public ChatRoom createChatRoom(Long userId, String title) {
        // 아이디를 활용해 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 오늘 날짜에 해당 유저가 이미 채팅방을 생성했는지 확인
        LocalDate today = LocalDate.now();
        boolean chatRoomExists = chatRoomRepository.findByCreatorAndDate(user, today).isPresent();

        // 하루에 하나의 채팅방만 생성할 수 있다는 비즈니스 로직
        if (chatRoomExists) {
            throw new RuntimeException("User has already created a chat room today.");
        }

        // 새로운 채팅방 생성
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle(title);
        chatRoom.setCreator(user);
        chatRoom.setDate(today);

        // 채팅방 저장
        return chatRoomRepository.save(chatRoom);
    }

    /** 채팅방 삭제 **/
    public void deleteChatRoom(Long chatRoomId) {
        // 채팅방이 존재하는지 확인
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        // 채팅방 삭제
        chatRoomRepository.delete(chatRoom);
    }
}
