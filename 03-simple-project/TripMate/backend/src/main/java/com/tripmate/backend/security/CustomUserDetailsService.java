package com.tripmate.backend.security;

import com.tripmate.backend.entity.User;
import com.tripmate.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     *이메일 (username)로 사용자 정보 로드
     *
     * Spring Security는 username이라는 용어를 사용하지만,
     * 우리는 이메일을 사용자 ID로 사용함.
     *
     * @param email 사용자 이메일
     * @return UserDetails 객체 (Spring Security가 인식)
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("사용자 정보 로드 시도 : email = {}", email);

        //1. DB에서 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다. email = {}", email);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다. : " + email);
                });

        // 2. 비활성화된 계정 체크
        if(!user.getIsActive()){
            log.error("비활성화된 계정입니다. : email = {}", email);
            throw new UsernameNotFoundException("비활성화된 계정입니다." + email);
        }

        log.debug("사용자 정보 로드 성공 : email = {}, name = {}", email, user.getName());

        //3. User 엔티티를 Spring Security의 UserDetails로 변환
        return createUserDetails(user);
    }

    /**
     * User 엔티티를 Spring Security UserDetails로 변환
     *
     * @param user User 엔티티
     * @return UserDetails 객체
     */
    private UserDetails createUserDetails(User user){
        //권한 (Authority)생성
        // 현재는 단순하게 "ROLE_USER" 하나만 부여
        // 나중에 관리자 기능 추가 시 "ROLE_ADMIN" 등 추가 가능
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        //Spring Security의 User 객체 생성 (우리의 User 엔티티와는 다른 클래스!)
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())          // 이메일을 username으로 사용
                .password(user.getPassword())       // 암호화된 비밀번호
                .authorities(authorities)           // 권한 목록
                .accountExpired(false)              // 계정 만료 여부
                .accountLocked(false)               // 계정 잠김 여부
                .credentialsExpired(false)          // 비밀번호 만료 여부
                .disabled(!user.getIsActive())      // 계정 비활성화 여부
                .build();
    }
}


/*
 * ===== UserDetails란? =====
 *
 * Spring Security가 인증/인가에 사용하는 사용자 정보 인터페이스
 *
 * 주요 메서드:
 * - getUsername(): 사용자 식별자 (우리는 이메일)
 * - getPassword(): 암호화된 비밀번호
 * - getAuthorities(): 권한 목록
 * - isEnabled(): 계정 활성화 여부
 * - isAccountNonExpired(): 계정 만료 여부
 * - isAccountNonLocked(): 계정 잠김 여부
 * - isCredentialsNonExpired(): 비밀번호 만료 여부
 *
 * ===== 권한(Authority)이란? =====
 *
 * 사용자가 할 수 있는 작업을 정의
 *
 * 예시:
 * - ROLE_USER: 일반 사용자
 * - ROLE_ADMIN: 관리자
 * - ROLE_PREMIUM: 프리미엄 회원
 *
 * 사용 예:
 * @PreAuthorize("hasRole('ADMIN')")
 * public void deleteUser() { ... }
 *
 * ===== 왜 두 개의 User 클래스? =====
 *
 * 1. com.tripmate.backend.entity.User
 *    - 우리가 만든 엔티티
 *    - DB 테이블과 매핑
 *    - 비즈니스 로직에 사용
 *
 * 2. org.springframework.security.core.userdetails.User
 *    - Spring Security가 제공하는 클래스
 *    - 인증/인가에만 사용
 *    - UserDetails 인터페이스 구현체
 *
 * 변환 과정:
 * DB User 엔티티 → UserDetails 객체 → Spring Security 인증
 *
 * ===== 트랜잭션 어노테이션 =====
 *
 * @Transactional(readOnly = true)
 * - 읽기 전용 트랜잭션
 * - DB 수정 불가 (조회만 가능)
 * - 성능 최적화 (플러시 생략 등)
 * - Dirty Checking 비활성화
 *
 * 장점:
 * - 실수로 데이터 수정 방지
 * - 약간의 성능 향상
 */
