package com.project.ecommerce.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 255, message = "Nome deve ter entre 2 e 255 caracteres")
    private String name;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal price;

    @DecimalMin(value = "0.01", message = "Preço original deve ser maior que zero")
    private BigDecimal originalPrice;

    @NotBlank(message = "URL da imagem é obrigatória")
    private String image;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(min = 2, max = 100, message = "Categoria deve ter entre 2 e 100 caracteres")
    private String category;

    @DecimalMin(value = "0.0", message = "Rating deve ser maior ou igual a 0")
    @DecimalMax(value = "5.0", message = "Rating deve ser menor ou igual a 5")
    private Double rating = 0.0;

    private Boolean inStock = true;

    private String description;

    @Min(value = 0, message = "Quantidade em estoque deve ser maior ou igual a 0")
    private Integer stockQuantity = 0;

    // Constructors

    public ProductDTO() {}

    public ProductDTO(String name, BigDecimal price, String image, String category, String description) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
        this.description = description;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
