package com.cart.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cart.dto.CartRequest;
import com.cart.dto.CartResponse;
import com.cart.service.CartService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;

	
	@PostMapping("/create")
	@CircuitBreaker(name="CartService",fallbackMethod="createCartFallback")
	public String createCart(@RequestBody CartRequest cartRequest, Model model) {
	    String result = cartService.createCart(cartRequest);
	    model.addAttribute("message", result);
	    return "cart"; 
	}
	public ResponseEntity<String> createCartFallback(@ModelAttribute CartRequest cartRequest, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
}


	// Get a cart by cart_id
	@GetMapping("/delete/{cart_id}")
	@CircuitBreaker(name="CartService",fallbackMethod="getCartByIdFallback")
	public ResponseEntity<CartResponse> getCartById(@PathVariable Long cartId) {
	    Optional<CartResponse> cartResponse = cartService.getCartById(cartId);
	    return cartResponse.map(ResponseEntity::ok)
	                       .orElse(ResponseEntity.notFound().build());
	}
	public ResponseEntity<String> getCartByIdFallback(@PathVariable Long cartId, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
}
	

	// Delete a cart by cart_id
	@DeleteMapping("/{cartId}")
	@CircuitBreaker(name="CartService",fallbackMethod="deleteCartByIdFallback")

	public ResponseEntity<Void> deleteCartById(@PathVariable Long cartId) {
		cartService.deleteCartById(cartId);
		return ResponseEntity.noContent().build();
	}
	public ResponseEntity<String> deleteCartByIdFallback(@PathVariable Long cartId, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
}



	
	@GetMapping("/user")
	@CircuitBreaker(name="CartService", fallbackMethod="getCartsByUserIdFallback")
	public ResponseEntity<List<CartResponse>> getCartsByUserId(@RequestParam("userId") String userId) {
        List<CartResponse> cartItems = cartService.getCartsByUserId(userId);

        // Calculate total price
        double cartTotal = cartItems.stream().mapToDouble(CartResponse::getTotalPrice).sum();
        
        // Optionally, you could wrap the response to include the total if needed
        return ResponseEntity.ok(cartItems); // Return the list of cart items as JSON
    }

	public ResponseEntity<String> getCartsByUserIdFallback(@RequestParam("userId") String userId, RuntimeException runtimeException) {
	    runtimeException.printStackTrace();
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                         .body("Something went wrong, please try again later.");
	}

	


	@PostMapping("/updateQuantity")
	@CircuitBreaker(name="CartService",fallbackMethod="updateCartQuantityFallback")
	public String updateCartQuantity(@RequestParam("productId") Long productId, 
	                                 @RequestParam("quantity") int quantity, 
	                                 Model model) {
//	    CartRequest cartRequest = new CartRequest();
//	    cartRequest.setProductId(productId);
//	    cartRequest.setQuantity(quantity);
	    
	    CartResponse updatedCart = cartService.updateCartQuantity(productId, quantity);
	    
	    // Add the updated cart details back to the model
	    List<CartResponse> cartItems = cartService.getCartsByUserId(updatedCart.getUserId());
	    model.addAttribute("cartItems", cartItems);

	    // Recalculate cart total
	    double cartTotal = cartItems.stream().mapToDouble(CartResponse::getTotalPrice).sum();
	    model.addAttribute("cartTotal", cartTotal);

	    // Optionally, add a success message
	    model.addAttribute("message", "Cart updated successfully!");

	    return "cart"; 
	}
	public ResponseEntity<String> updateCartQuantityFallback(@RequestParam("productId") Long productId, 
            @RequestParam("quantity") int quantity, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
}
	
// method move to-favourites
	@PostMapping("/favourite/{cartId}")
	@CircuitBreaker(name="CartService",fallbackMethod="moveToFavouritesFallback")
    public void moveToFavourites(@RequestParam Long cartId) {
		cartService.moveToFavourites(cartId);
	}
	public ResponseEntity<String> moveToFavouritesFallback(@RequestParam Long cartId, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try  after sometime.");
}


}

