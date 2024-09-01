package com.eastwoo.springdockerec2.board.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터 클래스
 * 이 필터는 모든 요청에서 JWT 토큰을 검증하고, 유효한 경우 인증 정보를 SecurityContext에 설정
 *
 * @author : dongwoo
 * @fileName : JwtAuthenticationFilter
 * @since : 2024-08-24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰을 처리하는 서비스

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestedUrl = httpServletRequest.getRequestURL().toString();
        log.info("Requested URL: " + requestedUrl);



        // 요청에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken(httpServletRequest);




        // 토큰이 존재하고 유효한 경우 인증 처리
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 설정
        }

        // 다음 필터로 요청을 전달
        chain.doFilter(request, response);
    }

}

