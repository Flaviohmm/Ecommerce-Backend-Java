package com.project.ecommerce.controller;

import com.project.ecommerce.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProductController {

    @GetMapping("/products")
    public ResponseEntity<?> getProducts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        // Simulação de produtos
        List<Map<String, Object>> products = Arrays.asList(
                createProduct(1L, "Smartphone", "Smartphone Android", 899.99),
                createProduct(2L, "Notebook", "Notebook Bluetooth", 2499.99),
                createProduct(3L, "Headphone", "Headphone Bluetooth", 199.99),
                createProduct(4L, "Mouse", "Mouse Wireless", 49.99),
                createProduct(5L, "Teclado", "Teclado Mecânico", 299.99)
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bem-vindo, " + user.getName() + "!");
        response.put("products", products);
        response.put("user", Map.of(
                "id", user.getId().toString(),
                "name", user.getName(),
                "email", user.getEmail()
        ));

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> createProduct(Long id, String name, String description, Double price) {
        Map<String, Object> product = new HashMap<>();
        product.put("id", id);
        product.put("name", name);
        product.put("description", description);
        product.put("price", price);
        return product;
    }
}
