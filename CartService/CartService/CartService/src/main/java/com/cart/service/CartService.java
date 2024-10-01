package com.cart.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.cart.dto.CartRequest;
import com.cart.dto.CartResponse;
import com.cart.dto.FavouriteRequest;
import com.cart.model.Cart;
import com.cart.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    @Lazy
    private FavouriteService favouriteService;

    // Create a new cart
    public String createCart(CartRequest cartRequest) {
        Cart cart = new Cart();
        int quantity=1;
        cart.setProductId(cartRequest.getProductId());
        cart.setUserId(cartRequest.getUserId());
        cart.setQuantity(quantity);
        cart.setSkuCode(cartRequest.getSkuCode());
        cart.setUnitPrice(cartRequest.getUnitPrice());
        cart.setTotalPrice(calculateTotalPrice(cart.getUnitPrice(), cart.getQuantity()));
        
        cart = cartRepository.save(cart);
        return "Cart created successfully with ID: " + cart.getCartId();
    }

    // Get a cart by ID
    public Optional<CartResponse> getCartById(Long cartId) {
        return cartRepository.findById(cartId)
                             .map(this::mapToResponse);
//                             .orElseThrow(() -> new RuntimeException("Cart with ID " + cartId + " not found"));
    }
    // Delete a cart by ID
    public void deleteCartById(Long cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
        } else {
            throw new RuntimeException("Cart with ID " + cartId + " does not exist.");
        }
    }

    // Get carts by user_id
    public List<CartResponse> getCartsByUserId(String userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        return carts.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Update cart quantity
    public CartResponse updateCartQuantity(Long cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setQuantity(quantity);
        cart.setTotalPrice(calculateTotalPrice(cart.getUnitPrice(), quantity));
        Cart updatedCart = cartRepository.save(cart);
        return mapToResponse(updatedCart);
    }
    
    //move to favourites...
    public void moveToFavourites(Long cartId) {
    	Optional<Cart> optionalCart=cartRepository.findById(cartId);
    	if(optionalCart.isPresent()) {
    		Cart cart=cartRepository.findById(cartId).get();
    		Long productId=cart.getProductId();
    		String userId=cart.getUserId();
    		FavouriteRequest favouriteRequest=new FavouriteRequest();
    		favouriteRequest.setProductId(productId);
    		favouriteRequest.setUserId(userId);
    		
    		favouriteService.addFavourite(favouriteRequest);
    		cartRepository.delete(cart);
    		
    	}
    }

    // Helper method to calculate total price
    private Double calculateTotalPrice(Double unitPrice, int quantity) {
        return unitPrice * quantity;
    }

    // Helper method to convert Cart to CartResponse
    private CartResponse mapToResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getCartId());
        response.setProductId(cart.getProductId());
        response.setUserId(cart.getUserId());
        response.setSkuCode(cart.getSkuCode());
        response.setQuantity(cart.getQuantity());
        response.setUnitPrice(cart.getUnitPrice());
        response.setTotalPrice(cart.getTotalPrice());
        return response;
    }
}
