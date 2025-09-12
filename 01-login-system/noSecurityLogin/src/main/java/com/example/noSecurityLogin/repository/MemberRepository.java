package com.example.noSecurityLogin.repository;

import com.example.noSecurityLogin.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //
    Optional<Member> findByEmail(String email);

    //
    boolean existsByEmail(String email);
}
