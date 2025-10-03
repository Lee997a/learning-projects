package com.tripmate.backend.config;

import com.tripmate.backend.security.JwtAuthenticationFilter;
import com.tripmate.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security 설정
 *
 * 주요 설정:
 * 1. 인증이 필요한 URL과 불필요한 URL 구분
 * 2. JWT 필터 등록
 * 3. 세션 사용 안 함 (Stateless)
 * 4. CORS 설정
 * 5. 비밀번호 암호화 방식
 */
@Configuration  // Spring 설정 클래스
@EnableWebSecurity  // Spring Security 활성화
@EnableMethodSecurity  // @PreAuthorize 등 메서드 보안 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 비밀번호 암호화 방식 설정
     *
     * BCrypt: 단방향 해시 함수
     * - 같은 비밀번호라도 매번 다른 해시값 생성 (Salt 사용)
     * - 복호화 불가능 (비밀번호를 알아낼 수 없음)
     * - 느린 속도로 Brute Force 공격 방어
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security 필터 체인 설정
     *
     * 이 메서드가 Spring Security의 핵심!
     *
     * @param http HttpSecurity 설정 객체
     * @return SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (JWT 사용 시 불필요)
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 설정 활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 세션 사용 안 함 (JWT는 Stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // URL별 인증 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증 불필요 (누구나 접근 가능)
                        .requestMatchers(
                                "/auth/**",              // 회원가입, 로그인 (context-path 제외)
                                "/public/**",            // 공개 API
                                "/h2-console/**",        // H2 콘솔 (개발용)
                                "/error"                 // 에러 페이지
                        ).permitAll()

                        // 나머지는 모두 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 필터 추가
                .addFilterBefore(
                        jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )

                // H2 콘솔 사용을 위한 설정 (개발용)
                .headers(headers ->
                        headers.frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }

    /**
     * JWT 인증 필터 빈 등록
     *
     * @return JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    /**
     * CORS 설정
     *
     * Cross-Origin Resource Sharing
     * - 프론트엔드(localhost:3000)와 백엔드(localhost:8080)가 다른 포트
     * - 브라우저 보안 정책상 기본적으로 차단됨
     * - CORS 설정으로 허용
     *
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 Origin (프론트엔드 주소)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",      // 개발 환경 (Next.js)
                "http://localhost:5173",      // 개발 환경 (Vite)
                "https://yourdomain.com"      // 운영 환경 (나중에 실제 도메인으로 변경)
        ));

        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // 허용할 헤더
        configuration.setAllowedHeaders(List.of("*"));

        // 인증 정보 포함 허용 (쿠키, Authorization 헤더 등)
        configuration.setAllowCredentials(true);

        // Preflight 요청 캐시 시간 (초)
        configuration.setMaxAge(3600L);

        // 모든 경로에 CORS 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

/*
 * ===== CSRF란? =====
 *
 * Cross-Site Request Forgery (사이트 간 요청 위조)
 *
 * 공격 시나리오:
 * 1. 사용자가 은행 사이트에 로그인 (세션 생성)
 * 2. 공격자가 만든 악성 사이트 방문
 * 3. 악성 사이트에서 은행 API 호출 (사용자 세션 사용)
 * 4. 사용자 모르게 계좌이체 발생!
 *
 * 방어:
 * - CSRF 토큰: 폼 제출 시 토큰 검증
 * - JWT는 Authorization 헤더 사용 → CSRF 공격 불가능
 *
 * 따라서 JWT 사용 시 CSRF 비활성화 가능!
 *
 * ===== Stateless란? =====
 *
 * 서버가 클라이언트 상태를 저장하지 않음
 *
 * Stateful (세션):
 * - 서버 메모리에 세션 저장
 * - 서버 여러 대면 세션 공유 필요
 * - 확장성 떨어짐
 *
 * Stateless (JWT):
 * - 서버는 토큰만 검증
 * - 세션 저장소 불필요
 * - 수평 확장 쉬움
 *
 * ===== 필터 체인 순서 =====
 *
 * HTTP 요청
 *   ↓
 * 1. CorsFilter (CORS 처리)
 *   ↓
 * 2. JwtAuthenticationFilter (JWT 검증) ← 우리가 추가
 *   ↓
 * 3. UsernamePasswordAuthenticationFilter (기본 인증)
 *   ↓
 * 4. FilterSecurityInterceptor (권한 체크)
 *   ↓
 * Controller
 *
 * addFilterBefore로 JWT 필터를 앞에 추가!
 *
 * ===== URL 패턴 매칭 =====
 *
 * /api/auth/**
 * - /api/auth/login ✅
 * - /api/auth/signup ✅
 * - /api/auth/user/profile ✅
 *
 * /api/trips/*
 * - /api/trips/1 ✅
 * - /api/trips/1/schedules ❌ (한 단계만)
 *
 * /api/trips/**
 * - /api/trips/1 ✅
 * - /api/trips/1/schedules ✅ (모든 하위 경로)
 *
 * ===== 개발/운영 환경 분리 =====
 *
 * 현재는 모든 설정이 하나에 있지만,
 * 나중에 application-dev.yml, application-prod.yml로 분리:
 *
 * 개발:
 * - H2 콘솔 활성화
 * - 상세 로그
 * - CORS 느슨하게
 *
 * 운영:
 * - H2 콘솔 비활성화
 * - 에러 로그만
 * - CORS 엄격하게
 */