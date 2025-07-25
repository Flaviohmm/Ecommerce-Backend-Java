package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Converter Entity para DTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setImage(product.getImage());
        dto.setCategory(product.getCategory());
        dto.setRating(product.getRating());
        dto.setInStock(product.getInStock());
        dto.setDescription(product.getDescription());
        dto.setStockQuantity(product.getStockQuantity());
        return dto;
    }

    // Converter DTO para Entity
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setOriginalPrice(dto.getOriginalPrice());
        product.setImage(dto.getImage());
        product.setCategory(dto.getCategory());
        product.setRating(dto.getRating());
        product.setInStock(dto.getInStock());
        product.setDescription(dto.getDescription());
        product.setStockQuantity(dto.getStockQuantity());
        return product;
    }

    // Listar todos os produtos
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Listar produtos em estoque
    public List<ProductDTO> getProductsInStock() {
        return productRepository.findByInStockTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar produto por ID
    public Optional<ProductDTO> getProductByID(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Buscar produtos por categoria
    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndInStockTrue(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar produtos com paginação
    public Page<ProductDTO> getProductsWithPagination(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findByInStockTrue(pageable)
                .map(this::convertToDTO);
    }

    // Buscar produtos com filtros
    public Page<ProductDTO> searchProducts(
            String category,
            Boolean inStock,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String name,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return productRepository.findProductsWithFilters(
                        category, inStock, minPrice, maxPrice, name, pageable)
                .map(this::convertToDTO);
    }

    // Criar produto
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    // Atualizar produto
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setOriginalPrice(productDTO.getOriginalPrice());
            product.setImage(productDTO.getImage());
            product.setCategory(productDTO.getCategory());
            product.setRating(productDTO.getRating());
            product.setInStock(productDTO.getInStock());
            product.setDescription(productDTO.getDescription());
            product.setStockQuantity(productDTO.getStockQuantity());

            Product updatedProduct = productRepository.save(product);
            return convertToDTO(updatedProduct);
        }
        throw new RuntimeException("Produto não encontrado com ID: " + id);
    }

    // Deletar produto
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
    }

    // Atualizar estoque
    public ProductDTO updateStock(Long id, Integer quantity) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setStockQuantity(quantity);
            product.setInStock(quantity > 0);

            Product updatedProduct = productRepository.save(product);
            return convertToDTO(updatedProduct);
        }
        throw new RuntimeException("Produto não encontrado com ID: " + id);
    }
}
