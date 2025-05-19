package com.bank.banksystem.controller;

import com.bank.banksystem.security.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(jwtUtil.generateToken(request.getUsername()));
    }

    @Data
    public class AuthRequest {
        private String username;
    }

}

