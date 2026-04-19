package com.library.library_management_system.controller;

import com.library.library_management_system.entity.Member;
import com.library.library_management_system.security.JwtUtil;
import com.library.library_management_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        return authService.authenticate(username, password)
                .map(member -> {
                    String token = jwtUtil.generateToken(member.getUsername());
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("username", member.getUsername());
                    response.put("role", member.getRole());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Member member) {
        boolean registered = authService.register(member);
        return registered ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Username or email already exists");
    }
}
