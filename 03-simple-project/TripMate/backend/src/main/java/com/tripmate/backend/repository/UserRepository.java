package com.tripmate.backend.repository;

import com.tripmate.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * UserRepository - 사춍자 데이터 접근 인터페이스
 *
 * JpaRepository<Entity타입, ID타입>을 상속받으면
 * 기본적인 CRUD 메서드를 자동으로 사용할 수 있다.
 *
 * 자동으로 제공되는 메서드들 :
 * - save(User user) : 저장 / 수정
 * - findById(Long id) : ID로 조회
 * - findAll() : 전체 조회
 * - deleteById(Long id) : ID로 삭제
 * - count() : 전체 개수
 * - existsById(Long id) : 존재 여부 확인
 */


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일로 사용자 찾기
     *
     * 메서드 이름 규칙 :
     * - findBy + 필드명 -> SELECT * FROM users WHERE email = ?
     * - JPA가 메서드 이름을 분석해서 자동으로 쿼리 생성
     *
     * Optional<User>로 반환하는 이유 :
     * - 사용자가 없을 수도 있으므로 null 대신 Optional 사용
     * - NullPointerException 방지
     *
     * @Param email 찾을 이메일
     * @return Optional로 감싼 User 객체
     */
    Optional<User> findByEmail (String email);

    /**
     * 이메일 중복 체크
     *
     * 메서드 이름 규칙 :
     * - existsBy + 필드명 -> SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)
     *
     * @Param email 체크할 이메일
     * @return 존재하면 true, 없으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 활성화된 사용자만 이메일로 찾기
     *
     * 메서드 이름 규칙 :
     * - findBy + 필드명1 And + 필드명2
     * -> SELECT * FROM users WHERE email = ? AND is_active = ?
     *
     * @Param email 이메일
     * @Param isActive 활성화 상태
     * @return Optional로 감싼 User 객체
     */
    Optional<User> findByEmailAndIsActive(String email, Boolean isAcrive);

    // ======== 추가로 필요할 수 있는 메서드들 ========

    /**
     * 이름으로 사용자 검색 (부분 일치)
     *
     * 메서드 이름 규칙:
     * - Containing → LIKE %이름%
     *
     * @param name 검색할 이름
     * @return 이름이 포함된 사용자 리스트
     */
    // List<User> findByNameContaining(String name);

    /**
     * 여러 이메일로 사용자 찾기
     *
     * 메서드 이름 규칙:
     * - In → WHERE email IN (?, ?, ?)
     *
     * @param emails 이메일 리스트
     * @return 해당하는 사용자 리스트
     */
    // List<User> findByEmailIn(List<String> emails);

    /**
     * 생성일 기준으로 최근 사용자 조회
     *
     * 메서드 이름 규칙:
     * - OrderBy + 필드명 + Desc → ORDER BY created_at DESC
     *
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 리스트
     */
    // Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * 커스텀 쿼리 작성 예시
     * JPA 메서드 이름 규칙으로 표현하기 어려운 경우 @Query 사용
     *
     * @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
     * Optional<User> findActiveUserByEmail(@Param("email") String email);
     */
}

/*
 * ===== JPA 메서드 이름 규칙 정리 =====
 *
 * 1. 조회 (SELECT)
 *    - findBy...         : 결과 반환
 *    - existsBy...       : boolean 반환 (존재 여부)
 *    - countBy...        : long 반환 (개수)
 *
 * 2. 조건 (WHERE)
 *    - And               : WHERE ... AND ...
 *    - Or                : WHERE ... OR ...
 *    - Between           : WHERE ... BETWEEN ? AND ?
 *    - LessThan          : WHERE ... < ?
 *    - GreaterThan       : WHERE ... > ?
 *    - Like              : WHERE ... LIKE ?
 *    - Containing        : WHERE ... LIKE %?%
 *    - StartingWith      : WHERE ... LIKE ?%
 *    - EndingWith        : WHERE ... LIKE %?
 *    - In                : WHERE ... IN (?)
 *    - IsNull            : WHERE ... IS NULL
 *    - IsNotNull         : WHERE ... IS NOT NULL
 *
 * 3. 정렬 (ORDER BY)
 *    - OrderBy...Asc     : ORDER BY ... ASC
 *    - OrderBy...Desc    : ORDER BY ... DESC
 *
 * 4. 제한 (LIMIT)
 *    - First, Top        : LIMIT 1
 *    - First10, Top10    : LIMIT 10
 *
 * 예시:
 * - findByEmailAndIsActive         → WHERE email = ? AND is_active = ?
 * - findByNameContaining           → WHERE name LIKE %?%
 * - findByCreatedAtBetween         → WHERE created_at BETWEEN ? AND ?
 * - findTop10ByOrderByCreatedAtDesc → ORDER BY created_at DESC LIMIT 10
 */

