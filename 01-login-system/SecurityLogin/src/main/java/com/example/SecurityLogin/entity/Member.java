package com.example.SecurityLogin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화 (날짜 추기시 추가됨)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nick_name", nullable = false)
    private String nickname;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    // 권할 설정
    /*
        STRING을 사용한 이유 :
        - 가독성 : DB에서 직접 활인할 때 "USER"가 0보다 이해하기 쉬움
        - 안전성 : enum 순서가 바뀌어도 데이터가 안전함.
        - 확장성 : 새로운 역할 추가시에도 기존 데이터에 영향 없음.
     */
    @Enumerated(EnumType.STRING) // 문자열로 저장
    @Column(name = "role")
    private Role role;

    // 회원가입 날짜 - 자동으로 생성시간 기록
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    // 마지막 수정 날짜 - 자동으로 수정시간 업데이트
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    // UserDetails 인터페이스 구현

    /*
        권한 정보
        role.name() -> "USER" 또는 "ADMIN" 반환
        "ROLE_" + role.name(0 -> "ROLE_USER" 또는 "ROLE_ADMIN"
        Spring Security는 이 권한으로 접근 제어

        SecurityConfig에서
        .requestMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN 필요
        .requestMatchers("/user/**").hasRole("USER")   // ROLE_USER 필요
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호가 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화됨
    }
}
