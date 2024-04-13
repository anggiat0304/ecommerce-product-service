package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.model.ImageProduct;
import com.ecommerce.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ImageProductRepository extends JpaRepository<ImageProduct , Long> {
    @Query(value = "SELECT * FROM image_product WHERE product_id = :productId",nativeQuery = true)
    List<ImageProduct> findByProduct(@Param("productId") Long productId);

    @Modifying
    @Query(value = "DELETE FROM image_product WHERE  product_id = :id",nativeQuery = true)
    int deleteByCode(@Param("id") Long productId);
}
