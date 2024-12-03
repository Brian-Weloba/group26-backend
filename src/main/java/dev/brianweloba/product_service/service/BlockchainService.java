package dev.brianweloba.product_service.service;

import dev.brianweloba.product_service.blockchain.model.Block;
import dev.brianweloba.product_service.blockchain.model.ReviewAction;
import dev.brianweloba.product_service.blockchain.model.ReviewBlock;
import dev.brianweloba.product_service.blockchain.model.ReviewStatus;
import dev.brianweloba.product_service.dao.AddReviewDTO;
import dev.brianweloba.product_service.dao.UpdateReviewDTO;
import dev.brianweloba.product_service.model.Review;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BlockchainService {
    @Getter
    private List<Block> blockchain = new ArrayList<>();
    private final int difficulty = 5;

    public BlockchainService() {
        Review genesisReview = new Review();
        genesisReview.setContent("Genesis block");
        Block genesisBlock = new Block(genesisReview, "0");
        blockchain.add(genesisBlock);
    }

    public Block addReview(AddReviewDTO reviewDTO) {
        Review review = new Review();
        review.setProductId(reviewDTO.getProductId());
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());
        review.setUserName(reviewDTO.getUserName());
        review.setVersion(1);
        review.setStatus(ReviewStatus.ACTIVE);
        review.setCreatedAt(LocalDateTime.now());
        review.setOriginalReviewId(review.getReviewId());

        Block newBlock = new Block(review, blockchain.get(blockchain.size() - 1).getHash());
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
        return newBlock;
    }

    public Review updateReview(String reviewId, UpdateReviewDTO updateReviewDTO) {
        Review currentReview = findActiveReview(reviewId);
        if (currentReview == null) {
            throw new NullPointerException("Review not found: " + reviewId);
        }

        Review updatedReview = new Review();
        updatedReview.setUserName(currentReview.getUserName());
        updatedReview.setProductId(currentReview.getProductId());
        updatedReview.setContent(updateReviewDTO.getContent());
        updatedReview.setRating(updateReviewDTO.getRating());
        updatedReview.setCreatedAt(LocalDateTime.now());
        updatedReview.setStatus(ReviewStatus.ACTIVE);
        updatedReview.setPreviousReviewId(currentReview.getReviewId());
        updatedReview.setVersion(currentReview.getVersion() + 1);
        updatedReview.setOriginalReviewId(currentReview.getOriginalReviewId());

        currentReview.setStatus(ReviewStatus.UPDATED);

        ReviewBlock newBlock = new ReviewBlock(updatedReview, ReviewAction.UPDATE,
                blockchain.get(blockchain.size() - 1).getHash());
        blockchain.add(newBlock);

        return updatedReview;
    }

    public List<Review> getReviewHistory(String reviewId) {
        return blockchain.stream()
                .filter(block -> block instanceof ReviewBlock)
                .map(Block::getReview)
                .filter(review -> review.getOriginalReviewId().equals(reviewId))
                .toList();
    }

    public Review findActiveReview(String reviewId) {
        return blockchain.stream()
                .filter(Objects::nonNull)
                .map(Block::getReview)
                .filter(review -> review.getReviewId().equals(reviewId) &&
                        review.getStatus().equals(ReviewStatus.ACTIVE))
                .findFirst()
                .orElse(null);
    }

    public List<Review> findReviewsByProductId(String productId) {
        return blockchain.stream()
                .filter(block -> block != null && block.getReview() != null)
                .map(Block::getReview)
                .filter(review -> productId.equals(review.getProductId()))
                .collect(Collectors.toList());
    }

    public List<Review> findReviewsByUserName(String userName) {
        return blockchain.stream()
                .filter(block -> block != null && block.getReview() != null)
                .map(Block::getReview)
                .filter(review -> userName.equals(review.getUserName()))
                .collect(Collectors.toList());
    }

    public int getReviewCountByProductId(String productId) {
        return (int) blockchain.stream()
                .filter(Objects::nonNull)
                .map(Block::getReview)
                .filter(review -> productId.equals(review.getProductId()))
                .count();
    }

    public float getAverageRatingByProductId(String productId) {
        List<Review> reviews = findReviewsByProductId(productId);
        if (reviews.isEmpty()) {
            return 0;
        }
        return (float) reviews.stream()
                .mapToInt(Review::getRating)
                .sum() / reviews.size();
    }

    public boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            // validae hash
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }

            // validate previous hash
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

}
