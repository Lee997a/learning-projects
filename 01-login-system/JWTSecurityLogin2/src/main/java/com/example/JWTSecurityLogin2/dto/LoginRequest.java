package com.example.JWTSecurityLogin2.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
