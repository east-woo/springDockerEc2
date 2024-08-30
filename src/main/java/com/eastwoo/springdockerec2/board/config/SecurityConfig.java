package com.eastwoo.springdockerec2.board.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
     * Spring Security의 HTTP 보안 구성을 설정
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 보안 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호를 비활성화. API의 경우 CSRF 보호가 필요하지 않을 수 있음.
                .authorizeHttpRequests(authz -> authz
                        //.requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/","/signup","/login","/api/users/register", "/api/users/login").permitAll()  // 회원 가입 및 로그인 엔드포인트는 인증 없이 접근
                        .anyRequest().authenticated()  // 그 외 모든 요청은 인증이 필요
                )
                //.headers(headers-> headers.frameOptions(options -> options.sameOrigin()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // JWT 인증 필터를 인증 필터 앞에 추가
                .httpBasic(AbstractHttpConfigurer::disable);  // 기본 HTTP 인증을 비활성화

        return http.build();  // 설정을 빌드하여 반환
    }


    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

    /**
     * 비밀번호 암호화에 사용되는 PasswordEncoder 빈을 생성
     *
     * @return BCryptPasswordEncoder 객체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt 알고리즘을 사용하여 비밀번호를 암호화
    }

    /**
     * AuthenticationManager 빈을 생성
     *
     * @param authenticationConfiguration AuthenticationConfiguration 객체
     * @return AuthenticationManager 객체
     * @throws Exception 인증 매니저 생성 중 발생할 수 있는 예외
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // AuthenticationConfiguration에서 인증 매니저를 return
    }
}
