package com.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartResponse {
	private Long cartId; 
    private Long productId;
    private String skuCode;
    private String userId;
    private int quantity;
    private Double unitPrice;
    private Double totalPrice;
	

}
