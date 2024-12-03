package dev.brianweloba.product_service.model;

import dev.brianweloba.product_service.blockchain.model.ReviewStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Review {
    private String reviewId;
    private String originalReviewId;
    private String userName;
    private String productId;
    private String content;
    private int rating;
    private LocalDateTime createdAt;
    private ReviewStatus status;
    private String previousReviewId;
    private int version;

    public Review(){
        this.reviewId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

}
