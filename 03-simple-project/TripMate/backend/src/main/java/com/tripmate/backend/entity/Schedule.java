package com.tripmate.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 일정 날짜
     */
    @Column
    private LocalDate date;

    /**
     * 일정 시간 (선택사항)
     * 예: "09:00", "13:30"
     */
    @Column(length = 10)
    private String time;

    /**
     * 일정 제목
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 장소
     */
    @Column(length = 200)
    private String location;

    /**
     * 위도
     */
    private Double latitude;

    /**
     * 경도
     */
    private Double longitude;

    /**
     * 일정 설명
     */
    @Column(length = 1000)
    private String description;

    /**
     * 카테고리
     * 예: "숙박", "식사", "관광", "교통" ...
     */
    @Column(length = 50)
    private String category;

    /**
     * 정렬 순서
     * 같은 날짜 내에서 순서 지정
     */
    @Builder.Default
    @Column(nullable = false)
    private Integer orderIndex = 0;

    /**
     * 소속 여행
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ===== 비즈니스 메서드 ====-=

    /**
     * 일정 정보 수정
     */
    public void update(LocalDate date, String time, String title,
                       String location, String description, String category){
        this.date = date;
        this.time = time;
        this.title = title;
        this.location = location;
        this.description = description;
        this.category = category;
    }

    /**
     * 위치 정보 업데이트
     */
    public void updateLocation(String location, Double latitude, Double longitude) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * 순서 변경
     */
    public void updateOrder(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
}

/*
 * ===== 일정 데이터 구조 =====
 *
 * Trip (1) ─── (N) Schedule
 *
 * 예시:
 *
 * 제주도 여행 (Trip)
 *   ├─ 2024-11-01 09:00 - 공항 출발 (Schedule)
 *   ├─ 2024-11-01 13:00 - 호텔 체크인 (Schedule)
 *   ├─ 2024-11-01 18:00 - 저녁 식사 (Schedule)
 *   ├─ 2024-11-02 10:00 - 한라산 등반 (Schedule)
 *   └─ 2024-11-03 11:00 - 공항 출발 (Schedule)
 *
 * ===== LocalDate vs String time =====
 *
 * date: LocalDate (2024-11-01)
 * - 날짜만 저장
 * - 날짜별 그룹핑 쉬움
 *
 * time: String ("09:00")
 * - 선택사항 (null 가능)
 * - 간단한 형식으로 충분
 * - LocalTime 대신 String 사용 이유:
 *   1. 사용자가 입력하기 쉬움
 *   2. 시간이 정확하지 않아도 됨 ("오전", "저녁" 같은 텍스트도 가능)
 *   3. DB 저장 간단
 *
 * ===== 위도/경도 저장 =====
 *
 * latitude: 위도 (Double)
 * longitude: 경도 (Double)
 *
 * 용도:
 * - 지도에 마커 표시
 * - 경로 최적화
 * - 거리 계산
 *
 * 예시:
 * - 서울 시청: 37.5665, 126.9780
 * - 부산 해운대: 35.1584, 129.1604
 *
 * ===== orderIndex (정렬 순서) =====
 *
 * 같은 날짜 내에서 순서 지정:
 *
 * 2024-11-01
 *   - orderIndex: 0 → 09:00 공항 출발
 *   - orderIndex: 1 → 13:00 호텔 체크인
 *   - orderIndex: 2 → 18:00 저녁 식사
 *
 * 사용자가 드래그 앤 드롭으로 순서 변경 가능
 *
 * ===== 카테고리 예시 =====
 *
 * - 숙박: 호텔 체크인/아웃
 * - 식사: 아침, 점심, 저녁
 * - 관광: 명소 방문
 * - 교통: 이동 (비행기, 기차, 버스)
 * - 쇼핑: 쇼핑몰, 시장
 * - 액티비티: 등산, 서핑, 다이빙
 * - 기타: 자유 시간
 *
 * 프론트엔드에서 아이콘/색상으로 구분 가능
 */