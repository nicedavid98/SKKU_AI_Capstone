package com.skkuaicapston.DepressionChatBot.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 토큰 생성 및 검증 유틸리티 클래스.
 */
@Component
public class JwtTokenUtil {

    // 시크릿 키를 환경 변수에서 가져옴
    @Value("${jwt.secret}")
    private static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");

    /**
     * JWT 토큰을 생성합니다.
     * @param username 사용자 이름
     * @return 생성된 JWT 토큰
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1시간
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact(); // JWT 토큰 생성
    }

    /**
     * 토큰에서 사용자 이름을 추출합니다.
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public static String extractUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * JWT 토큰이 유효한지 검증합니다.
     * @param token 클라이언트에서 전달된 JWT 토큰
     * @return 토큰 유효 여부
     */
    public static boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 토큰에서 Claims 정보를 추출합니다.
     * @param token JWT 토큰
     * @return Claims 정보
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
