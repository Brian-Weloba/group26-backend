package dev.brianweloba.product_service.model;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTest {

    @Test
    void testConstructorAndGettersSetters() {
        // Given
        UUID createdBy = UUID.randomUUID();
        UUID updatedBy = UUID.randomUUID();
        String productName = "Test Product";
        Double price = 100.0;
        String description = "Test Description";
        Timestamp created = new Timestamp(System.currentTimeMillis());
        Timestamp updated = new Timestamp(System.currentTimeMillis());
        boolean active = true;
        String thumbnailUrl = "test-thumbnail.jpg";
        String[] images = { "image1.jpg", "image2.jpg" };
        Currency currency = Currency.getInstance(new Locale("en", "KE"));

        // When
        Product product = new Product();
        product.setId(1L);
        product.setCreatedBy(createdBy);
        product.setUpdatedBy(updatedBy);
        product.setProductName(productName);
        product.setPrice(price);
        product.setDescription(description);
        product.setCreated(created);
        product.setUpdated(updated);
        product.setActive(active);
        product.setThumbnailUrl(thumbnailUrl);
        product.setImageUrls(Arrays.asList(images));
        product.setCurrency(currency);

        // Then
        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getCreatedBy()).isEqualTo(createdBy);
        assertThat(product.getUpdatedBy()).isEqualTo(updatedBy);
        assertThat(product.getProductName()).isEqualTo(productName);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getCreated()).isEqualTo(created);
        assertThat(product.getUpdated()).isEqualTo(updated);
        assertThat(product.isActive()).isEqualTo(active);
        assertThat(product.getThumbnailUrl()).isEqualTo(thumbnailUrl);
        assertThat(product.getImageUrls()).containsExactly("image1.jpg", "image2.jpg");
        assertThat(product.getCurrency()).isEqualTo(currency);
    }

    @Test
    void testPriceValueCalculation() {
        // Given
        Product product = new Product();
        product.setCurrency(Currency.getInstance("USD"));
        product.setPrice(50.0);

        // When
        String priceValue = product.getPriceValue();

        // Then
        assertThat(priceValue).isEqualTo("USD50.0");
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        Product product1 = new Product();
        product1.setId(1L);

        Product product2 = new Product();
        product2.setId(1L);

        Product product3 = new Product();
        product3.setId(2L);

        // Then
        assertThat(product1).isEqualTo(product2);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertThat(product1).isNotEqualTo(product3);
        assertThat(product1.hashCode()).isNotEqualTo(product3.hashCode());
    }

    @Test
    void testImagesNull() {
        // Given
        Product product = new Product();
        product.setImageUrls(null);

        // Then
        assertThat(product.getImageUrls()).isNull();
    }

    @Test
    void testImagesEmptyArray() {
        // Given
        Product product = new Product();
        product.setImageUrls(Arrays.asList());

        // Then
        assertThat(product.getImageUrls()).isEmpty();
    }
}
