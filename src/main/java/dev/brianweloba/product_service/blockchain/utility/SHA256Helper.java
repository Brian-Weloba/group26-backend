package dev.brianweloba.product_service.blockchain.utility;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SHA256Helper {
    public static  final SecureRandom secureRandom = new SecureRandom();

    public static String generateHash(String data){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate hash: SHA-256 algorithm not found", e);

        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for(byte b: hash){
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1){
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    //SHA-256 hash with a random salt
    public static SaltedHash generateSaltedHash(String data){
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return new SaltedHash(salt, generateHash(data + Base64.getEncoder().encodeToString(salt)));
    }

    public static boolean verifySaltedHash(String data, SaltedHash saltedHash){
        String computedHash = generateHash(data + Base64.getEncoder().encodeToString(saltedHash.salt));
        return saltedHash.hash.equals(computedHash);
    }

    private static byte[] generateSalt(){
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public record SaltedHash(byte[] salt, String hash) { }
}
