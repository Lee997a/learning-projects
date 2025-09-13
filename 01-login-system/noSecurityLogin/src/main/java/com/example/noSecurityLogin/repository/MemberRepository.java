package com.example.noSecurityLogin.repository;

import com.example.noSecurityLogin.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

/*
    Repository의 역할 :
    소프트웨어 개발에서 데이터 접근 및 관리의 중심 역할을 수행하며, 애플리케이션과 데이터베이스 간의 중개자 역할을 함.

    Spring Data JPA 등에서 제공하는 Repository는 ORM(Object-Relational Mapping) 기능을 활용하여
    객체와 데이터베이스 테이블 간의 매핑을 자동화하는데 사용함.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 데이터베이스에 이메일을 기준으로 사용자 조회
    Optional<Member> findByEmail(String email);

    // 이메일이 존재하는지 확인
    boolean existsByEmail(String email);
}
