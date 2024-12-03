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
    private double depressionLevel;  // -1 <= Good < -0.9, -0.9 <= Moderate < 0, 0 <= needs attention < 0.9, 0.9 <= severe <= 1, no chat == 10

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;
}