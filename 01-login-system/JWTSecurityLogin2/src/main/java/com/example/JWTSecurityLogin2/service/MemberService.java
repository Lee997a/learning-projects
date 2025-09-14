
// 2. Member 엔티티의 Role을 수정하여 첫 회원가입시 관리자로 설정하는 서비스
package com.example.JWTSecurityLogin2.service;

import com.example.JWTSecurityLogin2.dto.SignupRequest;
import com.example.JWTSecurityLogin2.entity.Member;
import com.example.JWTSecurityLogin2.entity.Role;
import com.example.JWTSecurityLogin2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member createMember(SignupRequest signupRequest) {
        // 이미 존재하는 이메일인지 확인
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        Member member = Member.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nickname(signupRequest.getNickname())
                .phoneNumber(signupRequest.getPhoneNumber())
                .role(Role.USER) // 기본값을 USER로 설정
                .build();

        return memberRepository.save(member);
    }
}