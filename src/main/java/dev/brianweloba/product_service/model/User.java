package dev.brianweloba.product_service.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String userName;

}
