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
public class CartRequest {
	private Long productId;
	private String skuCode;
    private String userId;
    private Double unitPrice;
	
}
