package com.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.dto.InventoryRequest;
import com.inventory.dto.InventoryResponse;
import com.inventory.dto.OrderDetailsDto;
import com.inventory.model.Inventory;
import com.inventory.model.Status;
import com.inventory.service.InventoryService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	private final InventoryService inventoryService;
	
	@Autowired
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService=inventoryService;
	}
	
	@PostMapping("/add")
	@CircuitBreaker(name="InventoryService",fallbackMethod="createInventoryFallback")
	public ResponseEntity<InventoryResponse> addInventory(@RequestParam("userId") String userId,
						@RequestParam ("skuCode") String skuCode,
						@RequestParam("quantity") Integer quantity ){
		
		try {
			Status status;
			if(quantity>0) {
				status=Status.AVAILABLE;
			}
			else {
				 status=Status.OUT_OF_STOCK;
			}
			InventoryRequest inventory= InventoryRequest.builder()
					.userId(userId)
					.skuCode(skuCode)
					.status(status)
					.quantity(quantity).build();
		return new ResponseEntity<>(inventoryService.addInventory(inventory), HttpStatus.CREATED);
		}catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
		
	}
	 public ResponseEntity<String> createInventoryFallback(InventoryRequest inventoryRequest, RuntimeException runtimeException) {
	        runtimeException.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Something went wrong, please try  after sometime.");
	}
		
	
	    // 2. check stock  by SKU Code
	@PostMapping("/instock")
	@CircuitBreaker(name="InventoryService",fallbackMethod="isInStockFallback")
	public ResponseEntity<List<Boolean>> isInStock(@RequestBody List<OrderDetailsDto> orderLineItems){
		
		return new ResponseEntity<>(inventoryService.isInStock(orderLineItems), HttpStatus.OK);
		
	}
	 public ResponseEntity<String> isInStockFallback(InventoryRequest inventoryRequest, RuntimeException runtimeException) {
	        runtimeException.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Something went wrong, please try  after sometime.");
	}
	 
	  // 3. Update Availability
	 
	 @PatchMapping("/{id}/availability")
	 @CircuitBreaker(name="InventoryService",fallbackMethod="updateAvailabilityFallback")
	 public ResponseEntity<InventoryResponse> updateAvailability(@RequestParam Long id, @RequestParam Status availability) {
	      try {
	          InventoryResponse updatedInventory = inventoryService.updateStatus(id, availability);

	            if (updatedInventory != null) {
	                return ResponseEntity.ok(updatedInventory);
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	            }
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }
	    }
	 public ResponseEntity<String> updateAvailabilityFallback(@PathVariable Long id, @RequestBody Status availability,RuntimeException runtimeException) {
	        runtimeException.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Something went wrong, please try  after sometime.");
	}
	
	// 4. Delete Product by SKU Code
	 	@DeleteMapping("/delete/{skuCode}")
	    @CircuitBreaker(name = "InventoryService", fallbackMethod = "deleteProductBySkuCodeFallback")
	    public ResponseEntity<Void> deleteProductBySkuCode(@PathVariable String skuCode) {
	        boolean isDeleted = inventoryService.deleteBySkuCode(skuCode);
	        if (isDeleted) {
	            return ResponseEntity.noContent().build(); 
	        } else {
	            return ResponseEntity.notFound().build(); 
	        }
	    }
	    
	    public ResponseEntity<String> deleteProductBySkuCodeFallback(@PathVariable String skuCode, RuntimeException runtimeException) {
	        runtimeException.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Something went wrong, please try  after sometime.");
	    }
	    
	    // 5. Get Inventory by SKU Code
	    @GetMapping("/{skuCode}")
	    @CircuitBreaker(name="InventoryService",fallbackMethod="getInventoryBySkuCodeFallback")
	    public ResponseEntity<InventoryResponse> getInventoryBySkuCode(@PathVariable String skuCode) {
	        try {
	            InventoryResponse inventoryResponse = inventoryService.getInventoryBySkuCode(skuCode);
	            return ResponseEntity.ok(inventoryResponse);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	    }
	    
	    public ResponseEntity<String> getInventoryBySkuCodeFallback(@PathVariable String skuCode, RuntimeException runtimeException) {
	        runtimeException.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Something went wrong, please try  after sometime.");
	   }
}
