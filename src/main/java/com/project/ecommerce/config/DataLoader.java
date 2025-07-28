package com.project.ecommerce.config;

import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Criar usuário admin padrão se não existir.
        if (!userRepository.existsByEmail("admin@ecommerce.com")) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@ecommerce.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);

            userRepository.save(admin);

            System.out.println("=== USUÁRIO ADMIN CRIADO ===");
            System.out.println("Email: admin@ecommerce.com");
            System.out.println("Senha: admin123");
            System.out.println("============================");
        }

        // Criar usuário customer para teste se não existir.
        if (!userRepository.existsByEmail("customer@test.com")) {
            User customer = new User();
            customer.setName("Cliente Teste");
            customer.setEmail("customer@test.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setRole(User.Role.CUSTOMER);

            userRepository.save(customer);

            System.out.println("=== USUÁRIO CUSTOMER CRIADO ===");
            System.out.println("Email: customer@test.com");
            System.out.println("Senha: customer123");
            System.out.println("================================");
        }
    }
}
