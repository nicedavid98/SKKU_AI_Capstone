package com.skkuaicapston.DepressionChatBot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name="summary")
public class Summary {
    @Id
    @Column(name="summary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference  // 순환 참조 방지
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double depressionLevel = -1;  // Cannot determine(No Chats) : -1, 0 <= Depression Detected <= 0.002, 0.002 < Needs Attention <= 0.995, 0.995 < Good <= 1

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;
}