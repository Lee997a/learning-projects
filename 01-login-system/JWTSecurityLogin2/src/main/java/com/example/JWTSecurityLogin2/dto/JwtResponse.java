package com.example.JWTSecurityLogin2.dto;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String email;
    private String nickname;

    public JwtResponse(String accessToken, String email, String nickname){
        this.token = accessToken;
        this.email = email;
        this.nickname = nickname;
    }
}
