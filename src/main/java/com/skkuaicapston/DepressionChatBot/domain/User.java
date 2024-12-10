package com.skkuaicapston.DepressionChatBot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String realname;

    @Column(nullable = false)
    private String profileImageUrl = "/images/default_profile_image.jpeg";

    @Column(nullable = false)
    private String bio = "Please enter your bio!";

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Summary> summaries;

    /**
     * 비밀번호를 암호화하여 설정합니다.
     * Spring Security의 BCryptPasswordEncoder를 사용.
     * @param rawPassword 사용자 입력 비밀번호
     */
    public void setPassword(String rawPassword) {
        this.password = encryptPassword(rawPassword);
    }

    /**
     * 비밀번호를 암호화하는 메서드.
     * @param rawPassword 사용자 입력 비밀번호
     * @return 암호화된 비밀번호
     */
    private String encryptPassword(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(rawPassword);
    }
}
