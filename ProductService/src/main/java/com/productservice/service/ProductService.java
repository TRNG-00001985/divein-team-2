package com.productservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.productservice.dto.CategoryResponse;
import com.productservice.dto.ProductRequest;
import com.productservice.dto.ProductResponse;
import com.productservice.model.Category;
import com.productservice.model.Product;
import com.productservice.repository.CategoryRepository;
import com.productservice.repository.ProductRepository;

@Service
public class ProductService {
	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;
	public ProductService(ProductRepository productRepository,CategoryRepository categoryRepository) {
		this.productRepository=productRepository;
		this.categoryRepository=categoryRepository;
	}
    public Product mapToProduct(ProductRequest productRequest) {
		
		Category category = categoryRepository.findById(productRequest.getCategoryId()).get();
		
		
		return  Product.builder()
				.userId(productRequest.getUserId())
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.skuCode( UUID.randomUUID().toString())
				.imageURL(productRequest.getImageURL())
				.category(category)
				.build();
										
	}
	
	public ProductResponse mapToProductResponse(Product product) {
		
		Category category = product.getCategory();
		CategoryResponse categoryDto = CategoryResponse.builder()
				.id(category.getId())
				.name(category.getName())
				.build();
		
		return ProductResponse.builder()
				.id(product.getId())
				.userId(product.getUserId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.skuCode(product.getSkuCode())
				.imageURL(product.getImageURL())
				.categoryDto(categoryDto)
				.build();
	}

	public ProductResponse createProduct(ProductRequest productRequest) {
		
		Product product = mapToProduct(productRequest);
		
		product = productRepository.save(product);
		
		
		
		return mapToProductResponse(product);
		
		
	}

	
	public ProductResponse getProductById(Long id) {
		
		Product product = productRepository.findById(id).get();
		
		return mapToProductResponse(product);		
		
	}
	
	public ProductResponse updateProductById(ProductRequest productRequest, Long id) {
		
		Product product = mapToProduct(productRequest);
		product.setId(id);
		
		product = productRepository.save(product);
		
		return mapToProductResponse(product);
		
	}
	
	public boolean deleteProductById(Long id) {
		try {
			
			productRepository.deleteById(id);
			return true;
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<ProductResponse> getAllProducts(){
		
		List<Product> products = productRepository.findAll();
		
		return products.stream().map(product -> mapToProductResponse(product)).toList();
		
	}
	public List<Product> getProductByCategoryId(Long categoryId){
		List<Product> products=productRepository.findByCategoryId(categoryId);
//		return products.stream().map(product->mapToProductResponse(product)).toList();
		return products;
	}
	public List<ProductResponse> getProductByUserId(String userId){
		List<Product> products=productRepository.findByuserId(userId);
		return products.stream().map(product->mapToProductResponse(product)).toList();	}
//	public boolean addToInventory(Long productId,Integer quantity) {
//		
//	}

}


