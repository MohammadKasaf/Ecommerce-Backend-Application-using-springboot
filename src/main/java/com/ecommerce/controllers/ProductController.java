package com.ecommerce.controllers;

import com.ecommerce.models.Product;
import com.ecommerce.requestDto.AddProductRequest;
import com.ecommerce.requestDto.UpdateProductRequest;
import com.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add-Product")
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequest product){

        try{
           return new ResponseEntity<>(productService.addProduct(product),HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //get product by id
    @GetMapping("/get-Product-By-ProductId")
    public ResponseEntity<?> getProduct(@RequestParam Long id){

        try{
            return new ResponseEntity<>(productService.getProduct(id), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //get all products
    @GetMapping("/get-All-Products-By-CategoryId")
    public ResponseEntity<?> getAllProducts(@RequestParam Long categoryId){

        try{
            return new ResponseEntity<>(productService.getAllProductsByCategoryId(categoryId), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //update product
    @PutMapping("/update-Product")
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductRequest productRequest){
        try{
            return new ResponseEntity<>(productService.updateProduct(productRequest),HttpStatus.OK);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //delete product by id
    @DeleteMapping("/delete-Product-By-ProductId")
    public ResponseEntity<?> deleteProduct(@RequestParam Long id) {
        try {
            // Call service to delete product
            String response = productService.deleteProduct(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    //delete all products
    @DeleteMapping("/delete-All")
    public ResponseEntity<?> deleteAllProducts() {
        try {
            // Call service to delete all products
            String response = productService.deleteAll();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Handle general exceptions
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
