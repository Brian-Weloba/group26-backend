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
public class UpdateProductDTO {
    @NotBlank(message = "Product name is required")
    private String productName;
    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be a positive number or zero")
    private Double price;
    private String description;
    private boolean active;
    private String thumbnailUrl;
    private List<String> imageUrls;
    String extn1;
    String extn2;
    String extn3;
    String extn4;
    String extn5;
    String extn6;
    String extn7;
    String extn8;
}
