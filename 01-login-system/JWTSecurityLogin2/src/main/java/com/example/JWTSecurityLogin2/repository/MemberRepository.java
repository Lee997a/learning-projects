package com.example.JWTSecurityLogin2.repository;

import com.example.JWTSecurityLogin2.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Boolean existsByEmail(String email);

}
