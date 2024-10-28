package com.skkuaicapston.DepressionChatBot.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="chatroom")
public class ChatRoom {
    @Id
    @Column(name="chatroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonManagedReference // 순환 참조 방지
    private User creator;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @JsonManagedReference  // 순환 참조 방지
    private List<Message> messages;
}
