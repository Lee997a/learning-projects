package com.example.SecurityLogin.service;


import com.example.SecurityLogin.dto.MemberDTO;
import com.example.SecurityLogin.entity.Member;

public interface MemberService {
    void join(MemberDTO memberDTO);
    Member findByEmail(String email);
}
