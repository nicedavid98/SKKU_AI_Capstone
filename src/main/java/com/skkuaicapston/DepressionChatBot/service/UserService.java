package com.skkuaicapston.DepressionChatBot.service;

import com.skkuaicapston.DepressionChatBot.domain.User;
import com.skkuaicapston.DepressionChatBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /** 회원가입 기능 **/
    public User registerUser(String username, String password, String realname, String bio) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with the same username already exists.");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRealname(realname);
        newUser.setBio(bio != null ? bio : "Please enter your bio!");
        newUser.setProfileImageUrl("/images/default_profile_image.jpeg"); // 기본 프로필 사진 URL

        return userRepository.save(newUser);
    }

    /** 로그인 기능 **/
    public User loginUser(String username, String password) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password.");
        }
        return user.get();
    }

    /** 프로필 정보 변경 기능 **/
    public User updateProfile(Long userId, String realname, String profileImageUrl, String bio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (realname != null && !realname.isEmpty()) user.setRealname(realname);
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) user.setProfileImageUrl(profileImageUrl);
        if (bio != null && !bio.isEmpty()) user.setBio(bio);

        return userRepository.save(user);
    }
}
