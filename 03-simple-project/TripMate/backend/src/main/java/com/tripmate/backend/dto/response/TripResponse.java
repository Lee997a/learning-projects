package com.tripmate.backend.dto.response;

import com.tripmate.backend.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 여행 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResponse {

    private Long id;
    private String title;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private String coverImage;
    private String description;
    private Double budget;
    private Boolean isPublic;

    // 소유자 정보 (간단하게)
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Trip Entity를 TripResponse로 변환
     */
    public static TripResponse from(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .destination(trip.getDestination())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .coverImage(trip.getCoverImage())
                .description(trip.getDescription())
                .budget(trip.getBudget())
                .isPublic(trip.getIsPublic())
                .ownerId(trip.getOwner().getId())
                .ownerName(trip.getOwner().getName())
                .ownerEmail(trip.getOwner().getEmail())
                .createdAt(trip.getCreatedAt())
                .updatedAt(trip.getUpdatedAt())
                .build();
    }
}

/*
 * ===== DTO 변환 패턴 =====
 *
 * Entity → Response DTO 변환:
 *
 * 1. 정적 팩토리 메서드 (from)
 *    TripResponse.from(trip)
 *
 * 2. 생성자
 *    new TripResponse(trip)
 *
 * 3. Builder
 *    TripResponse.builder()...build()
 *
 * 추천: 정적 팩토리 메서드
 * - 이름으로 의도 명확
 * - 변환 로직 캡슐화
 * - 테스트 쉬움
 *
 * ===== 왜 소유자 정보를 포함? =====
 *
 * 여행 목록에서 누가 만든 여행인지 바로 표시 가능:
 *
 * {
 *   "id": 1,
 *   "title": "제주도 여행",
 *   "ownerName": "홍길동",  ← 바로 사용 가능
 *   ...
 * }
 *
 * User 전체 정보는 불필요하므로 필요한 것만 포함
 */