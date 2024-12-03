package dev.brianweloba.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brianweloba.product_service.model.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    List<Product> findByActiveFalse();
}
