package com.project.ecommerce.aspect;

import com.project.ecommerce.security.JwtUtil;
import com.project.ecommerce.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AdminAuthorizationAspect {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Before("@annotation(com.project.ecommerce.annotation.AdminOnly)")
    public void checkAdminAccess() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token de acesso não fornecido");
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Token inválido");
        }

        String email = jwtUtil.getEmailFromToken(token);

        if (!authService.isAdmin(email)) {
            throw new RuntimeException("Acesso negado. Apenas administradores podem executar esta ação.");
        }
    }
}
