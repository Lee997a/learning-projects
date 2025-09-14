package com.example.JWTSecurityLogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtSecurityLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityLoginApplication.class, args);
	}

}
