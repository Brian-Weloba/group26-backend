package dev.brianweloba.product_service.dao;

import lombok.Data;

@Data
public class UpdateReviewDTO {
    String content;
    int rating;
}
