package com.example.noSecurityLogin.controller;

import com.example.noSecurityLogin.dto.MemberDTO;
import com.example.noSecurityLogin.entity.Member;
import com.example.noSecurityLogin.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String joinForm(){
        return "join";
    }

    @PostMapping("/join")
    public String join(MemberDTO memberDTO, Model model){
        try {
            memberService.join(memberDTO);
            return "redirect:/login";
        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "join";
        }
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("/login")
    public String login(MemberDTO memberDTO, HttpSession session, Model model){
        try {
            Member member = memberService.login(memberDTO);
            session.setAttribute("loginMember", member);
            return "redirect:/";
        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model){
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember != null){
            model.addAttribute("member", loginMember);
        }
        return "home";
    }
}
