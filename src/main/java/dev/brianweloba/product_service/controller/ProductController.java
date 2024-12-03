package dev.brianweloba.product_service.controller;

import dev.brianweloba.product_service.dao.ProductWithReviewDTO;
import org.springframework.web.bind.annotation.RestController;

import dev.brianweloba.product_service.dao.CreateProductDTO;
import dev.brianweloba.product_service.dao.UpdateProductDTO;
import dev.brianweloba.product_service.model.Product;
import dev.brianweloba.product_service.service.ProductService;
import dev.brianweloba.product_service.util.BooleanValidationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final BooleanValidationUtils booleanValidationUtils;

    @GetMapping("/ping")
    public String pingServer() {
        return "PONG! PONG! PONG!";
    }

    @PostMapping("/pong")
    public String postPong() {
        return "PING! PING! PING!";
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    //get product with reviews
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ProductWithReviewDTO> getProductWithReviews(@PathVariable("id") String id) {
        ProductWithReviewDTO productOptional = productService.getProductWithReviews(id);
        return  ResponseEntity.ok().body(productOptional);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ProductWithReviewDTO>> getAllProductsWithReviews() {
        List<ProductWithReviewDTO> productOptional = productService.getAllProductsWithReviews();
        return  ResponseEntity.ok().body(productOptional);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") String id) {
        Optional<Product> productOptional = productService.getProductById(id);

        return productOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/status")
    public ResponseEntity<List<Product>> getMethodName(@RequestParam("status") String status) {

        if (!booleanValidationUtils.isValidBoolean(status)) {
            return ResponseEntity.badRequest().build();
        }

        boolean statusBool = booleanValidationUtils.parseBoolean(status);
        return ResponseEntity.ok().body(productService.getAllByStatus(statusBool));
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
            @RequestBody @Valid CreateProductDTO createProductDTO) {
        Product product = productService.addProduct(createProductDTO);
        return ResponseEntity.created(URI.create("/api/v1/products/" + product.getId())).body(product);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String id,
            @RequestBody UpdateProductDTO updateProductDTO) {

        Product product = productService.updateProduct(id,updateProductDTO);
        return ResponseEntity.ok().body(product);
    }

//    @GetMapping("/product/ratings/{id}")

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(productService.deleteById(id));
    }
}
