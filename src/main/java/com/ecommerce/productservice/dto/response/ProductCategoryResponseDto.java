package com.ecommerce.productservice.dto.response;

import com.ecommerce.productservice.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductCategoryResponseDto {
    private List<Product> products;
}
