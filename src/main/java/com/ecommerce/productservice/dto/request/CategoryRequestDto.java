package com.ecommerce.productservice.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CategoryRequestDto {
    private String name;
    private String description;
    private String codeCategory;
    private byte[] imageData;
}
