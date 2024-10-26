package com.skkuaicapston.DepressionChatBot.service;

import com.skkuaicapston.DepressionChatBot.domain.Summary;
import com.skkuaicapston.DepressionChatBot.domain.User;
import com.skkuaicapston.DepressionChatBot.repository.SummaryRepository;
import com.skkuaicapston.DepressionChatBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    /** 사용자의 하루 요약을 저장 및 업데이트 **/
    public Summary saveDailySummary(Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 오늘 날짜에 해당하는 요약이 이미 존재하는지 확인 (이미 있어도 업데이트 시킴)
        LocalDate today = LocalDate.now();
        Summary summary = summaryRepository.findByUserAndDate(user, today)
                .orElseGet(() -> {
                    Summary newSummary = new Summary();
                    newSummary.setUser(user);
                    newSummary.setDate(today);
                    return newSummary;
                });

        // 오늘 사용자가 보낸 채팅 메시지들만 가져옴 (우울감 지수 계산용)
        List<String> userChats = messageService.getUserChatsForDate(userId, today);

        // 오늘 사용자가 챗봇과 주고받은 모든 대화 (전체 요약용)
        List<String> allChats = messageService.getAllChatsForDate(userId, today);

        // AI 모델을 통해 우울감 지수와 대화 요약 결과 추출
        double depressionLevel = calculateDepressionLevel(userChats);
        String conversationSummary = generateConversationSummary(allChats);

        // 요약 정보 업데이트
        summary.setDepressionLevel(depressionLevel);
        summary.setSummary(conversationSummary);

        // 요약 저장
        return summaryRepository.save(summary);
    }

    /** 사용자의 특정 날짜의 요약을 조회 **/
    public Summary getDailySummary(Long userId, LocalDate date) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 해당 날짜의 요약 조회
        return summaryRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new RuntimeException("Summary not found for the given date"));
    }

    /** AI 모델을 통해 우울감 지수 계산 **/
    private double calculateDepressionLevel(List<String> userChats) {
        // 추후 AI 모델과 연동하여 실제 우울감 지수를 계산하는 로직이 들어갈 부분
        return 1;
    }

    /** AI 모델을 통해 대화 요약 생성 **/
    private String generateConversationSummary(List<String> allChats) {
        // 추후 AI 모델과 연동하여 대화 내용을 요약하는 로직이 들어갈 부분
        return "요약 내용!!";
    }
}

