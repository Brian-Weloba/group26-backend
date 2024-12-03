package dev.brianweloba.product_service.dao;

import dev.brianweloba.product_service.blockchain.model.ReviewStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class AddReviewDTO {
    private String userName;
    private String productId;
    private String content;
    private int rating;
//    private ReviewStatus status = ReviewStatus.ACTIVE;
//    private int version =1;

//    private String reviewId = UUID.randomUUID().toString();
//    private String originalReviewId;
//    private String userId;
//    private String productId;
//    private String content;
//    private int rating;
//    private LocalDateTime createdAt;
//    private ReviewStatus status;
//    private String previousReviewHash;
//    private int version;
}
