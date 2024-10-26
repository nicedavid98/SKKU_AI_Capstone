package com.skkuaicapston.DepressionChatBot.controller;

import com.skkuaicapston.DepressionChatBot.domain.User;
import com.skkuaicapston.DepressionChatBot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 회원가입 엔드포인트 **/
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestParam String username,
                                             @RequestParam String password,
                                             @RequestParam String realname,
                                             @RequestParam(required = false) String bio) {
        User registeredUser = userService.registerUser(username, password, realname, bio);
        return ResponseEntity.ok(registeredUser);
    }

    /** 로그인 엔드포인트 **/
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String username,
                                          @RequestParam String password) {
        User loggedInUser = userService.loginUser(username, password);
        return ResponseEntity.ok(loggedInUser);
    }

    /** 프로필 업데이트 엔드포인트 **/
    @PutMapping("/{userId}/update-profile")
    public ResponseEntity<User> updateProfile(@PathVariable Long userId,
                                              @RequestParam(required = false) String realname,
                                              @RequestParam(required = false) String profileImageUrl,
                                              @RequestParam(required = false) String bio) {
        User updatedUser = userService.updateProfile(userId, realname, profileImageUrl, bio);
        return ResponseEntity.ok(updatedUser);
    }
}
