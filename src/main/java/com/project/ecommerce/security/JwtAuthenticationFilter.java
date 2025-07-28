package com.project.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        System.out.println("\n🔍 === PROCESSANDO REQUISIÇÃO ===");
        System.out.println("📋 Método: " + method);
        System.out.println("🌐 URI: " + uri);
        System.out.println("🏠 Origin: " + request.getHeader("Origin"));

        try {
            String token = extractTokenFromRequest(request);

            if (token != null) {
                System.out.println("🎫 Token encontrado, processando autenticação...");
                authenticateUser(token, request);
            } else {
                System.out.println("❌ Nenhum token JWT encontrado no header Authorization");
            }

        } catch (Exception e) {
            System.err.println("❌ ERRO durante autenticação JWT: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        System.out.println("================================\n");
        filterChain.doFilter(request, response);
    }

    /**
     * Extrair token do header Authorization
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        System.out.println("🔑 Authorization Header: " + (authHeader != null ?
                "Bearer " + authHeader.substring(7, Math.min(authHeader.length(), 27)) + "..." : "null"));

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    /**
     * Processar autenticação do usuário
     */
    private void authenticateUser(String token, HttpServletRequest request) {
        try {
            System.out.println("🔍 Iniciando validação do token...");

            // Debug do token para identificar problemas
            if (System.getProperty("jwt.debug", "true").equals("true")) {
                jwtUtil.debugToken(token);
            }

            // Validar token primeiro
            if (!jwtUtil.validateToken(token)) {
                System.err.println("❌ Token JWT inválido ou expirado");
                return;
            }

            System.out.println("✅ Token válido, extraindo informações...");

            // Extrair informações do usuário
            String email = jwtUtil.getEmailFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);

            System.out.println("📧 Email extraído: " + email);
            System.out.println("👤 Role extraída: " + role);

            if (email == null || role == null) {
                System.err.println("❌ Email ou role não encontrados no token");
                return;
            }

            // Verificar se já existe autenticação no contexto
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("ℹ️  Usuário já autenticado no contexto");
                return;
            }

            // Criar token de autenticação do Spring Security
            String roleWithPrefix = "ROLE_" + role.toUpperCase();
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix))
                    );

            // Adicionar detalhes da requisição
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Definir no contexto de segurança.
            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("🎉 SUCESSO! Usuário autenticado:");
            System.out.println("   📧 Email: " + email);
            System.out.println("   👤 Role: " + roleWithPrefix);
            System.out.println("   🔐 Authorities: " + authToken.getAuthorities());

        } catch (Exception e) {
            System.err.println("❌ ERRO durante processamento do token:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensagem: " + e.getMessage());

            // Limpar contexto em caso de erro.
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * Determinar quais requisições devem pular o filtro.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Endpoints que não precisam de autenticação.
        boolean isPublicEndpoint = path.startsWith("/api/auth/") ||
                path.startsWith("/api/public/") ||
                path.equals("/") ||
                path.startsWith("/static/") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/images/") ||
                path.startsWith("/favicon.ico") ||
                path.startsWith("/actuator/health") ||
                path.equals("/error");

        // Permitir OPTIONS para CORS preflight
        boolean isOptionsRequest = "OPTIONS".equalsIgnoreCase(method);

        boolean shouldSkip = isPublicEndpoint || isOptionsRequest;

        if (shouldSkip) {
            System.out.println("⏭️  PULANDO filtro JWT para: " + method + " " + path);
        }

        return shouldSkip;
    }
}