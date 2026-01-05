package com.tripmate.backend.repository;

import com.tripmate.backend.entity.Trip;
import com.tripmate.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TripRepository - 여행 데이터 접근
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    /**
     * 특정 사용자의 모든 여행 조회
     *
     * @param owner 사용자
     * @return 여행 목록 (최신순)
     */
    List<Trip> findByOwnerOrderByCreatedAtDesc(User owner);

    /**
     * 특정 사용자의 여행 중 ID로 조회
     * 권한 체크용
     *
     * @param id 여행 ID
     * @param owner 소유자
     * @return Optional<Trip>
     */
    Optional<Trip> findByIdAndOwner(Long id, User owner);

    /**
     * 공개된 여행만 조회
     *
     * @return 공개 여행 목록 (최신순)
     */
    List<Trip> findByIsPublicTrueOrderByCreatedAtDesc();

    /**
     * 여행지로 검색 (공개된 여행만)
     *
     * @param destination 여행지 (부분 일치)
     * @return 여행 목록
     */
    List<Trip> findByDestinationContainingAndIsPublicTrue(String destination);

    /**
     * 사용자의 여행 개수
     *
     * @param owner 사용자
     * @return 여행 개수
     */
    long countByOwner(User owner);

    /**
     * fetch join으로 N+1 문제 해결
     * Trip 조회 시 Owner도 함께 로딩
     *
     * @param id 여행 ID
     * @return Optional<Trip>
     */
    @Query("SELECT t FROM Trip t JOIN FETCH t.owner WHERE t.id = :id")
    Optional<Trip> findByIdWithOwner(@Param("id") Long id);

    /**
     * 사용자의 여행을 owner와 함께 조회
     *
     * @param owner 사용자
     * @return 여행 목록
     */
    @Query("SELECT t FROM Trip t JOIN FETCH t.owner WHERE t.owner = :owner ORDER BY t.createdAt DESC")
    List<Trip> findByOwnerWithOwner(@Param("owner") User owner);
}

/*
 * ===== JPA 메서드 이름 규칙 복습 =====
 *
 * findByOwnerOrderByCreatedAtDesc()
 * → SELECT * FROM trips WHERE owner = ? ORDER BY created_at DESC
 *
 * findByIdAndOwner()
 * → SELECT * FROM trips WHERE id = ? AND owner = ?
 *
 * findByIsPublicTrueOrderByCreatedAtDesc()
 * → SELECT * FROM trips WHERE is_public = true ORDER BY created_at DESC
 *
 * findByDestinationContainingAndIsPublicTrue()
 * → SELECT * FROM trips WHERE destination LIKE %?% AND is_public = true
 *
 * countByOwner()
 * → SELECT COUNT(*) FROM trips WHERE owner = ?
 *
 * ===== N+1 문제란? =====
 *
 * 문제 상황:
 *
 * List<Trip> trips = tripRepository.findAll(); // 1번 쿼리
 * for (Trip trip : trips) {
 *     trip.getOwner().getName(); // N번 쿼리 (각 Trip마다)
 * }
 *
 * 총 쿼리: 1 + N번 = 비효율적!
 *
 * 해결: fetch join
 *
 * @Query("SELECT t FROM Trip t JOIN FETCH t.owner")
 *
 * → SELECT t.*, u.* FROM trips t INNER JOIN users u ON t.user_id = u.id
 *
 * 한 번의 쿼리로 Trip + User 모두 조회!
 *
 * ===== @Query 어노테이션 =====
 *
 * JPQL (Java Persistence Query Language) 작성:
 * - SQL과 비슷하지만 엔티티 기반
 * - 테이블명 대신 엔티티명 사용
 * - 컬럼명 대신 필드명 사용
 *
 * 예시:
 *
 * SQL:
 * SELECT * FROM trips WHERE user_id = ?
 *
 * JPQL:
 * SELECT t FROM Trip t WHERE t.owner = :owner
 *
 * ===== 메서드 선택 가이드 =====
 *
 * 1. 단순 조회: 메서드 이름 규칙
 *    findByOwner(), findByIdAndOwner()
 *
 * 2. 복잡한 조회: @Query
 *    JOIN FETCH, 서브쿼리, 복잡한 조건
 *
 * 3. 성능 중요: @Query + fetch join
 *    N+1 문제 해결
 *
 * ===== 실전 사용 예시 =====
 *
 * // 내 여행 목록 (owner 정보 필요 없음)
 * List<Trip> trips = tripRepository.findByOwnerOrderByCreatedAtDesc(user);
 *
 * // 여행 상세 (owner 정보 필요)
 * Trip trip = tripRepository.findByIdWithOwner(id)
 *     .orElseThrow(() -> new RuntimeException("여행을 찾을 수 없습니다"));
 *
 * // 권한 체크
 * Trip trip = tripRepository.findByIdAndOwner(id, user)
 *     .orElseThrow(() -> new RuntimeException("권한이 없습니다"));
 *
 * // 공개 여행 검색
 * List<Trip> trips = tripRepository.findByDestinationContainingAndIsPublicTrue("제주");
 */