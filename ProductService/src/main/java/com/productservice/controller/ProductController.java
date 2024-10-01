package com.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productservice.dto.ProductRequest;
import com.productservice.dto.ProductResponse;
import com.productservice.model.Product;
import com.productservice.service.ProductService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/product")
public class ProductController {
	
	
	private final  ProductService productService;
	
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/addProduct")
	public String Home() {
		return "addProduct";
	}
	
    @GetMapping("/allproducts")
    public ResponseEntity<List<ProductResponse>> getDashboard(Model model) {
        List<ProductResponse> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return ResponseEntity.ok(products);  
    }
	public ResponseEntity<String> getDashboardfallbackMethod( RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }
	@GetMapping("/sellerdashboard")
	@CircuitBreaker(name="ProductService",fallbackMethod="getSellerDashboard")
	public String getSellerDashboard(Model model) {
	    List<ProductResponse> products = productService.getAllProducts();
	    model.addAttribute("products", products);
	    return "sellerdashboard";  // Return the name of the JSP file
	}
	public ResponseEntity<String> getSellerDashboard( RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }
	@PostMapping("/createProduct")
	@CircuitBreaker(name="ProductService",fallbackMethod="createProduct")
	public String createProduct(@ModelAttribute("product") ProductRequest productRequest){
		
		ProductResponse productResponse=productService.createProduct(productRequest);
		return "redirect:/api/product/sellerdashboard";
		
	}
	public ResponseEntity<String> createProduct(@ModelAttribute("product") ProductRequest productRequest, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }
	@GetMapping("/id")
	@CircuitBreaker(name="ProductService",fallbackMethod="getProductById")
	public ResponseEntity<ProductResponse> getProductById(@RequestParam("id") Long id){
		
		return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
		
		
		
	}
	public ResponseEntity<String> getProductById(@RequestParam Long id, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }
	@GetMapping("/updateProduct/{id}")
	@CircuitBreaker(name="ProductService",fallbackMethod="showUpdateProductForm")
	public String showUpdateProductForm(@PathVariable Long id, Model model) {
	    try {
	        ProductResponse product = productService.getProductById(id);
	        System.out.println(product);

	        if (product != null) {
	            model.addAttribute("product", product);
	            return "updateProduct"; 
	        } else {
	            model.addAttribute("message", "Product not found.");
	            return "error"; 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("message", "An error occurred.");
	        return "error"; 
	    }
	}
	public ResponseEntity<String> showUpdateProductForm(@PathVariable Long id, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }

	

	@PostMapping("/update")
	@CircuitBreaker(name="ProductService" ,fallbackMethod="updateProduct")
	public String updateProduct(
			@RequestParam("userId") String userId,
	    @RequestParam("name") String name,
	    @RequestParam("description") String description,
	    @RequestParam("imageURL") String imageURL,
	    @RequestParam("price") Double price,
	    @RequestParam("categoryId") Long categoryId,
	    @RequestParam("product_id") Long id,
	    Model model) {
	    
	    try {
	        ProductRequest productRequest = ProductRequest.builder()
	        		.userId(userId)
	                .name(name)
	                .description(description)
	                .price(price)
	                .imageURL(imageURL)
	                .categoryId(categoryId)
	                .build();
	                
	        ProductResponse result = productService.updateProductById(productRequest, id);
	        
	        model.addAttribute("message", "Product updated successfully!");
	        return "redirect:/api/product/sellerdashboard"; // Redirect to the product list page after update
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("message", "Error updating product.");
	        return "error"; // Redirect to an error page or show an error message
	    }
	}
	public ResponseEntity<String> updateProduct(@RequestParam("sellerId") String userId,
		    @RequestParam("name") String name,
		    @RequestParam("description") String description,
		    @RequestParam("imageURL") String imageURL,
		    @RequestParam("price") Double price,
		    @RequestParam("categoryId") Long categoryId,
		    @RequestParam("product_id") Long id, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }
	 
	
	@PostMapping("/deleteProduct")
	@CircuitBreaker(name="ProductService",fallbackMethod="deleteProductById")
	public String deleteProductById( @RequestParam Long id){
		boolean b=productService.deleteProductById(id);
		if(b) {
			return "redirect:/api/product/sellerdashboard";
		}
		return "";
		//return new ResponseEntity<>(productService.deleteProductById(id), HttpStatus.OK);
	}
	public ResponseEntity<String> deleteProductById(@RequestParam Long id, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }
	@GetMapping("/all")
	@CircuitBreaker(name="ProductService",fallbackMethod="getAllProducts")
	public ResponseEntity<List<ProductResponse>> getAllProducts(){
		
		return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
	}
	public ResponseEntity<String> getAllProducts( RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }
	@GetMapping("/categoryid")
	@CircuitBreaker(name="ProductService",fallbackMethod="getProductByCategoryId")
	public String  getProductByCategoryId(@RequestParam("id") Long id,Model model){
		List<Product> categoryproducts= productService.getProductByCategoryId(id);
		model.addAttribute("categoryproducts",categoryproducts);
		return "category";
	}
	public ResponseEntity<String> getProductByCategoryId(@RequestParam Long id, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }
	@GetMapping("/sellerid")
	@CircuitBreaker(name="ProductService",fallbackMethod="getProductBySellerId")
	public ResponseEntity<List<ProductResponse>> getProductByUserId(@RequestParam String id, Model model){
		
		List<ProductResponse>products=productService.getProductByUserId(id);
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	public ResponseEntity<String> getProductByUserId(@RequestParam String id, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
   }
	

}
