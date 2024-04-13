package com.ecommerce.productservice.dto.response;

import com.ecommerce.productservice.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CategoryResponseDto {
    private String name;
    private String code;
    private String description;
    @JsonIgnore
    private List<Product> products;

    public CategoryResponseDto(List<Product> products, String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.products = products;
        this.description = description;
    }

    public CategoryResponseDto() {
    }
}

