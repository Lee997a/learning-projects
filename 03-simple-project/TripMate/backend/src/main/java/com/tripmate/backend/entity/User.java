package com.tripmate.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {


    /**
    기본키 (Primary Key)
    @Id : 이 필드가 PK임을 선언
    @GeneratedValue : 자동으로 값 생성
    strategy = IDENTITY : DB의 AUTO_INCREMENT 사용
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
    이메일 (로그인 ID로 사용)
     @Colum : 컬럼 세부 설정
     nullable = flase : NOT NULL 제약 조건
     unique = true : UNIQUE 제약 조건 (중복 불가)
     length = 100 : VARCHAR(100)
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * 비밀번호 (암호화되어 저장됨)
     * 평문으로 저장하면 절대 안됨!!!!
     * BCrypt로 암호화한 값이 저장됨.
     */
    @Column(nullable = false)
    private String password;

    /**
     * 사용자 이름
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 프로필 이미지 URL
     * nullable = true가 기본값(생략 가능)
     */
    @Column(length = 500)
    private String profileImage;

    /**
     * 자기소개
     */
    @Column(length = 500)
    private String bio;

    /**
     * 계정 활성화 상태
     * 이메일 인증, 관리자 정지 등에 사용
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    /**
     * 생성시간
     * @CreationTimestamp : 엔티티 생성 시 자동으로 현재 시간 저장
     * updatable = false : 수정 불가 (생성 시간은 바뀌면 안됨)
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    /**
     * 수정 시간
     * @UpdateTimestamp : 엔티티 수정 시 자동으로 현재 시간 저장
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt;


    // ======== 비즈니스 메서드 =======

    /**
     * 프로필 업데이트
     * Entity에서 직접 값으 ㄹ변경하는 메서드를 만들어주면
     * 어디서 어떻게 변경되는지 추척하기 쉬움.
     */
    public void updateProfile(String name, String bio, String profileImage){
        this.name = name;
        this.bio = bio;
        this.profileImage = profileImage;
    }

    /**
     * 비밀번호 변경
     * @Param encodedPassword 이미 암호화된 비밀번호
     */
    public void updatePassword(String encodedPassword){
        this.password = encodedPassword;
    }

    /**
     * 계정 비활성화
     */

    public void deactivte(){
        this.isActive = false;
    }

    /**
     * 계정 활성화
     */
    public void activate(){
        this.isActive = true;
    }
}
