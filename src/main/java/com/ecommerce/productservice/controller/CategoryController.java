package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.request.CategoryRequestDto;
import com.ecommerce.productservice.dto.response.CategoryResponseDto;
import com.ecommerce.productservice.dto.response.MapCategoryResponseDto;
import com.ecommerce.productservice.generics.GenericResponse;
import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private  Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public GenericResponse<?> categorylist(@RequestHeader(value = "Authorization", required = true) String authorization,
                                           @RequestHeader("User") Long userId){
        try {
            List<CategoryResponseDto> categoryList = categoryService.categoryList(authorization,userId);
            GenericResponse<?> response = GenericResponse.success(HttpStatus.OK.toString(),categoryList);
            return response;
        }catch (IllegalArgumentException e){
            logger.error(e.getMessage());
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }catch (Exception e) {
            logger.error(e.getMessage());
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
    @GetMapping("/categories/{code}")
    public GenericResponse<?> getCategory(@RequestHeader(value = "Authorization", required = true) String authorization,
                                           @RequestHeader("User") Long userId,
                                            @PathVariable("code") String codeCategory){
        try {
            CategoryResponseDto category = categoryService.getCategory(authorization,userId,codeCategory);
            GenericResponse<?> response = GenericResponse.success(HttpStatus.OK.toString(),category);
            return response;
        }catch (IllegalArgumentException e){
            logger.error(e.getMessage());
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage());
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
    @GetMapping("/categories/map")
    public GenericResponse<?> mapsCategoryList(@RequestHeader(value = "Authorization", required = true) String authorization,
                                           @RequestHeader("User") Long userId){
        try {
            MapCategoryResponseDto categoryList = categoryService.categoryMapList(authorization,userId);
            GenericResponse<MapCategoryResponseDto> response = GenericResponse.success(HttpStatus.OK.toString(),categoryList);
            return response;
        }catch (IllegalArgumentException e){
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            logger.error(e.getMessage());
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/categories")
    public GenericResponse<?> addCategory(@RequestHeader(value = "Authorization", required = true) String authorization,
                                          @RequestHeader("User") Long userId,
                                          @RequestBody CategoryRequestDto requestDto){
        try {
            String res = categoryService.addCategory(authorization,userId,requestDto);
            GenericResponse<String> response = GenericResponse.success(HttpStatus.OK.toString(),res);
            return response;
        }catch (IllegalArgumentException e){
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
    @PutMapping("/categories/{code}")
    public GenericResponse<?> updateCategory(@RequestHeader(value = "Authorization", required = true) String authorization,
                                             @RequestHeader("User") Long userId,
                                             @PathVariable("code") String code , @RequestBody CategoryRequestDto categoryRequestDto){
        try {
            String res = categoryService.updateCategory(authorization,userId,categoryRequestDto,code);
            GenericResponse<String> response = GenericResponse.success(HttpStatus.OK.toString(),res);
            return response;
        }catch (IllegalArgumentException e){
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
    @DeleteMapping("/categories/{code}")
    public GenericResponse<?> deleteCategories(@RequestHeader(value = "Authorization", required = true) String authorization,
                                               @RequestHeader("User") Long userId,
                                               @PathVariable("code") String code,@RequestParam("confirm") boolean confirm){
        try {
             Boolean res = categoryService.deleteCategory(authorization,userId,code,confirm);
             GenericResponse<Boolean> response = GenericResponse.success(HttpStatus.OK.toString(),res);
             return response;
        }catch (IllegalArgumentException e){
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
}
