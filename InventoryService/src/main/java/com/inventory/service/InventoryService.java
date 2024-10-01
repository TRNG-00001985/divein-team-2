package com.inventory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.dto.InventoryRequest;
import com.inventory.dto.InventoryResponse;
import com.inventory.dto.OrderDetailsDto;
import com.inventory.model.Inventory;
import com.inventory.model.Status;
import com.inventory.repository.InventoryRepository;

@Service
public class InventoryService {
	private final InventoryRepository inventoryRepository;
	
	@Autowired
	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository=inventoryRepository;
	}
	
	public InventoryResponse addInventory(InventoryRequest inventoryRequest) {
		Inventory inventory= inventoryRepository.save(mapToInventory(inventoryRequest));
		return mapToInventoryResponse(inventory);
	}
	
	public Inventory mapToInventory(InventoryRequest inventory) {
		return Inventory.builder()
				.userId(inventory.getUserId())
				.skuCode(inventory.getSkuCode())
				.quantity(inventory.getQuantity())
				.status(inventory.getStatus())
				.build();
	}
	public InventoryResponse mapToInventoryResponse(Inventory inventory) {
		return InventoryResponse.builder()
				.id(inventory.getId())
				.userId(inventory.getUserId())
				.skuCode(inventory.getSkuCode())
				.status(inventory.getStatus())
				.quantity(inventory.getQuantity())
				.build();
	}
	public  List<Boolean> isInStock(List<OrderDetailsDto> orderDetails){
		List<Boolean> itemInstock=new ArrayList();
		for(OrderDetailsDto details:orderDetails) {
			Boolean status=inventoryRepository.findInventoryBySkuCode(details.getSkuCode()).getStatus().equals(Status.AVAILABLE);
			itemInstock.add(status);
		}
		return itemInstock;
	}
	
	public InventoryResponse getInventoryBySkuCode(String skuCode) {
        Inventory inventory = inventoryRepository.findInventoryBySkuCode(skuCode); 
        return inventory != null ? mapToInventoryResponse(inventory) : null; 
    }
	
	public InventoryResponse updateStatus(Long id, Status status) {
		Optional<Inventory> optionalInventory=inventoryRepository.findById(id);
		if(optionalInventory.isPresent()) {
			Inventory inventory=optionalInventory.get();
			inventory.setStatus(status);
			inventoryRepository.save(inventory);
			return mapToInventoryResponse(inventory);

		}
		return null;
	}
	
	
	public boolean deleteBySkuCode(String skuCode) {
        Inventory inventory = inventoryRepository.findInventoryBySkuCode(skuCode); 
        if (inventory != null) {
            inventoryRepository.delete(inventory); 
            return true;
        }
        return false;
    }
	
}
