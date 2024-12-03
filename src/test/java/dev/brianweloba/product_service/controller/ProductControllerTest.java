package dev.brianweloba.product_service.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import dev.brianweloba.product_service.config.ResourceServerConfig;
import dev.brianweloba.product_service.dao.CreateProductDTO;
import dev.brianweloba.product_service.dao.UpdateProductDTO;
import dev.brianweloba.product_service.model.Product;
import dev.brianweloba.product_service.service.ProductService;
import dev.brianweloba.product_service.util.BooleanValidationUtils;


@WebMvcTest(ProductController.class)
@Import(ResourceServerConfig.class)
class ProductControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private WebApplicationContext context;

        @MockBean
        private ProductService productService;
        @MockBean
        private BooleanValidationUtils booleanValidationUtils;
        private String userId;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                .build();
                userId = UUID.randomUUID().toString();
        }

        private Product createProduct(Long id, String name, Double price, boolean active) {
                Product product = new Product();
                product.setId(id);
                product.setCreatedBy(UUID.randomUUID());
                product.setUpdatedBy(UUID.randomUUID());
                product.setProductName(name);
                product.setPrice(price);
                product.setCurrency(Currency.getInstance(new Locale("en", "KE")));
                product.setDescription("Description for " + name);
                product.setCreated(new Timestamp(System.currentTimeMillis()));
                product.setUpdated(new Timestamp(System.currentTimeMillis()));
                product.setActive(active);
                product.setThumbnailUrl("http://example.com/image.png");
                List<String> imageUrls = new ArrayList<>();
                imageUrls.add("http://example.com/image1.png");
                imageUrls.add("http://example.com/image2.png");
                product.setImageUrls(imageUrls)
;
                return product;
        }

        @Test
        void testGetAllProducts() throws Exception {
                Product product1 = createProduct(1L, "Product1", 100.0, true);
                Product product2 = createProduct(2L, "Product2", 200.0, false);

                given(productService.getAllProducts()).willReturn(Arrays.asList(product1, product2));

                mockMvc.perform(get("/api/v1/products"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id", is(product1.getId().intValue())))
                                .andExpect(jsonPath("$[0].productName", is(product1.getProductName())))
                                .andExpect(jsonPath("$[0].price", is(product1.getPrice())))
                                .andExpect(jsonPath("$[1].id", is(product2.getId().intValue())))
                                .andExpect(jsonPath("$[1].productName", is(product2.getProductName())))
                                .andExpect(jsonPath("$[1].price", is(product2.getPrice())));
        }

        @Test
        void testGetProductById() throws Exception {
                Product product = createProduct(1L, "Product1", 100.0, true);

                given(productService.getProductById("1")).willReturn(Optional.of(product));

                mockMvc.perform(get("/api/v1/products/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                                .andExpect(jsonPath("$.productName", is(product.getProductName())))
                                .andExpect(jsonPath("$.price", is(product.getPrice())));
        }

        @Test
        void testGetProductByIdNotFound() throws Exception {
                given(productService.getProductById("1")).willReturn(Optional.empty());

                mockMvc.perform(get("/api/v1/products/1"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testGetMethodNameWithValidStatus() throws Exception {
                Product product = createProduct(1L, "Product1", 100.0, true);

                given(booleanValidationUtils.isValidBoolean("true")).willReturn(true);
                given(booleanValidationUtils.parseBoolean("true")).willReturn(true);
                given(productService.getAllByStatus(true)).willReturn(Arrays.asList(product));

                mockMvc.perform(get("/api/v1/products/status?status=true"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id", is(product.getId().intValue())))
                                .andExpect(jsonPath("$[0].productName", is(product.getProductName())))
                                .andExpect(jsonPath("$[0].price", is(product.getPrice())));
        }

        @Test
        void testGetMethodNameWithInvalidStatus() throws Exception {
                given(booleanValidationUtils.isValidBoolean("invalid")).willReturn(false);

                mockMvc.perform(get("/api/v1/products/status?status=invalid"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testAddProduct() throws Exception {
                Product product = createProduct(1L, "Product1", 100.0, true);
                CreateProductDTO createProductDTO = new CreateProductDTO();
                createProductDTO.setProductName("Product1");
                createProductDTO.setPrice(100.0);
                createProductDTO.setDescription("Description for Product1");
                createProductDTO.setActive(true);

                given(productService.addProduct( any(CreateProductDTO.class))).willReturn(product);

                mockMvc.perform(post("/api/v1/products/add?userId=" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{\"productName\":\"Product1\",\"price\":100.0,\"description\":\"Description for Product1\",\"active\":true}"))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", "/api/v1/products/1"))
                                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                                .andExpect(jsonPath("$.productName", is(product.getProductName())))
                                .andExpect(jsonPath("$.price", is(product.getPrice())));
        }

        @Test
        void testUpdateProduct() throws Exception {
                Product product = createProduct(1L, "UpdatedProduct", 200.0, false);
                UpdateProductDTO updateProductDTO = new UpdateProductDTO();
                updateProductDTO.setProductName("UpdatedProduct");
                updateProductDTO.setPrice(200.0);
                updateProductDTO.setDescription("Updated description");
                updateProductDTO.setActive(false);
                updateProductDTO.setThumbnailUrl("http://example.com/updated-image.png");
                updateProductDTO.setImageUrls(
                                Arrays.asList("http://example.com/updated-image1.png", "http://example.com/updated-image2.png"));

                given(productService.updateProduct(anyString(), any(UpdateProductDTO.class)))
                                .willReturn(product);

                mockMvc.perform(put("/api/v1/products/update/1?userId=" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{\"productName\":\"UpdatedProduct\",\"price\":200.0,\"description\":\"Updated description\",\"active\":false,\"thumbnailUrl\":\"http://example.com/updated-image.png\",\"images\":[\"http://example.com/updated-image1.png\",\"http://example.com/updated-image2.png\"]}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                                .andExpect(jsonPath("$.productName", is(product.getProductName())))
                                .andExpect(jsonPath("$.price", is(product.getPrice())));
        }

        @Test
        void testDeleteProduct() throws Exception {
                given(productService.deleteById("1")).willReturn("Product deleted successfully");

                mockMvc.perform(delete("/api/v1/products/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Product deleted successfully"));
        }
}
