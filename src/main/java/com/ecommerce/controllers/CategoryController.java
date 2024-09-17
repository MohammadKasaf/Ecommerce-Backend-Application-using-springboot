package com.ecommerce.controllers;

import com.ecommerce.models.Category;
import com.ecommerce.requestDto.AddCategoryRequest;
import com.ecommerce.responseDto.GetCategoryResponse;
import com.ecommerce.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add-Category")
    public ResponseEntity<?> addCategory(@RequestBody AddCategoryRequest category){

        try{
            return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    // Get category by ID
    @GetMapping("/get-Category-By-CategoryId")
    public ResponseEntity<?> getCategory(@RequestParam Long id) {
        try {
            GetCategoryResponse response = categoryService.getCategory(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // Get all categories
    @GetMapping("/get-All-Categories")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<GetCategoryResponse> categories = categoryService.getAllCategories();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    // Delete category by ID
    @DeleteMapping("/delete-Category-By-CategoryId")
    public ResponseEntity<String> deleteCategory(@RequestParam Long id) {

        try{
            String response=categoryService.deleteCategory(id);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



}
