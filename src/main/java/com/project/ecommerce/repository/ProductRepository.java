package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Buscar produtos por categoria
    List<Product> findByCategory(String category);

    // Buscar produtos em estoque
    List<Product> findByInStockTrue();

    // Buscar produtos por nome (case insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Buscar produtos por faixa de preço
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Buscar produtos por categoria e em estoque
    List<Product> findByCategoryAndInStockTrue(String category);

    // Buscar produtos com desconto
    List<Product> findByOriginalPriceIsNotNull();

    // Buscar produto por rating mínimo
    List<Product> findByRatingGreaterThanEqual(Double rating);

    // Buscar produtos com paginação
    Page<Product> findByInStockTrue(Pageable pageable);

    // Query customizada para busca avançada
    @Query("SELECT p FROM Product p WHERE " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:inStock IS NULL OR p.inStock = :inStock) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Product> findProductsWithFilters(
            @Param("category") String category,
            @Param("inStock") Boolean inStock,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("name") String name,
            Pageable pageable
    );
}
