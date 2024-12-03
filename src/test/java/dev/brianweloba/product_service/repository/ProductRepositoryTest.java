package dev.brianweloba.product_service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.brianweloba.product_service.model.Product;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product activeProduct;
    private Product inactiveProduct;

    @BeforeEach
    void setUp() {
        activeProduct = new Product();
        activeProduct.setProductName("Active Product");
        activeProduct.setDescription("Active Description");
        activeProduct.setPrice(100.0);
        activeProduct.setActive(true);
        activeProduct.setCreatedBy(UUID.randomUUID());
        activeProduct.setUpdatedBy(UUID.randomUUID());
        activeProduct.setCreated(new Timestamp(System.currentTimeMillis()));
        activeProduct.setUpdated(new Timestamp(System.currentTimeMillis()));

        inactiveProduct = new Product();
        inactiveProduct.setProductName("Inactive Product");
        inactiveProduct.setDescription("Inactive Description");
        inactiveProduct.setPrice(200.0);
        inactiveProduct.setActive(false);
        inactiveProduct.setCreatedBy(UUID.randomUUID());
        inactiveProduct.setUpdatedBy(UUID.randomUUID());
        inactiveProduct.setCreated(new Timestamp(System.currentTimeMillis()));
        inactiveProduct.setUpdated(new Timestamp(System.currentTimeMillis()));

        productRepository.save(activeProduct);
        productRepository.save(inactiveProduct);
    }

    @Test
    void testFindByActiveTrue() {
        List<Product> activeProducts = productRepository.findByActiveTrue();
        assertThat(activeProducts).hasSize(1);
        assertThat(activeProducts.get(0).getProductName()).isEqualTo("Active Product");
    }

    @Test
    void testFindByActiveFalse() {
        List<Product> inactiveProducts = productRepository.findByActiveFalse();
        assertThat(inactiveProducts).hasSize(1);
        assertThat(inactiveProducts.get(0).getProductName()).isEqualTo("Inactive Product");
    }
}
