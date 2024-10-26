package com.skkuaicapston.DepressionChatBot.repository;

import com.skkuaicapston.DepressionChatBot.domain.Message;
import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // 특정 채팅방의 모든 메시지를 시간 순으로 조회
    List<Message> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);

    // 특정 사용자가 보낸 특정 날짜의 메시지들을 조회 (senderType 선택 가능)
    @Query("SELECT m FROM Message m WHERE m.chatRoom.creator.id = :userId AND DATE(m.timestamp) = :date AND m.senderType = :senderType")
    List<Message> findMessagesByUserIdAndDateAndSenderType(@Param("userId") Long userId,
                                                           @Param("date") LocalDate date,
                                                           @Param("senderType") String senderType);

    // 특정 사용자가 특정 날짜에 보낸 모든 메시지를 조회 (USER와 BOT 모두 포함)
    @Query("SELECT m FROM Message m WHERE m.chatRoom.creator.id = :userId AND DATE(m.timestamp) = :date")
    List<Message> findMessagesByUserIdAndDate(@Param("userId") Long userId,
                                              @Param("date") LocalDate date);
}
