package com.tripmate.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인증 응답 DTO
 *
 * 로그인 / 회원가입 성공 시 반환되는 데이터
 * JWT 토큰과 사용자 정보를 함께 전달
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    /**
     * JWT 액세스 토큰
     *
     * 이 토큰을 이후 API 요청 시 Header에 포함 :
     * Authorization: Bearer {token}
     */
    private String token;

    /**
     * 사용자 정보
     *
     * 프론트엔드에서 바로 사용할 수 있도록 제공
     */
    private UserResponse user;
}

/*
 * ===== 인증 응답 구조 =====
 *
 * {
 *   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIi...",
 *   "user": {
 *     "id": 1,
 *     "email": "user@example.com",
 *     "name": "홍길동",
 *     "profileImage": null,
 *     "bio": null,
 *     "createdAt": "2025-10-01T19:00:00"
 *   }
 * }
 *
 * ===== 프론트엔드에서 사용 =====
 *
 * 1. 로그인 성공 시:
 * const response = await fetch('/api/auth/login', {
 *   method: 'POST',
 *   body: JSON.stringify({ email, password })
 * });
 *
 * const data = await response.json();
 *
 * 2. 토큰 저장 (localStorage 또는 쿠키):
 * localStorage.setItem('token', data.token);
 * localStorage.setItem('user', JSON.stringify(data.user));
 *
 * 3. 이후 API 요청 시:
 * fetch('/api/trips', {
 *   headers: {
 *     'Authorization': `Bearer ${localStorage.getItem('token')}`
 *   }
 * });
 *
 * ===== Refresh Token 패턴 (선택사항) =====
 *
 * 보안을 더 강화하려면 Access Token + Refresh Token 사용:
 *
 * {
 *   "accessToken": "...",   // 짧은 만료시간 (15분-1시간)
 *   "refreshToken": "...",  // 긴 만료시간 (7일-30일)
 *   "user": { ... }
 * }
 *
 * 흐름:
 * 1. Access Token으로 API 요청
 * 2. 만료되면 401 에러
 * 3. Refresh Token으로 새 Access Token 발급
 * 4. 새 Access Token으로 재요청
 *
 * 장점:
 * - Access Token 탈취되어도 짧은 시간만 유효
 * - Refresh Token은 안전하게 보관 (HttpOnly 쿠키)
 *
 * 단점:
 * - 구현 복잡도 증가
 * - Refresh Token 저장소 필요 (Redis 등)
 *
 * 현재 프로젝트는 Access Token만 사용 (단순함)
 * 나중에 필요하면 Refresh Token 추가 가능
 *
 * ===== 토큰 저장 위치 비교 =====
 *
 * 1. localStorage
 *    장점: 사용 쉬움, 만료 없음
 *    단점: XSS 공격에 취약
 *
 * 2. sessionStorage
 *    장점: 탭 닫으면 자동 삭제
 *    단점: XSS 공격에 취약
 *
 * 3. HttpOnly Cookie
 *    장점: XSS 공격 방어 (JS 접근 불가)
 *    단점: CSRF 공격 대비 필요
 *
 * 4. 메모리 (Redux, Context API)
 *    장점: XSS 공격 방어
 *    단점: 새로고침 시 날아감
 *
 * 추천: localStorage + XSS 방어 (input sanitization)
 */