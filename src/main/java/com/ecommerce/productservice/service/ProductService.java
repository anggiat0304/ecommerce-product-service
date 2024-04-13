package com.ecommerce.productservice.service;

import aj.org.objectweb.asm.TypeReference;
import com.ecommerce.productservice.configuration.CallBackApi;
import com.ecommerce.productservice.dto.request.ProductRequestDto;
import com.ecommerce.productservice.dto.request.ProductUpdateRequestDto;
import com.ecommerce.productservice.dto.response.ProductResponseDto;
import com.ecommerce.productservice.generics.GenericResponse;
import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.ImageProduct;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ImageProductRepository;
import com.ecommerce.productservice.repository.ProductCategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageProductRepository imageProductRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    CallBackApi callBackApi;
    public List<ProductResponseDto> getAllProducts(String token, Long userId) throws Exception {
        callBackApi.validateToken(token,userId);

        List<Product> productList = productRepository.findAll();

        List<ProductResponseDto> productResponseList = productList.stream()
                .map(product -> {
                    List<String> imageUrls = product.getImages().stream()
                            .map(ImageProduct::getImageData)
                            .collect(Collectors.toList());
                    List<Map<String, String>> codeAndCategory = product.getCategories().stream()
                            .map(category -> {
                                Map<String, String> categoryMap = new HashMap<>();
                                categoryMap.put("code", category.getCodeCategory());
                                categoryMap.put("name", category.getNameCategory());
                                return categoryMap;
                            })
                            .collect(Collectors.toList());

                    return new ProductResponseDto(
                            product.getId(),
                            product.getName(),
                            imageUrls,
                            product.getPrice(),
                            product.getCode(),
                            codeAndCategory,
                            product.getDescription()
                    );
                })
                .collect(Collectors.toList());

        return productResponseList;
    }
    public ProductResponseDto getProduct(String token , Long userId ,String code) throws Exception {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        try {
            callBackApi.validateToken(token,userId);
            Product product = productRepository.findByCode(code);
            if (product == null){
                throw new IllegalArgumentException("Product with code " + code + " doesn't exists");
            }
            productResponseDto.setProductName(product.getName());
            productResponseDto.setPrice(product.getPrice());
            productResponseDto.setCode(product.getCode());
            productResponseDto.setDescription(product.getDescription());
            //category list
            // Category list
            Set<Category> categoryList = product.getCategories();
            List<Map<String, String>> category = new ArrayList<>();
            categoryList.forEach(category1 -> {
                Map<String, String> categoryMap = new HashMap<>();
                categoryMap.put("name", category1.getNameCategory());
                categoryMap.put("code", category1.getCodeCategory()); // Add code if needed
                category.add(categoryMap);
            });
            productResponseDto.setCategoryList(category);

            //image list
            List<ImageProduct> imageProducts = imageProductRepository.findByProduct(product.getId());
            List<String> listImage = new ArrayList<>();
            imageProducts.forEach(imageProduct -> {
                listImage.add(imageProduct.getImageData());
            });
            productResponseDto.setImageUrl(listImage);
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
        return productResponseDto;
    }
    public String saveProduct(String token , Long userId, ProductRequestDto productRequestDto) {
        String response = null;
        try {
            callBackApi.validateToken(token,userId);
            Product product = new Product();
            if (productRepository.existsByCode(productRequestDto.getCode())) {
                throw new IllegalArgumentException("Product with code "+productRequestDto.getCode()+" already exists");
            }
            product.setCode(productRequestDto.getCode());
            product.setName(productRequestDto.getProductName());
            product.setPrice(productRequestDto.getPrice());
            product.setDescription(productRequestDto.getDescription());
            productRepository.saveAndFlush(product);

            List<String> imageUrls = productRequestDto.getImages();
            if (imageUrls != null && !imageUrls.isEmpty()) {
                for (String imageUrl : imageUrls) {
                    ImageProduct image = new ImageProduct();
                    image.setImageData(imageUrl);
                    image.setProduct(product);
                    imageProductRepository.saveAndFlush(image);
                }
            }

            List<String> categoryNames = productRequestDto.getSelectedCategories();
            if (categoryNames != null && !categoryNames.isEmpty()) {
                Set<Category> categories = categoryNames.stream()
                        .map(categoryName -> categoryRepository.findByNameCategory(categoryName))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
                product.setCategories(categories);
            }

            response = "Product saved successfully";
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to save product: " + e.getMessage());
        }
        return response;
    }

    public int updateProduct(String token, Long userId, ProductUpdateRequestDto requestDto, String code) throws Exception {
        Product product = new Product();
        callBackApi.validateToken(token, userId);
        product = productRepository.findByCode(code);

        if (product == null) {
            throw new IllegalArgumentException("Product with code: " + code + " doesn't exist");
        } else {
            productRepository.updateProductByCode(code, requestDto.getProductName(), requestDto.getPrice(), requestDto.getDescription());

            List<Map<String, String>> selectedCategories = requestDto.getSelectedCategories();
            if (selectedCategories != null && !selectedCategories.isEmpty()) {
                Set<Category> categories = selectedCategories.stream()
                        .map(categoryMap -> {
                            String categoryCode = categoryMap.get("code");
                            String categoryName = categoryMap.get("name");

                            Category category = categoryRepository.getCategoriesByCodeCategory(categoryCode);

                            // Throws an exception if the category is not found
                            if (category == null) {
                                throw new IllegalArgumentException("Category with code: " + categoryCode + " doesn't exist");
                            }
                            return category;
                        })
                        .collect(Collectors.toSet());

                // Set the new categories for the product
                product.setCategories(categories);
            }
            List<String> newImages = requestDto.getImages();
            if (newImages != null && !newImages.isEmpty()) {
                imageProductRepository.deleteByCode(product.getId());
                for (String imageData : newImages) {
                    ImageProduct imageProduct = new ImageProduct();
                    imageProduct.setImageData(imageData);
                    imageProduct.setProduct(product);
                    imageProductRepository.save(imageProduct);
                }
            }
        }
        return 1;
    }


    public boolean deleteProduct(String token , long userId,String code){
        boolean response = false;
        try {
            callBackApi.validateToken(token,userId);
            Product product = productRepository.findByCode(code);
            productCategoryRepository.deleteProductCategoriesByCode(code);
            imageProductRepository.deleteByCode(product.getId());

            productRepository.deleteByCode(code);
            response  = true;
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
