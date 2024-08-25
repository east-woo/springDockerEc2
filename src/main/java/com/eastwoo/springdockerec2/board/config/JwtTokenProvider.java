package com.eastwoo.springdockerec2.board.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


/**
 * JWT 토큰을 생성, 파싱, 검증하는 역할을 수행하는 클래스입니다.
 * 이 클래스는 JWT 토큰의 생성, 인증, 유저 정보 추출 및 토큰의 유효성을 검증하는 메소드를 제공합니다.
 *
 * @author : dongwoo
 * @fileName : JwtTokenProvider
 * @since : 2024-08-24
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey; // JWT 서명에 사용할 비밀 키

    @Value("${jwt.expiration}")
    private long validityInMilliseconds; // JWT의 유효 기간 (밀리초 단위)

    private final UserDetailsService userDetailsService; // 사용자 세부 정보를 로드하는 서비스

    private Key key; // JWT 서명에 사용할 키 객체

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        // 비밀 키를 사용하여 JWT 서명에 사용할 Key 객체를 초기화
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(String username) {
        // JWT Claims 생성
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        // JWT 토큰 생성 및 반환
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        // JWT 토큰에서 사용자 이름을 추출하고 UserDetails를 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        // 인증 객체 생성
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        // JWT 토큰에서 사용자 이름을 추출
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        // 요청 헤더에서 JWT 토큰 추출
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 접두사를 제거하고 토큰 반환
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            // JWT 토큰의 유효성을 검증
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; // 유효한 토큰
        } catch (Exception e) {
            return false; // 유효하지 않은 토큰
        }
    }
}