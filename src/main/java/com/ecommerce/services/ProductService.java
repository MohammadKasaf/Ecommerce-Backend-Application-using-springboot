package com.ecommerce.services;

import com.ecommerce.models.Category;
import com.ecommerce.models.Product;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.requestDto.AddProductRequest;
import com.ecommerce.requestDto.UpdateProductRequest;
import com.ecommerce.responseDto.GetProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    //save product
    @Transactional
    public String addProduct(AddProductRequest request) {
        // Fetch category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Create Product entity from request
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);

        // Save the product
        productRepository.save(product);
        return "Product added successfully";
    }


    //get product
    public GetProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        // Map Product entity to GetProductResponse DTO
        GetProductResponse response = new GetProductResponse();
        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategoryId(product.getCategory().getCategoryId());  // Assumes category is not null

        return response;
    }


    //get all products by categoryId
    public List<GetProductResponse> getAllProductsByCategoryId(Long categoryId) {
        // Fetch products by category ID
        List<Product> products = productRepository.findByCategory_CategoryId(categoryId);

        // Map Product entities to GetProductResponse DTOs
        List<GetProductResponse> responseList = new ArrayList<>();
        for (Product product : products) {
            GetProductResponse response = new GetProductResponse();
            response.setProductId(product.getProductId());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setStockQuantity(product.getStockQuantity());
            response.setCategoryId(product.getCategory().getCategoryId());  // Assumes category is not null
            responseList.add(response);
        }

        return responseList;
    }



    //update product
    @Transactional
    public String updateProduct(UpdateProductRequest productRequest) {
        // Retrieve the product from the repository
        Product product = productRepository.findById(productRequest.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productRequest.getId()));

        // Update the existing product's fields
        product.setPrice(productRequest.getPrice());
        product.setName(productRequest.getName());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setDescription(productRequest.getDescription());

        // Save the updated product
        productRepository.save(product);

        return "Product successfully updated";
    }


    //delete product
    @Transactional
    public String  deleteProduct(Long id) {

        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.delete(product);
            return "product successfully deleted";
        } else {
            return "product is not found with id:" + id;
        }

    }

    //delete all products
    @Transactional
    public String deleteAll(){

        List<Product> products=productRepository.findAll();
        productRepository.deleteAll(products);
        return "All prodcuts are deleted";
    }

}
