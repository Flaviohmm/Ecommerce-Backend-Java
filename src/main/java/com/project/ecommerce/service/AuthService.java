package com.project.ecommerce.service;

import com.project.ecommerce.dto.*;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest loginRequest) {
        System.out.println("Tentativa de login para: " + loginRequest.getEmail());

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            System.out.println("Usuário não encontrado: " + loginRequest.getEmail());
            throw new RuntimeException("Usuário não encontrado");
        }

        User user = userOptional.get();
        System.out.println("Usuário encontrado: " + user.getEmail() + ", Role: " + user.getRole());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Senha incorreta para: " + loginRequest.getEmail());
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());
        System.out.println("Token gerado com sucesso para: " + user.getEmail());
        System.out.println("Token: " + token.substring(0, 50) + "...");

        UserDTO userDTO = convertToUserDTO(user);

        return new AuthResponse(token, userDTO);
    }

    public AuthResponse loginAdmin(LoginRequest loginRequest) {
        System.out.println("Tentativa de login admin para: " + loginRequest.getEmail());

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            System.out.println("Usuário admin não encontrado: " + loginRequest.getEmail());
            throw new RuntimeException("Usuário não encontrado");
        }

        User user = userOptional.get();

        if (!user.isAdmin()) {
            System.out.println("Usuário não é admin: " + loginRequest.getEmail());
            throw new RuntimeException("Acesso negado. Apenas administradores podem fazer login admin.");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Senha incorreta para admin: " + loginRequest.getEmail());
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());
        System.out.println("Token admin gerado com sucesso para: " + user.getEmail());

        UserDTO userDTO = convertToUserDTO(user);

        return new AuthResponse(token, userDTO);
    }

    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(User.Role.CUSTOMER); // Por padrão, novos usuários são customers

        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public boolean isAdmin(String email) {
        User user = getUserByEmail(email);
        return user.isAdmin();
    }

    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId().toString(),
                user.getEmail(),
                user.getName(),
                user.getRole().toString().toLowerCase()
        );
    }
}
