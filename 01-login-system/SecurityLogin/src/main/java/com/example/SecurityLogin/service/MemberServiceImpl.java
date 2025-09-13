package com.example.SecurityLogin.service;

import com.example.SecurityLogin.dto.MemberDTO;
import com.example.SecurityLogin.entity.Member;
import com.example.SecurityLogin.entity.Role;
import com.example.SecurityLogin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberDTO memberDTO) {

        // 이메일 중복 체크
        if(memberRepository.existsByEmail(memberDTO.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(memberDTO.getPassword());

        Member member = Member.builder()
                .email(memberDTO.getEmail())
                .password(encodedPassword)
                .nickname(memberDTO.getNickname())
                .phoneNumber(memberDTO.getPhoneNumber())
                .role(Role.USER)
                .build();

        // 저장할 때 자동으로 createdDate, lastModifiedDate가 설정됨.
        memberRepository.save(member);

    }

    @Override
    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}
