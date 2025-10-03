package com.tripmate.backend.controller;

import com.tripmate.backend.dto.request.LoginRequest;
import com.tripmate.backend.dto.request.SignupRequest;
import com.tripmate.backend.dto.response.AuthResponse;
import com.tripmate.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 인증 컨트롤러
 *
 * 회원가입, 로그인 등 인증 관련 API 엔드포인트를 제공합니다.
 *
 * Base URL: /api/auth
 */
@Slf4j
@RestController
@RequestMapping("/auth")  // /api는 application.yml의 context-path로 자동 추가
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 API
     *
     * POST /api/auth/signup
     *
     * @param request 회원가입 요청 데이터
     * @return 201 Created + 인증 응답 (토큰 + 사용자 정보)
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        log.info("회원가입 요청: email={}", request.getEmail());

        AuthResponse response = authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)  // 201 Created
                .body(response);
    }

    /**
     * 로그인 API
     *
     * POST /api/auth/login
     *
     * @param request 로그인 요청 데이터
     * @return 200 OK + 인증 응답 (토큰 + 사용자 정보)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("로그인 요청: email={}", request.getEmail());

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);  // 200 OK
    }

    /**
     * 이메일 중복 체크 API
     *
     * GET /api/auth/check-email?email=test@example.com
     *
     * 회원가입 폼에서 실시간으로 이메일 중복 체크할 때 사용
     *
     * @param email 체크할 이메일
     * @return 200 OK + 중복 여부
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        log.debug("이메일 중복 체크: email={}", email);

        boolean isDuplicate = authService.isEmailDuplicate(email);

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("isDuplicate", isDuplicate);
        response.put("available", !isDuplicate);

        return ResponseEntity.ok(response);
    }

    /**
     * 테스트용 공개 API
     *
     * GET /api/auth/health
     *
     * 서버가 정상 작동하는지 확인
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Auth API is running");

        return ResponseEntity.ok(response);
    }
}

/*
 * ===== REST API 어노테이션 =====
 *
 * @RestController:
 * - @Controller + @ResponseBody
 * - 모든 메서드가 JSON 반환
 * - View를 반환하지 않음
 *
 * @RequestMapping("/auth"):
 * - 이 컨트롤러의 모든 메서드는 /auth로 시작
 * - application.yml의 context-path: /api 추가됨
 * - 최종 경로: /api/auth
 *
 * @PostMapping("/signup"):
 * - POST /api/auth/signup
 * - HTTP POST 메서드만 처리
 *
 * @GetMapping, @PutMapping, @DeleteMapping, @PatchMapping:
 * - 각각 GET, PUT, DELETE, PATCH 메서드
 *
 * ===== 요청 파라미터 어노테이션 =====
 *
 * @RequestBody:
 * - HTTP Body의 JSON을 객체로 변환
 * - Content-Type: application/json
 *
 * @RequestParam:
 * - Query String 파라미터
 * - /check-email?email=test@example.com
 *
 * @PathVariable:
 * - URL 경로의 변수
 * - /users/{id} → @PathVariable Long id
 *
 * @RequestHeader:
 * - HTTP 헤더 값
 * - @RequestHeader("Authorization") String token
 *
 * ===== Validation =====
 *
 * @Valid:
 * - Request DTO의 검증 어노테이션 자동 실행
 * - @NotBlank, @Email 등 검증
 * - 검증 실패 시 MethodArgumentNotValidException 발생
 *
 * 예시:
 *
 * @PostMapping("/signup")
 * public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
 *     // @Valid가 자동으로 검증
 *     // email이 비어있으면 → 400 Bad Request
 *     // email 형식이 틀리면 → 400 Bad Request
 * }
 *
 * ===== HTTP 상태 코드 =====
 *
 * 2xx 성공:
 * - 200 OK: 요청 성공
 * - 201 Created: 리소스 생성 성공 (회원가입, 게시글 작성)
 * - 204 No Content: 성공했지만 응답 본문 없음 (삭제)
 *
 * 4xx 클라이언트 오류:
 * - 400 Bad Request: 잘못된 요청 (검증 실패)
 * - 401 Unauthorized: 인증 필요
 * - 403 Forbidden: 권한 없음
 * - 404 Not Found: 리소스 없음
 * - 409 Conflict: 충돌 (중복)
 *
 * 5xx 서버 오류:
 * - 500 Internal Server Error: 서버 오류
 * - 503 Service Unavailable: 서비스 이용 불가
 *
 * ===== ResponseEntity =====
 *
 * 상태 코드와 헤더를 자유롭게 설정 가능
 *
 * 방법 1: 간단한 방법
 * return ResponseEntity.ok(data);  // 200 OK
 *
 * 방법 2: 상태 코드 지정
 * return ResponseEntity
 *     .status(HttpStatus.CREATED)  // 201
 *     .body(data);
 *
 * 방법 3: 헤더 추가
 * return ResponseEntity
 *     .status(HttpStatus.CREATED)
 *     .header("Location", "/api/users/1")
 *     .body(data);
 *
 * 방법 4: 응답 본문 없음
 * return ResponseEntity.noContent().build();  // 204
 *
 * ===== API 설계 모범 사례 =====
 *
 * 1. RESTful URL 설계
 *    ✅ POST /api/auth/signup (명사형)
 *    ❌ POST /api/auth/doSignup (동사형)
 *
 * 2. HTTP 메서드 의미에 맞게
 *    - GET: 조회
 *    - POST: 생성
 *    - PUT: 전체 수정
 *    - PATCH: 부분 수정
 *    - DELETE: 삭제
 *
 * 3. 적절한 상태 코드
 *    - 회원가입 성공 → 201 Created
 *    - 로그인 성공 → 200 OK
 *    - 삭제 성공 → 204 No Content
 *
 * 4. 일관된 응답 형식
 *    성공:
 *    {
 *      "data": { ... }
 *    }
 *
 *    실패:
 *    {
 *      "error": {
 *        "code": "DUPLICATE_EMAIL",
 *        "message": "이미 사용 중인 이메일입니다"
 *      }
 *    }
 *
 * 5. 버전 관리
 *    - /api/v1/auth/signup
 *    - API 변경 시 하위 호환성 유지
 *
 * ===== 테스트 방법 =====
 *
 * 1. cURL:
 * curl -X POST http://localhost:8080/api/auth/signup \
 *   -H "Content-Type: application/json" \
 *   -d '{"email":"test@example.com","password":"password123","name":"홍길동"}'
 *
 * 2. Postman:
 *    - POST http://localhost:8080/api/auth/signup
 *    - Body → raw → JSON
 *    - 요청 본문 입력
 *
 * 3. HTTPie:
 * http POST localhost:8080/api/auth/signup \
 *   email=test@example.com \
 *   password=password123 \
 *   name=홍길동
 *
 * 4. IntelliJ HTTP Client:
 *    - .http 파일 생성
 *    - 요청 작성 후 실행
 */