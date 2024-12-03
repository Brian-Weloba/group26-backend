package dev.brianweloba.product_service.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.brianweloba.product_service.dao.ProductWithReviewDTO;
import org.springframework.stereotype.Service;

import dev.brianweloba.product_service.dao.CreateProductDTO;
import dev.brianweloba.product_service.dao.UpdateProductDTO;
import dev.brianweloba.product_service.model.Product;
import dev.brianweloba.product_service.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BlockchainService blockchainService;

    public ProductService(ProductRepository productRepository,BlockchainService blockchainService) {
        this.productRepository = productRepository;
        this.blockchainService = blockchainService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> getAllInactiveProducts() {
        return productRepository.findByActiveFalse();
    }

    public List<Product> getAllByStatus(boolean status) {
        return status ? getAllActiveProducts() : getAllInactiveProducts();
    }

    public Optional<Product> getProductById(String idString) {
        try {
            Long id = Long.parseLong(idString);
            return productRepository.findById(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid id format: " + idString + ". Please provide a valid numeric id.");
        }
    }

    public Product addProduct( CreateProductDTO productDTO) {
            UUID userUuid = UUID.randomUUID();
            Product product = new Product();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setActive(productDTO.isActive());
            product.setImageUrls(productDTO.getImageUrls());

            product.setCreatedBy(userUuid);
            product.setUpdatedBy(userUuid);

            product.setCreated(currentTime);
            product.setUpdated(currentTime);

            return productRepository.save(product);

    }

    public Product updateProduct(String productId, UpdateProductDTO productDTO) {

        // Retrieve the product from repository
        Optional<Product> productOptional = getProductById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Update fields from DTO
            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setActive(productDTO.isActive());
            product.setThumbnailUrl(productDTO.getThumbnailUrl());
            product.setImageUrls(productDTO.getImageUrls());
            product.setExtn1(productDTO.getExtn1());
            product.setExtn2(productDTO.getExtn2());
            product.setExtn3(productDTO.getExtn3());
            product.setExtn4(productDTO.getExtn4());
            product.setExtn5(productDTO.getExtn5());
            product.setExtn6(productDTO.getExtn6());
            product.setExtn7(productDTO.getExtn7());
            product.setExtn8(productDTO.getExtn8());

            // Update timestamps and UUIDs
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            product.setUpdated(currentTime);
            product.setUpdatedBy(UUID.randomUUID());

            // Save and return the updated product
            return productRepository.save(product);
        } else {
            throw new NullPointerException("Product not found with ID: " + productId);
        }
    }

    public String deleteById(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Product Id cannot be null or empty.");
        }

        Long id;
        try {
            id = Long.parseLong(productId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    "Invalid id format: " + productId + ". Please provide a valid numeric id.");
        }

        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            productRepository.deleteById(id);
            return "Product with id " + productId + " deleted successfully.";
        } else {
            throw new NullPointerException("Product with id " + id + " does not exist.");
        }

    }

    public ProductWithReviewDTO getProductWithReviews(String productId) {
        Optional<Product> productOptional = getProductById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();


            ProductWithReviewDTO productWithReviewDTO = new ProductWithReviewDTO();
            productWithReviewDTO.setId(product.getId());
            productWithReviewDTO.setProductName(product.getProductName());
            productWithReviewDTO.setDescription(product.getDescription());
            productWithReviewDTO.setPrice(product.getPrice());
            productWithReviewDTO.setActive(product.isActive());
            productWithReviewDTO.setImageUrls(product.getImageUrls());

            productWithReviewDTO.setReviews(blockchainService.findReviewsByProductId(product.getId().toString()));
            productWithReviewDTO.setAverageRating(blockchainService.getAverageRatingByProductId(product.getId().toString()));
            return productWithReviewDTO;
        } else {
            throw new NullPointerException("Product with id " + productId + " does not exist.");
        }
    }

    public List<ProductWithReviewDTO> getAllProductsWithReviews() {
        List<Product> products = getAllProducts();
        return products.stream()
                .map(product -> getProductWithReviews(product.getId().toString()))
                .toList();
    }

}
