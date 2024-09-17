package com.ecommerce.services;

import com.ecommerce.models.Category;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.requestDto.AddCategoryRequest;
import com.ecommerce.responseDto.GetCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    //save category
    @Transactional
    public String saveCategory(AddCategoryRequest request) {
        // Map AddCategoryRequest to Category entity
        Category category = new Category();
        category.setCategoryId(request.getCategoryId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        // Save the category
        categoryRepository.save(category);
        return "Category saved successfully";
    }

    //get Category
    @Transactional
    public GetCategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category with id: " + id + " not found"));

        // Convert Category to GetCategoryResponse DTO
        return new GetCategoryResponse(
                category.getCategoryId(),
                category.getName(),
                category.getDescription()
        );
    }


    //get all categories
    @Transactional
    public List<GetCategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<GetCategoryResponse> responses = new ArrayList<>();

        for (Category category : categories) {
            GetCategoryResponse response = new GetCategoryResponse(
                    category.getCategoryId(),
                    category.getName(),
                    category.getDescription()
            );
            responses.add(response);
        }

        return responses;
    }


    //delete category
    @Transactional
    public String deleteCategory(Long id){

        Category category=categoryRepository.findById(id).orElse(null);
        if(category==null){
            return "category with id:"+ id +"not found";
        }

        categoryRepository.deleteById(id);
        return "category "+category.getName() + " successfully deleted ";
    }
}
