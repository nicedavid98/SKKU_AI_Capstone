package com.skkuaicapston.DepressionChatBot.controller;

import com.skkuaicapston.DepressionChatBot.domain.Summary;
import com.skkuaicapston.DepressionChatBot.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    /** 오늘 날짜의 요약을 저장/업데이트하는 엔드포인트 **/
    @PostMapping("/save")
    public ResponseEntity<Summary> saveDailySummary(@RequestParam Long userId) {
        Summary summary = summaryService.saveDailySummary(userId);
        return ResponseEntity.ok(summary);
    }

    /** 특정 날짜의 요약을 조회하는 엔드포인트 **/
    @GetMapping("/get")
    public ResponseEntity<Summary> getDailySummary(@RequestParam Long userId,
                                                   @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        Summary summary = summaryService.getDailySummary(userId, localDate);
        return ResponseEntity.ok(summary);
    }
}
