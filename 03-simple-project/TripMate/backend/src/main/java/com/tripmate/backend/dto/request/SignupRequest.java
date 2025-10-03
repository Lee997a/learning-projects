package com.tripmate.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 DTO
 *
 * 클라이언트가 회원가입 시 전송하는 데이터
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {

    /**
     * 이메일
     *
     * @NotBlank : null, 빈 문자열, 공백 불가
     * @Email : 이메일 형식 검증
     */
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
   private String email;

    /**
     * 비밀번호
     *
     * @Size : 최소 / 최대 길이 제한
     */
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 5, message = "이름은 2자 이상 50자 이하여야 합니다.")
    private String name;
}

/*
 * ===== Validation 어노테이션 =====
 *
 * Spring Boot Validation (jakarta.validation)을 사용하면
 * Controller에서 자동으로 입력값을 검증할 수 있습니다.
 *
 * 주요 어노테이션:
 *
 * @NotNull: null 불가
 * @NotBlank: null, 빈 문자열, 공백 불가
 * @NotEmpty: null, 빈 문자열 불가 (공백은 허용)
 *
 * @Size(min, max): 문자열 길이 또는 컬렉션 크기
 * @Min(value): 최소값
 * @Max(value): 최대값
 *
 * @Email: 이메일 형식
 * @Pattern(regexp): 정규표현식 패턴 매칭
 *
 * @Past: 과거 날짜
 * @Future: 미래 날짜
 *
 * 사용 예:
 *
 * @PostMapping("/signup")
 * public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
 *     // @Valid가 자동으로 검증
 *     // 검증 실패 시 MethodArgumentNotValidException 발생
 * }
 *
 * ===== 왜 final이 아닌가? =====
 *
 * Request DTO는 보통 final을 사용하지 않습니다:
 *
 * 1. Jackson이 JSON을 역직렬화할 때 setter나 기본 생성자 필요
 * 2. @Builder는 불변 객체가 아님
 * 3. 요청 데이터는 일회성이므로 불변성이 크게 중요하지 않음
 *
 * 만약 불변 DTO를 원한다면:
 * - record 사용 (Java 16+)
 * - @Value (Lombok)
 */
