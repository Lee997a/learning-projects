package com.example.JWTSecurityLogin2.controller;

import com.example.JWTSecurityLogin2.dto.LoginRequest;
import com.example.JWTSecurityLogin2.dto.SignupRequest;
import com.example.JWTSecurityLogin2.entity.Member;
import com.example.JWTSecurityLogin2.service.MemberService;
import com.example.JWTSecurityLogin2.util.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class WebAuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public String authenticateUser(@ModelAttribute LoginRequest loginRequest,
                                   HttpServletResponse response,
                                   RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenUtil.generateToken(userDetails);

            // JWT를 쿠키에 저장 (HttpOnly로 보안 강화)
            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // HTTPS에서는 true로 설정
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 24시간
            response.addCookie(jwtCookie);

            Member member = (Member) userDetails;

            // 권한에 따라 리다이렉트
            if (member.getRole().name().equals("ADMIN")) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/dashboard";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "로그인에 실패했습니다: " + e.getMessage());
            return "redirect:/login?error";
        }
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute SignupRequest signupRequest,
                               RedirectAttributes redirectAttributes) {
        try {
            Member member = memberService.createMember(signupRequest);
            redirectAttributes.addFlashAttribute("success", "회원가입이 성공적으로 완료되었습니다!");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // JWT 쿠키 삭제
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return "redirect:/";
    }
}