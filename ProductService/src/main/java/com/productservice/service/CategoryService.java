package com.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.productservice.dto.CategoryRequest;
import com.productservice.dto.CategoryResponse;
import com.productservice.model.Category;
import com.productservice.repository.CategoryRepository;
@Service
public class CategoryService {
	private CategoryRepository categoryRepository;
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository=categoryRepository;
	}
    public boolean createCategory(CategoryRequest categoryRequest) {
    	try {
		Category category  = Category.builder().name(categoryRequest.getName()).build();
		category=categoryRepository.save(category);
		return true;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
		return false;
	}
    public boolean deleteCategory(long categoryId) {
    	
         try {
			
			categoryRepository.deleteById(categoryId);
			return true;
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    public List<CategoryResponse> showCategories(){
    	List<Category> categories=categoryRepository.findAll();
    	return categories.stream().map(category->CategoryResponse.builder().id(category.getId()).name(category.getName()).build()).toList();
    }
	

}
