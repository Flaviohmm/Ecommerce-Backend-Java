package com.project.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Endpoint público - sem autenticação necessária");
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Acesso autorizado!");
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> adminEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Acesso admin autorizado!");
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}
