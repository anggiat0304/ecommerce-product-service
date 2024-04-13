package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findByCode(String code);

    boolean existsByCode(String code);
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.name = :name, p.price = :price , p.description = :desc WHERE p.code = :code")
    int updateProductByCode(@Param("code") String code, @Param("name") String name, @Param("price") float price, @Param("desc") String desc);

    @Modifying
    @Query(value = "DELETE FROM products WHERE  code = :code",nativeQuery = true)
    int deleteByCode(@Param("code") String code);
    @Query(value = "SELECT model FROM Product model  WHERE  model.id = :id")
    Product getProductById(@Param("id") Long id);
}
