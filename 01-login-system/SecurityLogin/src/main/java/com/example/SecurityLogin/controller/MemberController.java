package com.example.SecurityLogin.controller;

import com.example.SecurityLogin.dto.MemberDTO;
import com.example.SecurityLogin.entity.Member;
import com.example.SecurityLogin.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // http://localhost:8080
    @GetMapping("/")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("annoymousUser")){
            Member member = (Member) authentication.getPrincipal();
            model.addAttribute("member", member);
        }

        return "home";
    }

    // 회원가입 페이지
    // http://localhost:8080/join
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "join";
    }


    @PostMapping("/join")
    public String join(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "join";
        }

        try {
            memberService.join(memberDTO);
            return "redirect:/login?joined=true";
        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "join";
        }
    }

    // http://localhost:8080/login
    @GetMapping("/login")
    public String loginForm(Model model){
        return "login";
    }

    // http://localhost:8080/myPage
    @GetMapping("/mypage")
    public String myPage(Authentication authentication, Model model){
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);
        return "mypage";
    }
}
