package com.skkuaicapston.DepressionChatBot.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name="message")
public class Message {
    @Id
    @Column(name="message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    @JsonBackReference  // 순환 참조 방지를 위한 어노테이션 추가
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String senderType;  // "USER" or "BOT"

    @Column(nullable = false, columnDefinition = "TEXT")  // DB에 저장하려는 메시지의 content 가 설정된 컬럼 길이보다 길어서 발생한 오류 해결
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
