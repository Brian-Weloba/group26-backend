package dev.brianweloba.product_service.blockchain.model;

import dev.brianweloba.product_service.model.Review;
import lombok.Getter;

@Getter
public class ReviewBlock extends Block {
    private final ReviewAction action;
    public String originalReviewId;

    public ReviewBlock(Review review, ReviewAction action, String previousReviewHash) {
        super(review, previousReviewHash);
        this.action = action;
        this.originalReviewId = review.getVersion() == 1? review.getReviewId(): review.getOriginalReviewId();
    }
}
