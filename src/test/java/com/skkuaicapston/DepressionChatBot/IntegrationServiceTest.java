package com.skkuaicapston.DepressionChatBot;

import com.skkuaicapston.DepressionChatBot.domain.ChatRoom;
import com.skkuaicapston.DepressionChatBot.domain.Message;
import com.skkuaicapston.DepressionChatBot.domain.Summary;
import com.skkuaicapston.DepressionChatBot.domain.User;
import com.skkuaicapston.DepressionChatBot.service.ChatRoomService;
import com.skkuaicapston.DepressionChatBot.service.MessageService;
import com.skkuaicapston.DepressionChatBot.service.SummaryService;
import com.skkuaicapston.DepressionChatBot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@Transactional
//class IntegrationServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private ChatRoomService chatRoomService;
//
//    @Autowired
//    private MessageService messageService;
//
//    @Autowired
//    private SummaryService summaryService;
//
//    private User user;
//    private ChatRoom chatRoom;
//
//    @BeforeEach
//    void setup() {
//        // Step 1: 회원가입
//        user = userService.registerUser("testuser", "password123", "John Doe");
//
//        // Step 2: 로그인
//        User loginUser = userService.loginUser("testuser", "password123");
//        assertNotNull(loginUser);
//        assertEquals("testuser", loginUser.getUsername());
//    }
//
//    @Test
//    void testFullScenario() {
//        // Step 3: 채팅방 생성
//        chatRoom = chatRoomService.createChatRoom(user.getId(), "Today's Chat");
//        assertNotNull(chatRoom);
//        assertEquals("Today's Chat", chatRoom.getTitle());
//
//        // Step 4: 사용자 메시지 전송 (자동으로 챗봇의 응답이 생성됨)
//        Message userMessage = messageService.sendMessage(chatRoom.getId(), "USER", "I'm feeling sad today.");
//        assertNotNull(userMessage);
//        assertEquals("I'm feeling sad today.", userMessage.getContent());
//
//        // Step 5: 채팅방 내 메시지 확인 (사용자 메시지 + 봇 메시지)
//        List<Message> messages = messageService.getMessagesForChatRoom(chatRoom.getId());
//        assertEquals(2, messages.size());
//        assertEquals("I'm feeling sad today.", messages.get(0).getContent());
//        assertEquals("안녕하세용", messages.get(1).getContent());  // 자동으로 생성된 챗봇 응답 확인
//
//        // Step 6: 사용자의 요약 생성 (우울감 지수 및 대화 요약)
//        Summary summary = summaryService.saveDailySummary(user.getId());
//        assertNotNull(summary);
//        assertEquals(LocalDate.now(), summary.getDate());
//        assertTrue(summary.getDepressionLevel() > 0);
//        assertFalse(summary.getSummary().isEmpty());
//
//        // Step 7: 생성된 요약 조회
//        Summary retrievedSummary = summaryService.getDailySummary(user.getId(), LocalDate.now());
//        assertNotNull(retrievedSummary);
//        assertEquals(summary.getDepressionLevel(), retrievedSummary.getDepressionLevel());
//        assertEquals(summary.getSummary(), retrievedSummary.getSummary());
//    }
//
//    @Test
//    void testScenarioWithDuplicateChatRoomCreation() {
//        // Step 3: 첫 번째 채팅방 생성
//        chatRoom = chatRoomService.createChatRoom(user.getId(), "Today's Chat");
//        assertNotNull(chatRoom);
//
//        // Step 4: 동일한 날짜에 두 번째 채팅방 생성 시도 (예외 발생)
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            chatRoomService.createChatRoom(user.getId(), "Another Chat");
//        });
//        assertEquals("User has already created a chat room today.", exception.getMessage());
//    }
//}
