package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category , Long> {
        @Query(value = "SELECT * FROM categories  WHERE code_category = :category" , nativeQuery = true)
        Category findByNameCategory(@Param("category") String category);
        @Modifying
        @Query(value = "UPDATE categories SET name_category = :name , description = :desc , image_data = :image WHERE code_category = :code " , nativeQuery = true)
        int updateCategoryByCodeCategory(@Param("code") String codeCategory, @Param("name") String name , @Param("desc") String desc ,@Param("image") String image);

        @Modifying
        @Query(value = "DELETE FROM categories WHERE code_category = :code",nativeQuery = true)
        int deleteByCodeCategory(@Param("code") String code);

        @Query(value = "SELECT * FROM categories WHERE code_category = :code",nativeQuery = true)
        Category getCategoriesByCodeCategory(@Param("code") String code);

        @Query(value = "SELECT pc FROM ProductCategory pc WHERE pc.category.codeCategory = :code")
        Optional<ProductCategory> getCategoriesByCodeCategoryOptional(@Param("code") String code);

}
