package com.example.JWTSecurityLogin.controller;

import com.example.JWTSecurityLogin.dto.JwtResponse;
import com.example.JWTSecurityLogin.dto.LoginRequest;
import com.example.JWTSecurityLogin.dto.SignupRequest;
import com.example.JWTSecurityLogin.entity.Member;
import com.example.JWTSecurityLogin.service.MemberService;
import com.example.JWTSecurityLogin.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenUtil.generateToken(userDetails);

            Member member = (Member) userDetails;
            return ResponseEntity.ok(new JwtResponse(jwt, member.getEmail(), member.getNickname()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("로그인에 실패했습니다." + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest){
        try {
            Member member = memberService.createMember(signupRequest);
            return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
