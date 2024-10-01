package com.inventory.dto;



import com.inventory.model.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
	private Long id;
	private String userId;
	private String skuCode;
	private Integer quantity;
	private Status status;


}
