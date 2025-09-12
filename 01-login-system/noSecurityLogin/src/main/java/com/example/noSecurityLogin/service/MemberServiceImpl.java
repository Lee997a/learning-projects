package com.example.noSecurityLogin.service;

import com.example.noSecurityLogin.dto.MemberDTO;
import com.example.noSecurityLogin.entity.Member;
import com.example.noSecurityLogin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public void join(MemberDTO memberDTO) {
        // 이메일 중복 체크
        if(memberRepository.existsByEmail(memberDTO.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // DTO -> Entity 변환
        Member member = Member.builder()
                .email(memberDTO.getEmail())
                .password(memberDTO.getPassword())
                .nickname(memberDTO.getNickname())
                .phoneNumber(memberDTO.getPhoneNumber())
                .build();

        memberRepository.save(member);
    }

    @Override
    public Member login(MemberDTO memberDTO) {
        // 이메일로 회원 찾기
        Member member = memberRepository.findByEmail(memberDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 비밀번호 검증
        if(!member.getPassword().equals(member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}
