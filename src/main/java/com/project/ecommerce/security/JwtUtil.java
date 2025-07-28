package com.project.ecommerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Chave secreta robusta — IMPORTANTE: Use uma chave de pelo menos 64 caracteres
    @Value("${jwt.secret:myVerySecureSecretKeyForJWTTokenGenerationThatIsAtLeast512BitsLongToEnsureSecurityAndCompatibilityWithHS512Algorithm2024}")
    private String secret;

    @Value("${jwt.expiration:86400}") // 24 horas em segundos
    private int jwtExpiration;

    private SecretKey getSigningKey() {
        // Garantir que a chave seja robusta o suficiente.
        String finalSecret = secret.length() >= 64 ? secret :
                "myVerySecureSecretKeyForJWTTokenGenerationThatIsAtLeast512BitsLongToEnsureSecurityAndCompatibilityWithHS512Algorithm2024";

        return Keys.hmacShaKeyFor(finalSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    /**
     * Gerar token JWT
     */
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("iss", "ecommerce-app");
        return createToken(claims, email);
    }

    /**
     * Criar token com claims
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + (jwtExpiration * 1000L));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extrair email do token
     */
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extrair role do token
     */
    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    /**
     * Extrair data de expiração
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extrair qualquer claim do token
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrair todas as claims do token - VERSÃO COMPATÍVEL
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            // IMPORTANTE: Use parserBuilder() para versões mais recentes
            JwtParserBuilder parserBuilder = Jwts.parserBuilder();

            // Definir a chave de assinatura
            parserBuilder.setSigningKey(getSigningKey());

            // Criar o parser
            JwtParser parser = parserBuilder.build();

            // Parsear o token
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);

            return claimsJws.getBody();

        } catch (ExpiredJwtException e) {
            System.err.println("❌ Token expirado: " + e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            System.err.println("❌ Token não suportado: " + e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            System.err.println("❌ Token malformado: " + e.getMessage());
            throw e;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            System.err.println("❌ Assinatura inválida: " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Token vazio: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado ao processar token: " + e.getMessage());
            throw new RuntimeException("Erro ao processar token JWT", e);
        }
    }

    /**
     * Verificar se token expirou
     */
    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            System.err.println("Erro ao verificar expiração: " + e.getMessage());
            return true;
        }
    }

    /**
     * Validar token JWT
     */
    public Boolean validateToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                System.err.println("❌ Token vazio ou nulo");
                return false;
            }

            // Tentar parsear o token
            JwtParserBuilder parserBuilder = Jwts.parserBuilder();
            parserBuilder.setSigningKey(getSigningKey());
            JwtParser parser = parserBuilder.build();

            // Se conseguir parsear sem exceção, o token é estruturalmente válido
            parser.parseClaimsJws(token);

            // Verificar se não expirou
            boolean expired = isTokenExpired(token);
            if (expired) {
                System.err.println("❌ Token válido mas expirado");
                return false;
            }

            System.out.println("✅ Token JWT válido");
            return true;

        } catch (ExpiredJwtException e) {
            System.err.println("❌ Token expirado durante validação");
            return false;
        } catch (UnsupportedJwtException e) {
            System.err.println("❌ Token não suportado durante validação");
            return false;
        } catch (MalformedJwtException e) {
            System.err.println("❌ Token malformado durante validação");
            return false;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            System.err.println("❌ Assinatura inválida durante validação");
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Argumento inválido durante validação");
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado durante validação: " + e.getMessage());
            return false;
        }
    }

    /**
     * Debug do token — informações detalhadas
     */
    public void debugToken(String token) {
        try {
            System.out.println("\n=== 🔍 DEBUG TOKEN JWT ===");
            System.out.println("📄 Token (primeiros 50 chars): " + token.substring(0, Math.min(50, token.length())) + "...");

            Claims claims = getAllClaimsFromToken(token);
            System.out.println("📧 Subject (email): " + claims.getSubject());
            System.out.println("👤 Role: " + claims.get("role"));
            System.out.println("🏢 Issuer: " + claims.get("iss"));
            System.out.println("📅 Issued At: " + claims.getIssuedAt());
            System.out.println("⏰ Expiration: " + claims.getExpiration());
            System.out.println("❓ Is Expired: " + isTokenExpired(token));
            System.out.println("✅ Is Valid: " + validateToken(token));
            System.out.println("🔑 Signing Key Length: " + getSigningKey().getEncoded().length + " bytes");
            System.out.println("==========================\n");
        } catch (Exception e) {
            System.err.println("❌ Erro ao fazer debug do token: " + e.getMessage());
        }
    }

    /**
     * Método para testar a geração e validação de token
     */
    public void testTokenGeneration() {
        try {
            System.out.println("\n=== 🧪 TESTE DE TOKEN ===");

            // Gerar token de teste
            String testToken = generateToken("admin@ecommerce.com", "ADMIN");
            System.out.println("🎫 Token gerado: " + testToken.substring(0, 50) + "...");

            // Testar validação
            boolean isValid = validateToken(testToken);
            System.out.println("✅ Token válido: " + isValid);

            if (isValid) {
                String email = getEmailFromToken(testToken);
                String role = getRoleFromToken(testToken);
                System.out.println("📧 Email extraído: " + email);
                System.out.println("👤 Role extraída: " + role);
            }

            System.out.println("========================\n");
        } catch (Exception e) {
            System.err.println("❌ Erro no teste de token: " + e.getMessage());
        }
    }
}