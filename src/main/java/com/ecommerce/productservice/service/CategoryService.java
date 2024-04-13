package com.ecommerce.productservice.service;

import com.ecommerce.productservice.configuration.CallBackApi;
import com.ecommerce.productservice.dto.request.CategoryRequestDto;
import com.ecommerce.productservice.dto.response.CategoryResponseDto;
import com.ecommerce.productservice.dto.response.MapCategoryResponseDto;
import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.model.ProductCategory;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductCategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CallBackApi callBackApi;

    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    ProductRepository productRepository;
    public String addCategory(String token , Long userId, CategoryRequestDto categoryRequestDto) {

        String response = null;
        try {
            callBackApi.validateToken(token,userId);
           Category category = categoryRepository.getCategoriesByCodeCategory(categoryRequestDto.getCodeCategory());
            if (category != null) {
                throw new IllegalArgumentException("Category already Exists");
//                return "Code Already Exists";
            } else {
                category = new Category();
                category.setNameCategory(categoryRequestDto.getName());
                category.setDescription(categoryRequestDto.getDescription());
                category.setCodeCategory(categoryRequestDto.getCodeCategory());
                categoryRepository.saveAndFlush(category);
            }
            response = "oke";
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public List<CategoryResponseDto> categoryList(String token, Long userId) throws Exception {
        callBackApi.validateToken(token, userId);
        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream()
                .map(category -> {
                    CategoryResponseDto categoryResponseDto = new CategoryResponseDto(); // Inisialisasi objek di dalam stream
                    categoryResponseDto.setProducts(null);
                    categoryResponseDto.setCode(category.getCodeCategory());
                    categoryResponseDto.setName(category.getNameCategory());
                    categoryResponseDto.setDescription(category.getDescription());
                    return categoryResponseDto;
                })
                .collect(Collectors.toList()); // Mengubah tipe kembali menjadi List<CategoryResponseDto>
    }



    public CategoryResponseDto getCategory(String token, long userId, String code) {
        try {
            callBackApi.validateToken(token, userId);

            Category category = categoryRepository.getCategoriesByCodeCategory(code);
            List<ProductCategory> productCategory = productCategoryRepository.getProductCategoriesByCategoryCodeCategory(category.getId());

            List<Product> products = productCategory.stream()
                    .map(productCategory1 -> productRepository.getProductById(productCategory1.getProduct().getId()))
                    .collect(Collectors.toList());

            return new CategoryResponseDto(products, category.getCodeCategory(), category.getNameCategory(), category.getDescription());
        } catch (DataAccessException e) {
            throw new RuntimeException("An unexpected database error occurred. Details: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred. Details: " + e.getMessage(), e);
        }
    }




    public MapCategoryResponseDto categoryMapList(String token, Long userId) throws Exception {
        callBackApi.validateToken(token, userId);
        List<Category> categoryList = categoryRepository.findAll();
        MapCategoryResponseDto categoryResponseDto = new MapCategoryResponseDto();
        List<Map<String, String>> categoryCode = new ArrayList<>();
        categoryList.forEach(category1 -> {
            Map<String, String> categoryMap = new HashMap<>();
            categoryMap.put("name", category1.getNameCategory());
            categoryMap.put("code", category1.getCodeCategory()); // Add code if needed
            categoryCode.add(categoryMap);
        });
        categoryResponseDto.setCategoryList(categoryCode);
        return categoryResponseDto;
    }


    public String updateCategory(String token, Long userId ,CategoryRequestDto categoryRequestDto, String code) {
        String response = null;
        try {
            callBackApi.validateToken(token,userId);
            categoryRepository.updateCategoryByCodeCategory(code, categoryRequestDto.getName(), categoryRequestDto.getDescription(), Arrays.toString(categoryRequestDto.getImageData()));
            response = "oke";
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public boolean deleteCategory(String token , Long userId ,String codeCategory, boolean confirm) {
        boolean response = false;
        try {
            callBackApi.validateToken(token,userId);
            if (confirm) {
                categoryRepository.deleteByCodeCategory(codeCategory);
                response = true;
            } else {
                response = false;
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
