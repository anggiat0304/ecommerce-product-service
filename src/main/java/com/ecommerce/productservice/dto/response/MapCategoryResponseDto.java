package com.ecommerce.productservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MapCategoryResponseDto {
    private List<Map<String, String>> categoryList;
}
