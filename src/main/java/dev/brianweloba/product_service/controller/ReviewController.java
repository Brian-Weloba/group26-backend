package dev.brianweloba.product_service.controller;

import dev.brianweloba.product_service.blockchain.model.Block;
import dev.brianweloba.product_service.dao.AddReviewDTO;
import dev.brianweloba.product_service.dao.UpdateReviewDTO;
import dev.brianweloba.product_service.model.Review;
import dev.brianweloba.product_service.service.BlockchainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final BlockchainService blockchainService;

    public ReviewController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @PostMapping
    public ResponseEntity<Block> addReview(@RequestBody AddReviewDTO reviewDTO) {
        Block reviewBlock = blockchainService.addReview(reviewDTO);
        return ResponseEntity.ok(reviewBlock);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateChain() {
        return ResponseEntity.ok(blockchainService.isChainValid() ? "Chain is valid" : "Chain is not valid");
    }

    @GetMapping
    public ResponseEntity<List<Block>> getChain() {
        return ResponseEntity.ok(blockchainService.getBlockchain());
    }

    @GetMapping("/{reviewId}/history")
    public ResponseEntity<List<Review>> getReviewHistory(@PathVariable("reviewId")  String reviewId) {
        return ResponseEntity.ok(blockchainService.getReviewHistory(reviewId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable("reviewId") String reviewId, @RequestBody UpdateReviewDTO review) {
        return ResponseEntity.ok(blockchainService.updateReview(reviewId, review));
    }

}
