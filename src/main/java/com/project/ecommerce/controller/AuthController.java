package com.project.ecommerce.controller;

import com.project.ecommerce.dto.AuthResponse;
import com.project.ecommerce.dto.LoginRequest;
import com.project.ecommerce.dto.RegisterRequest;
import com.project.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            System.out.println("Login bem-sucedido para: " + loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro no login: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/login-admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.loginAdmin(loginRequest);
            System.out.println("Login admin bem-sucedido para: " + loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro no login admin: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);
            System.out.println("Usuário registrado: " + registerRequest.getEmail());
            return ResponseEntity.ok("Usuário registrado com sucesso");
        } catch (Exception e) {
            System.err.println("Erro no registro: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}
