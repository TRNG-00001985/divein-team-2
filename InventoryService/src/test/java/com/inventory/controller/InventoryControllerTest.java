package com.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.dto.InventoryRequest;
import com.inventory.dto.InventoryResponse;
import com.inventory.dto.OrderDetailsDto;
import com.inventory.model.Status;
import com.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    // Set up common mock data before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for addInventory
    @Test
    void testAddInventory() throws Exception {
        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .userId("user1")
                .skuCode("SKU001")
                .status(Status.AVAILABLE)
                .quantity(10)
                .build();

        InventoryResponse inventoryResponse = InventoryResponse.builder()
                .id(1L)
                .userId("user1")
                .skuCode("SKU001")
                .status(Status.AVAILABLE)
                .quantity(10)
                .build();

        when(inventoryService.addInventory(any(InventoryRequest.class))).thenReturn(inventoryResponse);

        mockMvc.perform(post("/api/inventory/add/")
                        .param("userId", "user1")
                        .param("skuCode", "SKU001")
                        .param("quantity", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.skuCode").value("SKU001"));

        verify(inventoryService, times(1)).addInventory(any(InventoryRequest.class));
    }

    // Test case for isInStock
    @Test
    void testIsInStock() throws Exception {
        List<OrderDetailsDto> orderDetailsList = Arrays.asList(
                new OrderDetailsDto(1L, "SKU001", 100.0, 5),
                new OrderDetailsDto(2L, "SKU002", 200.0, 3)
        );

        List<Boolean> stockStatus = Arrays.asList(true, false);

        when(inventoryService.isInStock(anyList())).thenReturn(stockStatus);

        mockMvc.perform(post("/api/inventory/instock/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDetailsList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(true))
                .andExpect(jsonPath("$[1]").value(false));

        verify(inventoryService, times(1)).isInStock(anyList());
    }

    // Test case for updateAvailability
    @Test
    void testUpdateAvailability() throws Exception {
        InventoryResponse inventoryResponse = InventoryResponse.builder()
                .id(1L)
                .userId("user1")
                .skuCode("SKU001")
                .status(Status.AVAILABLE)
                .quantity(10)
                .build();

        when(inventoryService.updateStatus(eq(1L), eq(Status.AVAILABLE))).thenReturn(inventoryResponse);

        mockMvc.perform(patch("/api/inventory/1/availability")
                        .param("id", "1")
                        .param("availability", "AVAILABLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.skuCode").value("SKU001"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        verify(inventoryService, times(1)).updateStatus(eq(1L), eq(Status.AVAILABLE));
    }

    // Test case for deleteProductBySkuCode
    @Test
    void testDeleteProductBySkuCode() throws Exception {
        when(inventoryService.deleteBySkuCode("SKU001")).thenReturn(true);

        mockMvc.perform(delete("/api/inventory/delete/SKU001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(inventoryService, times(1)).deleteBySkuCode("SKU001");
    }

    // Test case for getInventoryBySkuCode
    @Test
    void testGetInventoryBySkuCode() throws Exception {
        InventoryResponse inventoryResponse = InventoryResponse.builder()
                .id(1L)
                .userId("user1")
                .skuCode("SKU001")
                .status(Status.AVAILABLE)
                .quantity(10)
                .build();

        when(inventoryService.getInventoryBySkuCode("SKU001")).thenReturn(inventoryResponse);

        mockMvc.perform(get("/api/inventory/SKU001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.skuCode").value("SKU001"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        verify(inventoryService, times(1)).getInventoryBySkuCode("SKU001");
    }
}
