package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Transactional
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long> {

    @Modifying
    @Query("DELETE FROM ProductCategory pc WHERE pc.product.code = :code")
    void deleteProductCategoriesByCode(@Param("code") String code);

    @Query(value = "SELECT model FROM ProductCategory model WHERE model.category.id = :categoryId")
    List<ProductCategory> getProductCategoriesByCategoryCodeCategory(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT model FROM ProductCategory as model WHERE  model.category.id = :categoryId ")
    List<ProductCategory> getProductCategoryByCategoryId(Long categoryId);


}
