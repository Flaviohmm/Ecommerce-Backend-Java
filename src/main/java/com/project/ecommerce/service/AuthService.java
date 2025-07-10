package com.project.ecommerce.service;

import com.project.ecommerce.dto.AuthResponse;
import com.project.ecommerce.dto.LoginRequest;
import com.project.ecommerce.dto.RegisterRequest;
import com.project.ecommerce.dto.UserResponse;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public void register(RegisterRequest registerRequest) {
        // Verificar se o e-mail já está em uso.
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        // Criar novo usuário
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        // Buscar usuário pelo e-mail
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        // Verificar senha
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        // Gerar token JWT
        String token = jwtTokenProvider.generateToken(user.getEmail());

        // Criar resposta
        UserResponse userResponse = new UserResponse(
                user.getId().toString(),
                user.getName(),
                user.getEmail()
        );

        return new AuthResponse(token, userResponse);
    }
}
