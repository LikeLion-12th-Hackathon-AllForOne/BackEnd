package com.likelion.allForOne.config;

import com.likelion.allForOne.login.LoginServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginServiceImpl loginService;
    @Value("${jwt.secret.ACCESS_TOKEN_KEY}")
    private String accessTokenKey;
    @Value("${jwt.secret.REFRESH_TOKEN_KEY}")
    private String refreshTokenKey;

    private static final String[] AUTH_WHITELIST = {
            "/api/login/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
//        return httpSecurity
//                .httpBasic().disable()
//                .csrf().disable()
//                .cors().and()
//                .authorizeHttpRequests()
//                .requestMatchers("/api/login/**").permitAll()
//                .requestMatchers("/api/pw486/**").authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 사용하는 경우 사용
//                .and()
//                .addFilterBefore(new JwtFilter(loginService, accessTokenKey), UsernamePasswordAuthenticationFilter.class)
//                .build();
        //CSRF, CORS
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(Customizer.withDefaults());

        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        httpSecurity.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        //FormLogin, BasicHttp 비활성화
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);

        //JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        httpSecurity.addFilterBefore(new JwtFilter(loginService, accessTokenKey), UsernamePasswordAuthenticationFilter.class);

        // 권한 규칙 작성
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
        );

        return httpSecurity.build();
    }
}
