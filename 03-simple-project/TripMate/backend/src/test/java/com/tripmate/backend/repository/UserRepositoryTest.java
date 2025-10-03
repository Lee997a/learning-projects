package com.tripmate.backend.repository;

import com.tripmate.backend.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * UserRepository 테스트
 *
 * @DataJpaTest: JPA 관련 컴포넌트만 로드하는 가벼운 테스트
 * - 실제 DB 대신 인메모리 H2 DB 사용
 * - 각 테스트 후 자동 롤백 (데이터 초기화)
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 저장 및 조회 테스트")
    void saveAndFindUser() {
        // Given: 테스트 데이터 준비
        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword123")
                .name("테스트유저")
                .isActive(true)
                .build();

        // When: 저장
        User savedUser = userRepository.save(user);

        // Then: 검증
        assertThat(savedUser.getId()).isNotNull();  // ID가 자동 생성되었는지
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getName()).isEqualTo("테스트유저");
    }

    @Test
    @DisplayName("이메일로 사용자 찾기 테스트")
    void findByEmail() {
        // Given: 사용자 저장
        User user = User.builder()
                .email("find@example.com")
                .password("password123")
                .name("찾을유저")
                .isActive(true)
                .build();
        userRepository.save(user);

        // When: 이메일로 조회
        Optional<User> foundUser = userRepository.findByEmail("find@example.com");

        // Then: 검증
        assertThat(foundUser).isPresent();  // 사용자가 존재하는지
        assertThat(foundUser.get().getName()).isEqualTo("찾을유저");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 조회 시 빈 Optional 반환")
    void findByEmailNotFound() {
        // When: 존재하지 않는 이메일로 조회
        Optional<User> foundUser = userRepository.findByEmail("notfound@example.com");

        // Then: 빈 Optional 반환
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("이메일 중복 체크 테스트")
    void existsByEmail() {
        // Given: 사용자 저장
        User user = User.builder()
                .email("exists@example.com")
                .password("password123")
                .name("중복체크유저")
                .isActive(true)
                .build();
        userRepository.save(user);

        // When & Then: 존재하는 이메일
        assertThat(userRepository.existsByEmail("exists@example.com")).isTrue();

        // When & Then: 존재하지 않는 이메일
        assertThat(userRepository.existsByEmail("notexists@example.com")).isFalse();
    }

    @Test
    @DisplayName("활성화된 사용자만 조회 테스트")
    void findByEmailAndIsActive() {
        // Given: 활성/비활성 사용자 저장
        User activeUser = User.builder()
                .email("active@example.com")
                .password("password123")
                .name("활성유저")
                .isActive(true)
                .build();

        User inactiveUser = User.builder()
                .email("inactive@example.com")
                .password("password123")
                .name("비활성유저")
                .isActive(false)
                .build();

        userRepository.save(activeUser);
        userRepository.save(inactiveUser);

        // When & Then: 활성 사용자 조회 성공
        Optional<User> found1 = userRepository.findByEmailAndIsActive("active@example.com", true);
        assertThat(found1).isPresent();

        // When & Then: 비활성 사용자는 조회 안됨
        Optional<User> found2 = userRepository.findByEmailAndIsActive("inactive@example.com", true);
        assertThat(found2).isEmpty();
    }
}