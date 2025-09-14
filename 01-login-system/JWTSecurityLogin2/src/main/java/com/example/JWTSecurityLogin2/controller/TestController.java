// 1. 테스트용 API 컨트롤러들 추가
package com.example.JWTSecurityLogin2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/user/test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> userTest() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("사용자 API 테스트 성공! 현재 사용자: " + auth.getName());
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminTest() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("관리자 API 테스트 성공! 현재 관리자: " + auth.getName());
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 1234);
        stats.put("activeTokens", 856);
        stats.put("dailyLogins", 342);
        stats.put("securityAlerts", 3);
        stats.put("serverStatus", "HEALTHY");
        stats.put("lastBackup", "2024-01-15 02:30:00");

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/public/info")
    public ResponseEntity<Map<String, String>> getPublicInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("service", "Spring Security JWT Demo");
        info.put("version", "1.0.0");
        info.put("status", "Running");

        return ResponseEntity.ok(info);
    }
}
