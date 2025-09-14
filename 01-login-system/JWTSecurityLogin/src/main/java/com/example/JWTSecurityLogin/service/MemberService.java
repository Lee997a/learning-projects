package com.example.JWTSecurityLogin.service;

import com.example.JWTSecurityLogin.dto.SignupRequest;
import com.example.JWTSecurityLogin.entity.Member;
import com.example.JWTSecurityLogin.entity.Role;
import com.example.JWTSecurityLogin.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member createMember(SignupRequest signupRequest){
        // 이미 존재하는 이메일인지 확인
        if(memberRepository.existsByEmail(signupRequest.getEmail())){
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        Member member = Member.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nickname(signupRequest.getNickname())
                .phoneNumber(signupRequest.getPhoneNumber())
                .role(Role.USER)
                .build();

        return memberRepository.save(member);
    }
}
