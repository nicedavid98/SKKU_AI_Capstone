package com.skkuaicapston.DepressionChatBot.repository;

import com.skkuaicapston.DepressionChatBot.domain.Summary;
import com.skkuaicapston.DepressionChatBot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
    Optional<Summary> findByUserAndDate(User user, LocalDate date);
}
