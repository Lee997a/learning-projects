package com.example.JWTSecurityLogin2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtSecurityLogin2Application {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityLogin2Application.class, args);
	}

}
