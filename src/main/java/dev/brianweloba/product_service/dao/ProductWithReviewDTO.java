package dev.brianweloba.product_service.dao;

import dev.brianweloba.product_service.model.Product;
import dev.brianweloba.product_service.model.Review;
import lombok.Data;

import java.util.List;

@Data
public class ProductWithReviewDTO extends Product {
    List<Review> reviews;
    float averageRating;
}
