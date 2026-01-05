package com.tripmate.backend.service;

import com.tripmate.backend.dto.request.TripCreateRequest;
import com.tripmate.backend.dto.request.TripUpdateRequest;
import com.tripmate.backend.dto.response.TripResponse;
import com.tripmate.backend.entity.Trip;
import com.tripmate.backend.entity.User;
import com.tripmate.backend.repository.TripRepository;
import com.tripmate.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 여행 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    /**
     * 여행 생성
     *
     * @param request 여행 생성 요청
     * @param userEmail 로그인한 사용자 이메일
     * @return 생성된 여행 정보
     */
    @Transactional
    public TripResponse createTrip(TripCreateRequest request, String userEmail) {
        log.info("여행 생성 시도: title={}, user={}", request.getTitle(), userEmail);

        // 1. 사용자 조회
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 2. Trip 엔티티 생성
        Trip trip = Trip.builder()
                .title(request.getTitle())
                .destination(request.getDestination())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .budget(request.getBudget())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .owner(user)
                .build();

        // 3. 저장
        Trip savedTrip = tripRepository.save(trip);
        log.info("여행 생성 완료: tripId={}", savedTrip.getId());

        // 4. Response 변환
        return TripResponse.from(savedTrip);
    }

    /**
     * 내 여행 목록 조회
     *
     * @param userEmail 로그인한 사용자 이메일
     * @return 여행 목록
     */
    @Transactional(readOnly = true)
    public List<TripResponse> getMyTrips(String userEmail) {
        log.info("내 여행 목록 조회: user={}", userEmail);

        // 사용자 조회
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 여행 목록 조회 (최신순)
        List<Trip> trips = tripRepository.findByOwnerOrderByCreatedAtDesc(user);

        // Response 변환
        return trips.stream()
                .map(TripResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 여행 상세 조회
     *
     * @param tripId 여행 ID
     * @param userEmail 로그인한 사용자 이메일
     * @return 여행 상세 정보
     */
    @Transactional(readOnly = true)
    public TripResponse getTripById(Long tripId, String userEmail) {
        log.info("여행 상세 조회: tripId={}, user={}", tripId, userEmail);

        // 사용자 조회
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 여행 조회 (owner 정보 포함)
        Trip trip = tripRepository.findByIdWithOwner(tripId)
                .orElseThrow(() -> new IllegalArgumentException("여행을 찾을 수 없습니다"));

        // 권한 체크: 내 여행이거나 공개된 여행만 조회 가능
        if (!trip.isOwner(user.getId()) && !trip.getIsPublic()) {
            throw new IllegalArgumentException("접근 권한이 없습니다");
        }

        return TripResponse.from(trip);
    }

    /**
     * 여행 수정
     *
     * @param tripId 여행 ID
     * @param request 수정 요청
     * @param userEmail 로그인한 사용자 이메일
     * @return 수정된 여행 정보
     */
    @Transactional
    public TripResponse updateTrip(Long tripId, TripUpdateRequest request, String userEmail) {
        log.info("여행 수정 시도: tripId={}, user={}", tripId, userEmail);

        // 사용자 조회
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 여행 조회 (권한 체크 포함)
        Trip trip = tripRepository.findByIdAndOwner(tripId, user)
                .orElseThrow(() -> new IllegalArgumentException("여행을 찾을 수 없거나 권한이 없습니다"));

        // 수정
        trip.update(
                request.getTitle(),
                request.getDestination(),
                request.getStartDate(),
                request.getEndDate(),
                request.getDescription(),
                request.getBudget()
        );

        log.info("여행 수정 완료: tripId={}", tripId);

        return TripResponse.from(trip);
    }

    /**
     * 여행 삭제
     *
     * @param tripId 여행 ID
     * @param userEmail 로그인한 사용자 이메일
     */
    @Transactional
    public void deleteTrip(Long tripId, String userEmail) {
        log.info("여행 삭제 시도: tripId={}, user={}", tripId, userEmail);

        // 사용자 조회
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 여행 조회 (권한 체크 포함)
        Trip trip = tripRepository.findByIdAndOwner(tripId, user)
                .orElseThrow(() -> new IllegalArgumentException("여행을 찾을 수 없거나 권한이 없습니다"));

        // 삭제
        tripRepository.delete(trip);
        log.info("여행 삭제 완료: tripId={}", tripId);
    }

    /**
     * 공개 여행 목록 조회
     *
     * @return 공개 여행 목록
     */
    @Transactional(readOnly = true)
    public List<TripResponse> getPublicTrips() {
        log.info("공개 여행 목록 조회");

        List<Trip> trips = tripRepository.findByIsPublicTrueOrderByCreatedAtDesc();

        return trips.stream()
                .map(TripResponse::from)
                .collect(Collectors.toList());
    }
}

/*
 * ===== 트랜잭션 전략 =====
 *
 * @Transactional:
 * - 메서드 전체가 하나의 트랜잭션
 * - 예외 발생 시 자동 롤백
 * - 정상 종료 시 자동 커밋
 *
 * @Transactional(readOnly = true):
 * - 읽기 전용 트랜잭션
 * - 성능 최적화
 * - Dirty Checking 비활성화
 *
 * ===== 권한 체크 패턴 =====
 *
 * 1. findByIdAndOwner() 사용:
 *    Trip trip = tripRepository.findByIdAndOwner(tripId, user)
 *        .orElseThrow(() -> new IllegalArgumentException("권한 없음"));
 *
 *    장점: 한 번의 쿼리로 조회 + 권한 체크
 *
 * 2. isOwner() 메서드 사용:
 *    Trip trip = tripRepository.findById(tripId)...
 *    if (!trip.isOwner(user.getId())) {
 *        throw new IllegalArgumentException("권한 없음");
 *    }
 *
 *    장점: 명시적, 가독성 좋음
 *
 * ===== Stream API 활용 =====
 *
 * List<Trip> → List<TripResponse> 변환:
 *
 * trips.stream()
 *     .map(TripResponse::from)
 *     .collect(Collectors.toList());
 *
 * =
 *
 * List<TripResponse> responses = new ArrayList<>();
 * for (Trip trip : trips) {
 *     responses.add(TripResponse.from(trip));
 * }
 * return responses;
 *
 * Stream이 더 간결하고 함수형 스타일
 *
 * ===== 에러 메시지 전략 =====
 *
 * 현재: IllegalArgumentException 사용
 *
 * 개선 방안:
 * - 커스텀 예외 클래스 생성
 * - 에러 코드로 세분화
 *
 * 예시:
 *
 * public class TripNotFoundException extends RuntimeException {
 *     public TripNotFoundException(Long tripId) {
 *         super("여행을 찾을 수 없습니다: " + tripId);
 *     }
 * }
 *
 * public class UnauthorizedException extends RuntimeException {
 *     public UnauthorizedException() {
 *         super("접근 권한이 없습니다");
 *     }
 * }
 *
 * ===== 변경 감지 (Dirty Checking) =====
 *
 * @Transactional 내에서 엔티티 수정 시:
 *
 * Trip trip = tripRepository.findById(id)...
 * trip.update(...);
 * // save() 호출 안 해도 자동 저장!
 *
 * JPA가 변경 사항을 감지하고 트랜잭션 커밋 시 UPDATE 쿼리 실행
 *
 * 조건:
 * - @Transactional 안에서
 * - 영속 상태의 엔티티 (findById, save로 조회된 것)
 */