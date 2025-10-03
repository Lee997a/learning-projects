package com.tripmate.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 DTO
 *
 * 클라이언트가 로그인 시 전송하는 데이터
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    /**
     * 이메일
     */
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    /**
     * 비밀번호
     */
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}

/*
 * ===== 로그인과 회원가입의 차이 =====
 *
 * LoginRequest:
 * - 이메일, 비밀번호만 필요
 * - 이름 불필요
 * - 비밀번호 길이 검증 불필요 (이미 가입된 계정)
 *
 * SignupRequest:
 * - 이메일, 비밀번호, 이름 필요
 * - 비밀번호 길이 검증 필요 (새로 만드는 계정)
 *
 * ===== 보안 고려사항 =====
 *
 * 로그인 실패 시 메시지:
 *
 * ❌ 나쁜 예:
 * "존재하지 않는 이메일입니다"
 * "비밀번호가 틀렸습니다"
 * → 공격자가 이메일 존재 여부를 알 수 있음
 *
 * ✅ 좋은 예:
 * "이메일 또는 비밀번호가 일치하지 않습니다"
 * → 어떤 게 틀렸는지 알 수 없음
 *
 * 추가 보안:
 * - 로그인 실패 횟수 제한 (5회 실패 시 계정 잠금)
 * - CAPTCHA (봇 방지)
 * - 2FA (이중 인증)
 */