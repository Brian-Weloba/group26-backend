package dev.brianweloba.product_service.dao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductDTO {
    @NotBlank(message = "Product name is required")
    private String productName;
    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be a positive number or zero")
    private Double price;
    String description;
    boolean active;
    List<String> imageUrls;
}
