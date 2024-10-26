package com.skkuaicapston.DepressionChatBot.repository;

import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import com.skkuaicapston.DepressionChatBot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 특정 사용자와 날짜에 해당하는 채팅방을 찾는 메서드
    Optional<ChatRoom> findByCreatorAndDate(User creator, LocalDate date);
}
