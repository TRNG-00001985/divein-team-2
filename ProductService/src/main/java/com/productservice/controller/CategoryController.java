package com.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productservice.dto.CategoryRequest;
import com.productservice.dto.CategoryResponse;
import com.productservice.service.CategoryService;

@RestController
@RequestMapping("/api/category/")
public class CategoryController {
     private final  CategoryService categoryService;
	
	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	@PostMapping
	public ResponseEntity<Boolean> createProduct(@RequestBody CategoryRequest categoryRequest){
		
		return new ResponseEntity<>(categoryService.createCategory(categoryRequest),HttpStatus.CREATED);
	}
	@DeleteMapping
	public ResponseEntity<Boolean> deleteProductById( @RequestParam Long id){
		
		return new ResponseEntity<>(categoryService.deleteCategory(id), HttpStatus.OK);
	}
	@GetMapping
	public ResponseEntity<List<CategoryResponse>> showCategories(){
		return new ResponseEntity<>(categoryService.showCategories(),HttpStatus.OK);
	}
	

}
