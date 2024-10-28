package com.skkuaicapston.DepressionChatBot.repository;

import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByCreatorId(Long userId); // userId를 기준으로 채팅방 목록 조회
}
