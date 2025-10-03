package com.tripmate.backend.service;

import com.tripmate.backend.dto.request.LoginRequest;
import com.tripmate.backend.dto.request.SignupRequest;
import com.tripmate.backend.dto.response.AuthResponse;
import com.tripmate.backend.dto.response.UserResponse;
import com.tripmate.backend.entity.User;
import com.tripmate.backend.repository.UserRepository;
import com.tripmate.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스
 *
 * 회원가입과 로그인 비즈니스 로직을 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     *
     * @param request 회원가입 요청 데이터
     * @return 인증 응답 (토큰 + 사용자 정보)
     * @throws IllegalArgumentException 이메일 중복 시
     */
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        log.info("회원가입 시도: email={}", request.getEmail());

        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("이미 존재하는 이메일: {}", request.getEmail());
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        log.debug("비밀번호 암호화 완료");

        // 3. User 엔티티 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .isActive(true)  // 기본값이지만 명시적으로
                .build();

        // 4. DB 저장
        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: userId={}, email={}", savedUser.getId(), savedUser.getEmail());

        // 5. JWT 토큰 생성
        String token = jwtTokenProvider.createToken(savedUser.getEmail());

        // 6. 응답 생성
        return AuthResponse.builder()
                .token(token)
                .user(UserResponse.from(savedUser))
                .build();
    }

    /**
     * 로그인
     *
     * @param request 로그인 요청 데이터
     * @return 인증 응답 (토큰 + 사용자 정보)
     * @throws IllegalArgumentException 인증 실패 시
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("로그인 시도: email={}", request.getEmail());

        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 이메일: {}", request.getEmail());
                    return new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다");
                });

        // 2. 비활성화된 계정 체크
        if (!user.getIsActive()) {
            log.warn("비활성화된 계정: email={}", request.getEmail());
            throw new IllegalArgumentException("비활성화된 계정입니다");
        }

        // 3. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("비밀번호 불일치: email={}", request.getEmail());
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다");
        }

        log.info("로그인 성공: userId={}, email={}", user.getId(), user.getEmail());

        // 4. JWT 토큰 생성
        String token = jwtTokenProvider.createToken(user.getEmail());

        // 5. 응답 생성
        return AuthResponse.builder()
                .token(token)
                .user(UserResponse.from(user))
                .build();
    }

    /**
     * 이메일 중복 체크
     *
     * 회원가입 폼에서 실시간 중복 체크용
     *
     * @param email 체크할 이메일
     * @return 중복이면 true, 사용 가능하면 false
     */
    @Transactional(readOnly = true)
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}

/*
 * ===== @Transactional 어노테이션 =====
 *
 * 트랜잭션: 데이터베이스 작업의 논리적 단위
 *
 * ACID 원칙:
 * - Atomicity (원자성): 전부 성공 or 전부 실패
 * - Consistency (일관성): 데이터 무결성 유지
 * - Isolation (격리성): 동시 트랜잭션 간 간섭 없음
 * - Durability (지속성): 커밋 후 영구 저장
 *
 * @Transactional:
 * - 메서드 시작 시 트랜잭션 시작
 * - 정상 종료 시 커밋
 * - 예외 발생 시 롤백
 *
 * @Transactional(readOnly = true):
 * - 읽기 전용 트랜잭션
 * - DB 수정 불가
 * - 성능 최적화
 *
 * 예시:
 *
 * @Transactional
 * public void transferMoney(Long fromId, Long toId, int amount) {
 *     // 1. A 계좌에서 출금
 *     withdraw(fromId, amount);
 *
 *     // 만약 여기서 예외 발생하면?
 *     // → 롤백되어 출금도 취소됨!
 *
 *     // 2. B 계좌에 입금
 *     deposit(toId, amount);
 *
 *     // 모두 성공하면 커밋
 * }
 *
 * ===== 비밀번호 암호화 =====
 *
 * BCryptPasswordEncoder:
 *
 * 1. 단방향 해시 함수
 *    - 암호화: "password123" → "$2a$10$N9qo8..."
 *    - 복호화 불가능!
 *
 * 2. Salt 사용
 *    - 같은 비밀번호라도 매번 다른 해시값
 *    - "password123" → "$2a$10$abc..."
 *    - "password123" → "$2a$10$xyz..." (다름!)
 *
 * 3. 느린 속도
 *    - 의도적으로 느리게 (Brute Force 방어)
 *    - 일반 사용자는 문제없지만 해커는 힘듦
 *
 * 암호화:
 * String encoded = passwordEncoder.encode("password123");
 * → "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
 *
 * 검증:
 * boolean matches = passwordEncoder.matches("password123", encoded);
 * → true (원본 비밀번호와 일치하는지 확인)
 *
 * ===== 보안 모범 사례 =====
 *
 * 1. 에러 메시지
 *    ❌ "존재하지 않는 이메일입니다"
 *    ❌ "비밀번호가 틀렸습니다"
 *    ✅ "이메일 또는 비밀번호가 일치하지 않습니다"
 *
 *    이유: 공격자가 이메일 존재 여부를 알 수 없게
 *
 * 2. 로그인 실패 제한
 *    - 5회 실패 시 계정 잠금
 *    - CAPTCHA 추가
 *    - IP 기반 rate limiting
 *
 * 3. 비밀번호 정책
 *    - 최소 8자 이상
 *    - 대문자, 소문자, 숫자, 특수문자 조합
 *    - 이전 비밀번호 재사용 금지
 *    - 주기적 변경 권장
 *
 * 4. 2단계 인증 (2FA)
 *    - SMS 인증
 *    - OTP (Google Authenticator)
 *    - 이메일 인증
 *
 * ===== 예외 처리 전략 =====
 *
 * 현재: IllegalArgumentException 사용
 *
 * 개선 방안:
 * - 커스텀 예외 클래스 생성
 * - 예외 코드로 세분화
 * - GlobalExceptionHandler에서 일괄 처리
 *
 * 예시:
 *
 * public class DuplicateEmailException extends RuntimeException {
 *     public DuplicateEmailException(String email) {
 *         super("이미 사용 중인 이메일입니다: " + email);
 *     }
 * }
 *
 * @ExceptionHandler(DuplicateEmailException.class)
 * public ResponseEntity<?> handleDuplicateEmail(DuplicateEmailException e) {
 *     return ResponseEntity
 *         .status(HttpStatus.CONFLICT)
 *         .body(new ErrorResponse("DUPLICATE_EMAIL", e.getMessage()));
 * }
 */