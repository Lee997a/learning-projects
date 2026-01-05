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

/**
 * Trip Entity - 여행 정보
 */
@Entity
@Table(name = "trips")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 여행 제목
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 여행지
     */
    @Column(nullable = false, length = 200)
    private String destination;

    /**
     * 여행 시작일
     */
    @Column(nullable = false)
    private LocalDate startDate;

    /**
     * 여행 종료일
     */
    @Column(nullable = false)
    private LocalDate endDate;

    /**
     * 커버 이미지 URL
     */
    @Column(length = 500)
    private String coverImage;

    /**
     * 여행 설명
     */
    @Column(length = 1000)
    private String description;

    /**
     * 예산
     */
    private Double budget;

    /**
     * 공개 여부
     * true: 공개, false: 비공개
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean isPublic = false;

    /**
     * 여행 생성자 (소유자)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ===== 비즈니스 메서드 =====

    /**
     * 여행 정보 수정
     */
    public void update(String title, String destination, LocalDate startDate,
                       LocalDate endDate, String description, Double budget) {
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.budget = budget;
    }

    /**
     * 커버 이미지 변경
     */
    public void updateCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    /**
     * 공개 여부 변경
     */
    public void togglePublic() {
        this.isPublic = !this.isPublic;
    }

    /**
     * 소유자 확인
     */
    public boolean isOwner(Long userId) {
        return this.owner.getId().equals(userId);
    }
}

/*
 * ===== @ManyToOne 관계 =====
 *
 * Trip (N) : User (1)
 * - 여러 개의 여행은 한 명의 사용자에게 속함
 * - 한 사용자는 여러 여행을 만들 수 있음
 *
 * @ManyToOne:
 * - Trip이 Many(N), User가 One(1)
 * - Trip 테이블에 user_id 외래키 생성
 *
 * fetch = FetchType.LAZY:
 * - Trip 조회 시 User는 즉시 로딩하지 않음
 * - owner.getName() 같이 실제 사용할 때 로딩
 * - N+1 문제 방지 (성능 최적화)
 *
 * FetchType.EAGER vs LAZY:
 * - EAGER: Trip 조회 시 User도 함께 조회 (JOIN)
 * - LAZY: Trip만 조회, User는 필요할 때 조회
 * - 기본값: @ManyToOne은 EAGER, @OneToMany는 LAZY
 * - 추천: 명시적으로 LAZY 사용
 *
 * ===== LocalDate vs LocalDateTime =====
 *
 * LocalDate:
 * - 날짜만 (2025-10-03)
 * - 여행 시작일, 종료일
 *
 * LocalDateTime:
 * - 날짜 + 시간 (2025-10-03 18:30:00)
 * - 생성 시간, 수정 시간
 *
 * ===== 비즈니스 메서드 패턴 =====
 *
 * Setter 대신 의도가 명확한 메서드 사용:
 *
 * ❌ setTitle(), setDescription() 등
 * ✅ update() - 어떤 작업인지 명확
 * ✅ togglePublic() - 공개/비공개 토글
 * ✅ isOwner() - 권한 체크
 *
 * 장점:
 * - 의도가 명확
 * - 검증 로직 추가 가능
 * - 변경 추적 용이
 */