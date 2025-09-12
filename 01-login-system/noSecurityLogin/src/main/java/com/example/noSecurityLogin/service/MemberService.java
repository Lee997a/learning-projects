package com.example.noSecurityLogin.service;

import com.example.noSecurityLogin.dto.MemberDTO;
import com.example.noSecurityLogin.entity.Member;

public interface MemberService {


    void join(MemberDTO memberDTO);

    Member login(MemberDTO memberDTO);

    Member findByEmail(String email);


}
