package dev.brianweloba.product_service.model;

import java.sql.Timestamp;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    UUID createdBy;
    UUID updatedBy;
    String productName;
    @Transient
    @Setter(AccessLevel.NONE)
    String priceValue;
    Double price;
    Currency currency = Currency.getInstance(new Locale("en", "GB"));
    String description;
    Timestamp created;
    Timestamp updated;
    boolean active;
    String thumbnailUrl;
    String extn1;
    String extn2;
    String extn3;
    String extn4;
    String extn5;
    String extn6;
    String extn7;
    String extn8;
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    public String getPriceValue() {
        if (this.price != null) {
            return this.currency + this.price.toString();
        } else {
            return this.currency + "0.00";
        }
    }
}
