package com.ecommerce.productservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ProductUpdateRequestDto {
    private String productName;
    private String code;
    private float price;
    private String description;
    private List<String> images;
    private List<Map<String,String>> selectedCategories;
}
