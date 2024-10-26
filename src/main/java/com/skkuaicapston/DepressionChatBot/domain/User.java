package com.skkuaicapston.DepressionChatBot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@Table(name="user")
public class User {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username으로 회원가입 여부 판별. 유니크하게 설정하였음
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String realname;

    @Column(nullable = false)
    private String profileImageUrl = "https://cdn2.ppomppu.co.kr/zboard/data3/2022/0509/m_20220509173224_d9N4ZGtBVR.jpeg"; // 기본 프로필 이미지 URL

    @Column(nullable = false)
    private String bio = "Please enter your bio!"; // 기본 자기소개

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Summary> summaries;
}
