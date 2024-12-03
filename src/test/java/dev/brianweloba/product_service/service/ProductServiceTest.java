package dev.brianweloba.product_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import dev.brianweloba.product_service.dao.CreateProductDTO;
import dev.brianweloba.product_service.dao.UpdateProductDTO;
import dev.brianweloba.product_service.model.Product;
import dev.brianweloba.product_service.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private Product product;
	private CreateProductDTO createProductDTO;
	private UpdateProductDTO updateProductDTO;

	@BeforeEach
	void setUp() {
		product = new Product();
		product.setProductName("Test Product");
		product.setDescription("Description");
		product.setPrice(100.0);
		product.setActive(true);
		product.setCreatedBy(UUID.randomUUID());
		product.setUpdatedBy(UUID.randomUUID());
		product.setCreated(new Timestamp(System.currentTimeMillis()));
		product.setUpdated(new Timestamp(System.currentTimeMillis()));
		product.setCurrency(Currency.getInstance(new Locale("en", "KE")));
		product.setThumbnailUrl("http://example.com/thumbnail.jpg");
		product.setImageUrls(Arrays.asList( "http://example.com/image1.jpg", "http://example.com/image2.jpg" ));
		product.setExtn1("ext1");
		product.setExtn2("ext2");
		product.setExtn3("ext3");
		product.setExtn4("ext4");
		product.setExtn5("ext5");
		product.setExtn6("ext6");
		product.setExtn7("ext7");
		product.setExtn8("ext8");

		createProductDTO = new CreateProductDTO();
		createProductDTO.setProductName("New Product");
		createProductDTO.setDescription("New Description");
		createProductDTO.setPrice(200.0);
		createProductDTO.setActive(true);

		updateProductDTO = new UpdateProductDTO();
		updateProductDTO.setProductName("Updated Product");
		updateProductDTO.setDescription("Updated Description");
		updateProductDTO.setPrice(300.0);
		updateProductDTO.setActive(false);
		updateProductDTO.setThumbnailUrl("http://example.com/updated_thumbnail.jpg");
		updateProductDTO.setImageUrls(Arrays.asList( "http://example.com/updated_image1.jpg" ));
		updateProductDTO.setExtn1("updated_ext1");
		updateProductDTO.setExtn2("updated_ext2");
		updateProductDTO.setExtn3("updated_ext3");
		updateProductDTO.setExtn4("updated_ext4");
		updateProductDTO.setExtn5("updated_ext5");
		updateProductDTO.setExtn6("updated_ext6");
		updateProductDTO.setExtn7("updated_ext7");
		updateProductDTO.setExtn8("updated_ext8");
	}

	@Test
	void testGetAllProducts() {
		when(productRepository.findAll()).thenReturn(Arrays.asList(product));
		List<Product> products = productService.getAllProducts();
		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals(product.getId(), products.get(0).getId());
	}

	@Test
	void testGetAllActiveProducts() {
		when(productRepository.findByActiveTrue()).thenReturn(Arrays.asList(product));
		List<Product> products = productService.getAllActiveProducts();
		assertNotNull(products);
		assertEquals(1, products.size());
		assertTrue(products.get(0).isActive());
	}

	@Test
	void testGetAllInactiveProducts() {
		product.setActive(false);
		when(productRepository.findByActiveFalse()).thenReturn(Arrays.asList(product));
		List<Product> products = productService.getAllInactiveProducts();
		assertNotNull(products);
		assertEquals(1, products.size());
		assertFalse(products.get(0).isActive());
	}

	@Test
	void testGetProductById_ValidId() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		Optional<Product> foundProduct = productService.getProductById("1");
		assertTrue(foundProduct.isPresent());
		assertEquals(product.getId(), foundProduct.get().getId());
	}

	@Test
	void testGetProductById_InvalidId() {
		assertThrows(NumberFormatException.class, () -> productService.getProductById("invalid_id"));
	}

	@Test
	void testUpdateProduct_NonExistent() {
		String randomUUID = UUID.randomUUID().toString();
		assertThrows(NullPointerException.class,
				() -> productService.updateProduct("999", updateProductDTO));
	}

	@Test
	void testAddProduct_ValidUUID() {
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
			Product savedProduct = invocation.getArgument(0);
			savedProduct.setId(1L); // Simulating ID generation
			return savedProduct;
		});

		Product savedProduct = productService.addProduct( createProductDTO);

		assertNotNull(savedProduct);
		assertEquals(createProductDTO.getProductName(), savedProduct.getProductName());
		assertEquals(createProductDTO.getDescription(), savedProduct.getDescription());
		assertEquals(createProductDTO.getPrice(), savedProduct.getPrice());
		assertEquals(createProductDTO.isActive(), savedProduct.isActive());
	}

	@Test
	void testUpdateProduct_ValidIdAndUUID() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(productRepository.save(any(Product.class))).thenReturn(product);

		Product updatedProduct = productService.updateProduct("1", updateProductDTO);
		assertNotNull(updatedProduct);
		assertEquals(updateProductDTO.getProductName(), updatedProduct.getProductName());
		assertEquals(updateProductDTO.getDescription(), updatedProduct.getDescription());
		assertEquals(updateProductDTO.getPrice(), updatedProduct.getPrice());
		assertEquals(updateProductDTO.isActive(), updatedProduct.isActive());
		assertEquals(updateProductDTO.getThumbnailUrl(), updatedProduct.getThumbnailUrl());
		assertArrayEquals(updateProductDTO.getImageUrls().toArray(), updatedProduct.getImageUrls().toArray());
		assertEquals(updateProductDTO.getExtn1(), updatedProduct.getExtn1());
		assertEquals(updateProductDTO.getExtn2(), updatedProduct.getExtn2());
		assertEquals(updateProductDTO.getExtn3(), updatedProduct.getExtn3());
		assertEquals(updateProductDTO.getExtn4(), updatedProduct.getExtn4());
		assertEquals(updateProductDTO.getExtn5(), updatedProduct.getExtn5());
		assertEquals(updateProductDTO.getExtn6(), updatedProduct.getExtn6());
		assertEquals(updateProductDTO.getExtn7(), updatedProduct.getExtn7());
		assertEquals(updateProductDTO.getExtn8(), updatedProduct.getExtn8());
	}

	@Test
	void testDeleteById_ValidId() {
		// Arrange
		when(productRepository.findById(1L)).thenReturn(Optional.of(new Product())); // Simulate product exists

		// Act
		productService.deleteById("1");

		// Assert
		verify(productRepository, times(1)).deleteById(1L);
	}

	@Test
	void testDeleteById_InvalidId() {
		assertThrows(NumberFormatException.class, () -> productService.deleteById("invalid_id"));
	}

	@Test
	void testDeleteById_NullID() {
		assertThrows(IllegalArgumentException.class, () -> productService.deleteById(null));
	}

	@Test
	void testDeleteById_EmptyID() {
		assertThrows(IllegalArgumentException.class, () -> productService.deleteById(""));
	}

	@Test
	void testDeleteById_NonExistent() {
		assertThrows(NullPointerException.class, () -> productService.deleteById("9999"));
	}

	@Test
	void testGetPriceValue() {
		String expectedPriceValue = product.getCurrency() + product.getPrice().toString();
		assertEquals(expectedPriceValue, product.getPriceValue());
	}
}
