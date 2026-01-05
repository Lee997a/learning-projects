package com.tripmate.backend.controller;

import com.tripmate.backend.dto.request.TripCreateRequest;
import com.tripmate.backend.dto.request.TripUpdateRequest;
import com.tripmate.backend.dto.response.TripResponse;
import com.tripmate.backend.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 여행 컨트롤러
 *
 * Base URL: /api/trips
 * 모든 API는 인증 필요 (JWT 토큰)
 */
@Slf4j
@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    /**
     * 여행 생성
     *
     * POST /api/trips
     *
     * @param request 여행 생성 요청
     * @param authentication Spring Security 인증 정보
     * @return 201 Created + 생성된 여행 정보
     */
    @PostMapping
    public ResponseEntity<TripResponse> createTrip(
            @Valid @RequestBody TripCreateRequest request,
            Authentication authentication
    ) {
        String userEmail = authentication.getName(); // JWT에서 추출한 이메일
        log.info("여행 생성 요청: user={}, title={}", userEmail, request.getTitle());

        TripResponse response = tripService.createTrip(request, userEmail);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * 내 여행 목록 조회
     *
     * GET /api/trips
     *
     * @param authentication Spring Security 인증 정보
     * @return 200 OK + 여행 목록
     */
    @GetMapping
    public ResponseEntity<List<TripResponse>> getMyTrips(Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("내 여행 목록 조회: user={}", userEmail);

        List<TripResponse> trips = tripService.getMyTrips(userEmail);

        return ResponseEntity.ok(trips);
    }

    /**
     * 여행 상세 조회
     *
     * GET /api/trips/{id}
     *
     * @param id 여행 ID
     * @param authentication Spring Security 인증 정보
     * @return 200 OK + 여행 상세 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTripById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        log.info("여행 상세 조회: tripId={}, user={}", id, userEmail);

        TripResponse trip = tripService.getTripById(id, userEmail);

        return ResponseEntity.ok(trip);
    }

    /**
     * 여행 수정
     *
     * PUT /api/trips/{id}
     *
     * @param id 여행 ID
     * @param request 수정 요청
     * @param authentication Spring Security 인증 정보
     * @return 200 OK + 수정된 여행 정보
     */
    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripUpdateRequest request,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        log.info("여행 수정 요청: tripId={}, user={}", id, userEmail);

        TripResponse trip = tripService.updateTrip(id, request, userEmail);

        return ResponseEntity.ok(trip);
    }

    /**
     * 여행 삭제
     *
     * DELETE /api/trips/{id}
     *
     * @param id 여행 ID
     * @param authentication Spring Security 인증 정보
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        log.info("여행 삭제 요청: tripId={}, user={}", id, userEmail);

        tripService.deleteTrip(id, userEmail);

        return ResponseEntity.noContent().build();
    }

    /**
     * 공개 여행 목록 조회
     *
     * GET /api/trips/public
     *
     * @return 200 OK + 공개 여행 목록
     */
    @GetMapping("/public")
    public ResponseEntity<List<TripResponse>> getPublicTrips() {
        log.info("공개 여행 목록 조회");

        List<TripResponse> trips = tripService.getPublicTrips();

        return ResponseEntity.ok(trips);
    }
}

/*
 * ===== Authentication 객체 =====
 *
 * Spring Security의 인증 정보:
 *
 * @GetMapping
 * public ResponseEntity<?> getData(Authentication authentication) {
 *     String email = authentication.getName();  // 이메일 (JWT의 subject)
 *     // ...
 * }
 *
 * JWT 필터에서 SecurityContext에 저장한 인증 정보를
 * Spring이 자동으로 주입해줌
 *
 * 흐름:
 * 1. 클라이언트가 JWT 토큰을 Header에 담아 요청
 * 2. JwtAuthenticationFilter에서 토큰 검증
 * 3. SecurityContext에 Authentication 저장
 * 4. Controller에서 Authentication 주입받아 사용
 *
 * ===== @PathVariable =====
 *
 * URL 경로의 변수를 파라미터로 받기:
 *
 * @GetMapping("/{id}")
 * public ResponseEntity<?> get(@PathVariable Long id) {
 *     // GET /api/trips/1 → id = 1
 *     // GET /api/trips/999 → id = 999
 * }
 *
 * 여러 개도 가능:
 *
 * @GetMapping("/{tripId}/schedules/{scheduleId}")
 * public ResponseEntity<?> get(
 *     @PathVariable Long tripId,
 *     @PathVariable Long scheduleId
 * ) { ... }
 *
 * ===== HTTP 메서드별 용도 =====
 *
 * POST /api/trips
 * - 여행 생성
 * - 201 Created 반환
 *
 * GET /api/trips
 * - 여행 목록 조회
 * - 200 OK 반환
 *
 * GET /api/trips/{id}
 * - 여행 상세 조회
 * - 200 OK 반환
 *
 * PUT /api/trips/{id}
 * - 여행 전체 수정
 * - 200 OK 반환
 *
 * PATCH /api/trips/{id}
 * - 여행 부분 수정 (예: 제목만 수정)
 * - 200 OK 반환
 *
 * DELETE /api/trips/{id}
 * - 여행 삭제
 * - 204 No Content 반환
 *
 * ===== 인증 vs 인가 =====
 *
 * 인증 (Authentication):
 * - "너 누구야?"
 * - JWT 토큰으로 사용자 확인
 * - 로그인 여부 체크
 *
 * 인가 (Authorization):
 * - "이거 할 권한 있어?"
 * - Service에서 소유자 확인
 * - findByIdAndOwner()로 권한 체크
 *
 * 예시:
 *
 * 인증: JWT 토큰 있음 → 로그인된 사용자
 * 인가: 이 여행의 주인인가? → 수정/삭제 가능
 *
 * ===== ResponseEntity.noContent() =====
 *
 * 삭제 성공 시:
 * - 204 No Content
 * - 응답 본문 없음
 *
 * return ResponseEntity.noContent().build();
 *
 * → HTTP/1.1 204 No Content
 *    (본문 없음)
 *
 * 다른 상태 코드:
 * - ResponseEntity.ok() → 200
 * - ResponseEntity.created() → 201
 * - ResponseEntity.badRequest() → 400
 * - ResponseEntity.notFound() → 404
 */