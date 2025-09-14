package com.example.productorders.repository;

import com.example.productorders.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.supplier WHERE p.id = :productId")
    Product findProductWithSupplier(@Param("productId") Long productId);
}
