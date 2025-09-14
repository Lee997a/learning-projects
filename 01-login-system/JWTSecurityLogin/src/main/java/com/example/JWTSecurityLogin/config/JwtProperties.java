package com.example.JWTSecurityLogin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
    JWT 관련 설정값들을 관리하는 설정 클래스
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret = "myVerySecureAndLongSecretKeyForJWTTokenGenerationThatIsAtLeast512BitsLong1234567890ABCDEF";
    private long expiration = 86400000; // 24시간 (밀리초)


    public String getSecret(){return secret;}
    public void setSecret(String secret){this.secret = secret;}
    public long getExpiration(){return expiration;}
    public void setExpiration(long expiration){this.expiration = expiration;}
}
