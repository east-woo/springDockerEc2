package com.eastwoo.springdockerec2.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 보안 설정을 담당하는 클래스.
 * 이 클래스는 JWT 기반 인증을 설정하며, Spring Security의 HTTP 요청 및 인증을 구성합니다.
 *
 * @author : dongwoo
 * @fileName : SecurityConfig
 * @since : 2024-08-22
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 생성자 주입을 통해 JwtAuthenticationFilter를 주입받습니다.
     *
     * @param jwtAuthenticationFilter JWT 인증 필터
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Spring Security의 HTTP 보안 구성을 설정합니다.
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 보안 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호를 비활성화합니다. API의 경우 CSRF 보호가 필요하지 않을 수 있습니다.
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/users/register", "/api/login").permitAll()  // 회원 가입 및 로그인 엔드포인트는 인증 없이 접근할 수 있습니다.
                        .anyRequest().authenticated()  // 그 외 모든 요청은 인증이 필요합니다.
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // JWT 인증 필터를 인증 필터 앞에 추가합니다.
                .httpBasic(AbstractHttpConfigurer::disable);  // 기본 HTTP 인증을 비활성화합니다.

        return http.build();  // 설정을 빌드하여 반환합니다.
    }

    /**
     * 비밀번호 암호화에 사용되는 PasswordEncoder 빈을 생성합니다.
     *
     * @return BCryptPasswordEncoder 객체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt 알고리즘을 사용하여 비밀번호를 암호화합니다.
    }

    /**
     * AuthenticationManager 빈을 생성합니다.
     *
     * @param authenticationConfiguration AuthenticationConfiguration 객체
     * @return AuthenticationManager 객체
     * @throws Exception 인증 매니저 생성 중 발생할 수 있는 예외
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // AuthenticationConfiguration에서 인증 매니저를 가져옵니다.
    }
}
