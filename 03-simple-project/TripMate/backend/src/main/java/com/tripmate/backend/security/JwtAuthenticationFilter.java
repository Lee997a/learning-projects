package com.tripmate.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 *
 * 모든 HTTP 요청마다 실행되어 JWT 토큰을 검증합니다.
 *
 * OncePerRequestFilter:
 * - 요청당 한 번만 실행되는 필터
 * - 중복 실행 방지
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 필터 로직 구현
     *
     * 요청 → JWT 추출 → 검증 → SecurityContext에 인증 정보 저장 → 다음 필터
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // 1. 요청 헤더에서 JWT 토큰 추출
            String token = resolveToken(request);

            // 2. 토큰이 있고 유효한지 검증
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                // 3. 토큰에서 인증 정보 추출
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // 4. SecurityContext에 인증 정보 저장
                // → Spring Security가 이 정보를 사용해서 인증 확인
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("인증 정보 저장 완료: {}", authentication.getName());
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생: {}", e.getMessage());
            // 예외가 발생해도 요청은 계속 진행 (다른 필터에서 처리)
        }

        // 5. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰 추출
     *
     * Authorization 헤더 형식: "Bearer {token}"
     *
     * @param request HTTP 요청
     * @return JWT 토큰 (없으면 null)
     */
    private String resolveToken(HttpServletRequest request) {
        // Authorization 헤더 값 가져오기
        String bearerToken = request.getHeader("Authorization");

        // "Bearer "로 시작하는지 확인하고 토큰만 추출
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // "Bearer " 제거
        }

        return null;
    }
}

/*
 * ===== 필터 동작 과정 =====
 *
 * 1. 클라이언트 요청
 *    GET /api/trips
 *    Headers:
 *      Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
 *
 * 2. JwtAuthenticationFilter 실행
 *    - Authorization 헤더에서 토큰 추출
 *    - 토큰 검증 (서명, 만료 시간)
 *    - 토큰에서 이메일 추출
 *    - UserDetailsService로 사용자 정보 로드
 *    - Authentication 객체 생성
 *    - SecurityContext에 저장
 *
 * 3. 다음 필터로 전달
 *    - Spring Security가 SecurityContext 확인
 *    - 인증 정보 있으면 통과
 *    - 없으면 403 Forbidden
 *
 * 4. Controller 실행
 *    @GetMapping("/api/trips")
 *    public List<Trip> getTrips() {
 *        // 인증된 사용자만 여기까지 도달
 *        // SecurityContextHolder에서 현재 사용자 정보 가져올 수 있음
 *    }
 *
 * ===== SecurityContext란? =====
 *
 * 현재 요청을 처리하는 스레드의 인증 정보 저장소
 *
 * 인증 정보 저장:
 * SecurityContextHolder.getContext().setAuthentication(auth);
 *
 * 인증 정보 조회:
 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 * String email = auth.getName();  // 이메일
 *
 * Controller에서 사용:
 * @GetMapping("/profile")
 * public User getProfile() {
 *     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 *     String email = auth.getName();
 *     return userService.getUserByEmail(email);
 * }
 *
 * 또는 더 간단하게:
 * @GetMapping("/profile")
 * public User getProfile(@AuthenticationPrincipal UserDetails userDetails) {
 *     String email = userDetails.getUsername();
 *     return userService.getUserByEmail(email);
 * }
 *
 * ===== 왜 OncePerRequestFilter? =====
 *
 * 일반 Filter:
 * - 요청당 여러 번 실행될 수 있음
 * - Forward, Include 시 중복 실행
 *
 * OncePerRequestFilter:
 * - 요청당 딱 한 번만 실행 보장
 * - 성능 최적화
 * - 중복 인증 방지
 *
 * ===== 에러 처리 전략 =====
 *
 * 현재: try-catch로 예외를 잡고 로그만 남김
 * - 토큰이 없거나 잘못되어도 요청은 계속 진행
 * - 다음 필터(FilterSecurityInterceptor)에서 인증 체크
 * - 인증 안 되어 있으면 403 Forbidden 반환
 *
 * 장점:
 * - /api/auth/login 같은 공개 URL은 통과
 * - 보호된 URL만 401/403 반환
 *
 * 개선 가능:
 * - 커스텀 예외 핸들러로 더 자세한 에러 메시지
 * - 토큰 만료, 토큰 잘못됨 등 구분
 */