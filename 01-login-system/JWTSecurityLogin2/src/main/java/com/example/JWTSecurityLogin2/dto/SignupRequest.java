package com.example.JWTSecurityLogin2.dto;

import lombok.Data;

@Data
public class SignupRequest {

    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
}
