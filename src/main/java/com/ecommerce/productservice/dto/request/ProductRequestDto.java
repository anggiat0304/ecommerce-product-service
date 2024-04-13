package com.ecommerce.productservice.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ProductRequestDto {
    private String productName;
    private String code;
    private float price;
    private String description;
    private List<String> images;
    private List<String> selectedCategories;
}
