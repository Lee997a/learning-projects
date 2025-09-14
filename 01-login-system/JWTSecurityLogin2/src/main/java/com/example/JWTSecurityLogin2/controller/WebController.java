package com.example.JWTSecurityLogin2.controller;

import com.example.JWTSecurityLogin2.dto.LoginRequest;
import com.example.JWTSecurityLogin2.dto.SignupRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    // 메인 페이지
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "로그인에 실패했습니다.");
        }
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "auth/signup";
    }

    // 사용자 대시보드
    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        // JWT 토큰에서 사용자 정보를 가져와서 모델에 추가
        return "user/dashboard";
    }

    // 관리자 대시보드
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        return "admin/dashboard";
    }
}