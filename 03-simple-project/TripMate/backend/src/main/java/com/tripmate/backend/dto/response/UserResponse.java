package com.tripmate.backend.dto.response;

import com.tripmate.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 정보 응답 DTO
 *
 * 클라이언트에게 사용자 정볼르 전달할 때 사용
 * 비밀번호 등 민감한 정보는 제외
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String profileImage;
    private String bio;
    private LocalDateTime createdAt;


    public static UserResponse from (User user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .createdAt(user.getCreateAt())
                .build();
    }
}

/*
 * ===== 정적 팩토리 메서드 패턴 =====
 *
 * from(), of(), valueOf() 등의 정적 메서드로 객체 생성
 *
 * 장점:
 * 1. 이름이 있어서 의도가 명확
 *    - new UserResponse(user) vs UserResponse.from(user)
 *
 * 2. 반환 타입을 유연하게 할 수 있음
 *    - 상황에 따라 다른 하위 타입 반환 가능
 *
 * 3. 매번 새 객체를 만들 필요 없음
 *    - 캐싱 가능
 *
 * 4. 가독성
 *    - UserResponse.from(user) - "user로부터 만든다"
 *    - UserResponse.of(id, email) - "이것들로 만든다"
 *
 * 사용 예:
 *
 * User user = userRepository.findById(1L).orElseThrow();
 * UserResponse response = UserResponse.from(user);
 *
 * ===== 왜 비밀번호를 제외? =====
 *
 * 보안상 매우 중요!
 *
 * ❌ Entity를 그대로 반환하면:
 * {
 *   "id": 1,
 *   "email": "user@example.com",
 *   "password": "$2a$10$encoded...",  ← 암호화되어도 노출 위험!
 *   "name": "홍길동"
 * }
 *
 * ✅ Response DTO 사용:
 * {
 *   "id": 1,
 *   "email": "user@example.com",
 *   "name": "홍길동"
 * }
 *
 * 추가 고려사항:
 * - 이메일도 민감 정보일 수 있음 (상황에 따라 마스킹)
 * - 전화번호는 일부 마스킹 (010-1234-XXXX)
 * - 개인정보는 최소한만 노출
 *
 * ===== Response DTO 설계 전략 =====
 *
 * 1. 기본 정보만 (UserResponse)
 *    - id, email, name
 *    - 목록 조회, 간단한 정보
 *
 * 2. 상세 정보 (UserDetailResponse)
 *    - 위 + profileImage, bio, createdAt
 *    - 프로필 상세 페이지
 *
 * 3. 요약 정보 (UserSummaryResponse)
 *    - id, name만
 *    - 댓글 작성자, 여행 참여자 등
 *
 * 상황에 맞게 여러 Response를 만들어 사용
 */