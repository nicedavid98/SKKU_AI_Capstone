package com.skkuaicapston.DepressionChatBot.repository;

import com.skkuaicapston.DepressionChatBot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);  // username으로 사용자 조회하는 메서드
}
