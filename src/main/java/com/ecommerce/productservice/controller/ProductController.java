package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.request.ProductRequestDto;
import com.ecommerce.productservice.dto.request.ProductUpdateRequestDto;
import com.ecommerce.productservice.dto.response.ProductResponseDto;
import com.ecommerce.productservice.generics.GenericResponse;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping( value =  "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse<?> getAllProducts(@RequestHeader(value = "Authorization", required = true) String authorization, @RequestHeader("User") Long userId){
        try {
            List<ProductResponseDto> products = productService.getAllProducts(authorization,userId);
            System.out.println("Products: " + products);
            GenericResponse<List<ProductResponseDto>> response = GenericResponse.success(HttpStatus.OK.toString(),products);
            System.out.println("Response: " + response);
             return response;
        }catch (Exception e){
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }

    @GetMapping("/products/{code}")
    public GenericResponse<ProductResponseDto> getProductById(
            @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestHeader("User") Long userId,
            @PathVariable("code") String code) {
        try {
            ProductResponseDto product = productService.getProduct(authorization,userId,code);
            GenericResponse<ProductResponseDto> response = GenericResponse.success(HttpStatus.OK.toString(), product);
            return response;
        } catch (Exception e) {
            GenericResponse<ProductResponseDto> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }


    @PostMapping("/products")
    public GenericResponse<?> saveProduct(
            @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestHeader("User") Long userId,
            @RequestBody ProductRequestDto productRequestDto){
       try {
           String saveProducts =  productService.saveProduct(authorization,userId, productRequestDto);
           GenericResponse<String> response = GenericResponse.success(HttpStatus.OK.toString(),saveProducts);
           return response ;
       }catch (Exception e){
           GenericResponse<?> response = GenericResponse.error(e.getMessage());
           return response;
       }
    }
    @PutMapping("/products/{code}")
    public GenericResponse<?> updateProduct(
            @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestHeader("User") Long userId,
            @PathVariable("code") String code ,
            @RequestBody ProductUpdateRequestDto productRequestDto){
        try {
            Integer update =  productService.updateProduct(authorization,userId, productRequestDto,code);
            GenericResponse<Integer> response = GenericResponse.success(HttpStatus.OK.toString(),update);
            return response;
        }catch (Exception e){
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
    @DeleteMapping("/products/{code}")
    public GenericResponse<?> deleteProduct(
            @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestHeader("User") Long userId,
            @PathVariable("code") String code){
        try {
            Boolean deleteProduct = productService.deleteProduct(authorization,userId,code);
            GenericResponse<Boolean>  response = GenericResponse.success(HttpStatus.OK.toString(),deleteProduct);
            return response;
        }catch (IllegalArgumentException e){
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
}
