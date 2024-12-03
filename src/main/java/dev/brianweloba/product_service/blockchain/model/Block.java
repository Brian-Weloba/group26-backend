package dev.brianweloba.product_service.blockchain.model;

import java.time.LocalDateTime;

import dev.brianweloba.product_service.blockchain.utility.SHA256Helper;
import dev.brianweloba.product_service.model.Review;
import lombok.Data;

@Data
public class Block {
    private String previousHash;
    private String hash;
    private Review review;
    private LocalDateTime timestamp;
    private int nonce;

    public Block(Review review, String previousHash) {
        this.review = review;
        this.previousHash = previousHash;
        this.timestamp = LocalDateTime.now();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String data = previousHash + timestamp.toString() + nonce + review.toString();
        return SHA256Helper.generateHash(data);
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

}
