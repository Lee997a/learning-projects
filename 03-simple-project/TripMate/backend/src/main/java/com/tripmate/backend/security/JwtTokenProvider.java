package com.tripmate.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j // 로그 사용
@Component // Spring Bean으로 등록
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class JwtTokenProvider {

    // application.yml에서 값 주입
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    // 나중에 만들 예정
    private final UserDetailsService userDetailsService;

    // JWT 서명에 사용할 키
    private SecretKey key;

    /**
     * Bean 생성 후 초기화
     * secretKey를 SecretKey 객체로 변환
     *
     */
    @PostConstruct
    protected void init(){
        // 문자열 키를 SecretKey 객체로 변환
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        log.info("JWT 토큰 프로바이더 초기화 완료");
    }

    /**
     * JWT 토큰 생성
     *
     * @Param email 사용자 이메일 (subject에 저장)
     * @return 생성된 JWT 토큰 문자열
     */
    public String createToken(String email){
        //Claims : JWT에 담을 정보
        Claims claims = Jwts.claims().subject(email).build();

        // 현재 시간
        Date now = new Date();
        // 만료시간 (현재 시간 + 유효기간)
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        // JWT 생성
        String token = Jwts.builder()
                .claims(claims)                     // 사용자 정보
                .issuedAt(now)                      // 발급 시간
                .expiration(validity)               // 만료 시간
                .signWith(key, Jwts.SIG.HS256)      // 서명 (HS256 알고리즘)
                .compact();                         // 문자열로 반환
        log.debug("JWT 토큰 생성 완료 : email = {}", email);
        return token;
    }

    /**
     * JWT 토큰에서 인증 정보 추출
     *
     * @Param token JWT 토큰
     * @return Spring Security의 Authentication 객체
     */
    public Authentication getAuthentication(String token){
        // 토큰에서 사용자 이메일 추출
        String email = getUserEmail(token);

        // UserDetailsService로 사용자 정보 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Authentication 객체 생성 (Spring Security가 인식)
        return new UsernamePasswordAuthenticationToken(
                userDetails,            // principal(인증된 사용자)
                "",                     // credentials (비밀번호, 토큰 인증이므로 비워둠)
                userDetails.getAuthorities()    // 권환
        );
    }

    /**
     * JWT 토큰에서 사용자 이메일 추출
     *
     * @Param token JWT 토큰
     * @return 사용자 이메일
     */
    public String getUserEmail(String token){
        // 토큰 파싱 후 subject(이메일)반환
        return Jwts.parser()
                .verifyWith(key)            // 서명 검증
                .build()
                .parseSignedClaims(token)   // 토큰 파싱
                .getPayload()               // Claims 추출
                .getSubject();              // subject(이메일) 반환
    }

    /**
     * JWT 토큰 유효성 검증
     *
     * @Param token JWT 토큰
     * @return 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token){
        try {
            // 토큰 파싱 시도 (서명 검증 + 만료 시간 확인)
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            log.debug("JWT 토큰 검증 성공");
            return true;
        }catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }

    /**
     * HTTP 요청 헤더에서 토큰 추출
     *
     * Authorization 헤더 형식: "Bearer {token}"
     *
     * @param bearerToken Authorization 헤더 값
     * @return 토큰 문자열 (Bearer 제거)
     */
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // "Bearer " 제거하고 토큰만 반환
            return bearerToken.substring(7);
        }
        return null;
    }
}


/*
 * ===== JWT 토큰 구조 =====
 *
 * JWT는 3부분으로 구성됩니다:
 *
 * Header.Payload.Signature
 *
 * 예시:
 * eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjk...
 *
 * 1. Header (헤더)
 *    - 토큰 타입 (JWT)
 *    - 서명 알고리즘 (HS256)
 *    { "alg": "HS256", "typ": "JWT" }
 *
 * 2. Payload (페이로드)
 *    - 실제 데이터 (Claims)
 *    - subject: 사용자 이메일
 *    - iat: 발급 시간
 *    - exp: 만료 시간
 *    { "sub": "user@example.com", "iat": 1696161600, "exp": 1696766400 }
 *
 * 3. Signature (서명)
 *    - Header + Payload를 secret key로 서명
 *    - 토큰 위변조 방지
 *    HMACSHA256(
 *      base64UrlEncode(header) + "." + base64UrlEncode(payload),
 *      secret
 *    )
 *
 * ===== 왜 안전한가? =====
 *
 * 1. 서명 검증
 *    - secret key 없이는 유효한 토큰 생성 불가
 *    - 토큰이 위변조되면 서명이 맞지 않아 검증 실패
 *
 * 2. 만료 시간
 *    - 토큰에 유효기간이 있어 오래된 토큰은 무효화
 *
 * 3. HTTPS 사용
 *    - 토큰 전송 시 암호화 (중간에서 가로채기 방지)
 *
 * ===== 주의사항 =====
 *
 * 1. secret key는 절대 노출되면 안됨!
 *    - Git에 커밋 금지
 *    - 환경 변수로 관리
 *
 * 2. Payload에 민감한 정보 넣지 않기
 *    - JWT는 암호화가 아닌 인코딩
 *    - 누구나 디코딩해서 볼 수 있음
 *    - 비밀번호, 개인정보 넣지 말 것!
 *
 * 3. 토큰 만료 시간 적절히 설정
 *    - 너무 길면 보안 위험
 *    - 너무 짧으면 사용자 불편
 *    - 추천: Access Token 1-7일, Refresh Token 14-30일
 */