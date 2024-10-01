package com.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.inventory.dto.InventoryRequest;
import com.inventory.dto.InventoryResponse;
import com.inventory.dto.OrderDetailsDto;
import com.inventory.model.Inventory;
import com.inventory.model.Status;
import com.inventory.repository.InventoryRepository;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddInventory() {
        InventoryRequest inventoryRequest = new InventoryRequest("user1", "SKU001", 10, Status.AVAILABLE);
        Inventory inventory = Inventory.builder()
                .id(1L)
                .userId("user1")
                .skuCode("SKU001")
                .quantity(10)
                .status(Status.AVAILABLE)
                .build();

        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        InventoryResponse response = inventoryService.addInventory(inventoryRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("SKU001", response.getSkuCode());
        assertEquals(10, response.getQuantity());
        assertEquals(Status.AVAILABLE, response.getStatus());
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void testIsInStock() {
        // Creating two OrderDetailsDto objects with id, skuCode, price, and quantity
        OrderDetailsDto orderDetails1 = new OrderDetailsDto(1L, "SKU001", 1000.0, 20);
        OrderDetailsDto orderDetails2 = new OrderDetailsDto(2L, "SKU002", 2000.0, 10);

        // Creating mock Inventory objects for the two skuCodes
        Inventory inventory1 = Inventory.builder().skuCode("SKU001").status(Status.AVAILABLE).build();
        Inventory inventory2 = Inventory.builder().skuCode("SKU002").status(Status.OUT_OF_STOCK).build();

        // Mocking repository calls for the respective skuCodes
        when(inventoryRepository.findInventoryBySkuCode("SKU001")).thenReturn(inventory1);
        when(inventoryRepository.findInventoryBySkuCode("SKU002")).thenReturn(inventory2);

        // Call the service method
        List<Boolean> inStock = inventoryService.isInStock(Arrays.asList(orderDetails1, orderDetails2));

        // Assert the results
        assertEquals(2, inStock.size());
        assertTrue(inStock.get(0));   // SKU001 is available
        assertFalse(inStock.get(1));  // SKU002 is out of stock

        // Verify that repository methods were called once for each skuCode
        verify(inventoryRepository, times(1)).findInventoryBySkuCode("SKU001");
        verify(inventoryRepository, times(1)).findInventoryBySkuCode("SKU002");
    }


    @Test
    void testGetInventoryBySkuCode() {
        Inventory inventory = Inventory.builder()
                .id(1L)
                .skuCode("SKU001")
                .quantity(10)
                .status(Status.AVAILABLE)
                .build();

        when(inventoryRepository.findInventoryBySkuCode("SKU001")).thenReturn(inventory);

        InventoryResponse response = inventoryService.getInventoryBySkuCode("SKU001");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("SKU001", response.getSkuCode());
        assertEquals(10, response.getQuantity());
        assertEquals(Status.AVAILABLE, response.getStatus());

        verify(inventoryRepository, times(1)).findInventoryBySkuCode("SKU001");
    }

    @Test
    void testUpdateStatus() {
        Inventory inventory = Inventory.builder()
                .id(1L)
                .skuCode("SKU001")
                .quantity(10)
                .status(Status.OUT_OF_STOCK)
                .build();

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        InventoryResponse response = inventoryService.updateStatus(1L, Status.AVAILABLE);

        assertNotNull(response);
        assertEquals(Status.AVAILABLE, response.getStatus());

        verify(inventoryRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    void testDeleteBySkuCode_Success() {
        Inventory inventory = Inventory.builder().skuCode("SKU001").build();

        when(inventoryRepository.findInventoryBySkuCode("SKU001")).thenReturn(inventory);
        doNothing().when(inventoryRepository).delete(any(Inventory.class));

        boolean isDeleted = inventoryService.deleteBySkuCode("SKU001");

        assertTrue(isDeleted);
        verify(inventoryRepository, times(1)).findInventoryBySkuCode("SKU001");
        verify(inventoryRepository, times(1)).delete(inventory);
    }

    @Test
    void testDeleteBySkuCode_NotFound() {
        when(inventoryRepository.findInventoryBySkuCode("SKU001")).thenReturn(null);

        boolean isDeleted = inventoryService.deleteBySkuCode("SKU001");

        assertFalse(isDeleted);
        verify(inventoryRepository, times(1)).findInventoryBySkuCode("SKU001");
        verify(inventoryRepository, never()).delete(any(Inventory.class));
    }
}
