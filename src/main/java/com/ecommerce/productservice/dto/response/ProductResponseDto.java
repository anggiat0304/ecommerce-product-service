package com.ecommerce.productservice.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Data
public class ProductResponseDto {
    private Long id;
    private String productName;
    private List<String> imageUrl;
    private float price;
    private String code;
    private List<Map<String,String>> categoryList;
    private String description ;

    public ProductResponseDto(Long id, String productName, List<String> imageUrl, float price, String code, List<Map<String,String>> categoryList,String description) {
        this.id = id;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.code = code;
        this.categoryList = categoryList;
        this.description = description;
    }

    public ProductResponseDto() {

    }
}
