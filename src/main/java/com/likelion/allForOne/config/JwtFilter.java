package com.likelion.allForOne.config;

import com.likelion.allForOne.login.LoginServiceImpl;
import com.likelion.allForOne.login.dto.UserDto;
import com.likelion.allForOne.login.utils.CustomUserDetails;
import com.likelion.allForOne.login.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final LoginServiceImpl loginService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 특정 url 필터링 제외
        if ("/api/login/sign".equals(request.getRequestURI())
                || "/api/user/join".equals(request.getRequestURI())
                || "/api/user/checkIdDuplicate".equals(request.getRequestURI())
                || request.getRequestURI().startsWith("/api/code")) {
            log.info("필터링에서 제외합니다.");
            filterChain.doFilter(request, response);
            return;
        }


        // 토큰 전달 여부 확인
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer")){
            log.error("잘못된 authorization 입니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "잘못된 authorization 입니다."); // 401 오류 응답 보내기
            return;
        }

        // 토큰 꺼내기
        String token = authorization.split(" ")[1];

        // 토큰 Expired 여부 확인
        try {
            if (JwtUtil.isExpired(token, secretKey)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
                return;
            }
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException occurred: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
            return;
        } catch (SignatureException e) {
            log.error("SignatureException occurred: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
            return;
        }

        // 1. 토큰에서 userSeq 꺼내기
        UserDto userDto = JwtUtil.getUserDto(token, secretKey);

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userSeq(userDto.getUserSeq())
                .userId(userDto.getUserId())
                .userName(userDto.getUserName())
                .userImg(userDto.getUserImg())
                .authorities(List.of(new SimpleGrantedAuthority("USER")))
                .build();

        // 2. 권한 부여하기
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, List.of(new SimpleGrantedAuthority("USER")));

        // 3. Detail 넣어주기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
