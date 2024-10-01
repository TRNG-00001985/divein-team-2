//package ControllerTest;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import com.cart.controller.CartController;
//import com.cart.dto.CartRequest;
//import com.cart.dto.CartResponse;
//import com.cart.service.CartService;
//
//public class CartControllerTest {
//
//    @Mock
//    private CartService cartService;
//
//    @InjectMocks
//    private CartController cartController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this); // Initialize mocks
//        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build(); // Build MockMvc for CartController
//    }
//
//    // Test for createCart()
//    @Test
//    void testCreateCart() throws Exception {
//        when(cartService.createCart(any(CartRequest.class))).thenReturn("Cart created successfully");
//
//        mockMvc.perform(post("/api/cart/create")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("productId", "1")
//                .param("userId", "user1")
//                .param("quantity", "2")
//                .param("unitPrice", "100"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("message"))
//                .andExpect(model().attribute("message", "Cart created successfully"));
//    }
//
////    // Test for getCartById()
////    @Test
////    void testGetCartById() throws Exception {
////        CartResponse cartResponse = new CartResponse();
////        cartResponse.setCartId(1L);
////        cartResponse.setProductId(1L);
////        cartResponse.setUserId("user1");
////        cartResponse.setQuantity(2);
////        cartResponse.setTotalPrice(200.0);
////
////        when(cartService.getCartById(1L)).thenReturn(Optional.of(cartResponse));
////
////        mockMvc.perform(get("/api/cart/1"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.cartId").value(1L))
////                .andExpect(jsonPath("$.productId").value(1L))
////                .andExpect(jsonPath("$.userId").value("user1"))
////                .andExpect(jsonPath("$.quantity").value(2))
////                .andExpect(jsonPath("$.totalPrice").value(200.0));
////    }
////
////    // Test for getCartById() when not found
////    @Test
////    void testGetCartById_NotFound() throws Exception {
////        when(cartService.getCartById(1L)).thenReturn(Optional.empty());
////
////        mockMvc.perform(get("/api/cart/1"))
////                .andExpect(status().isNotFound());
////    }
//
//    // Test for deleteCartById()
//    @Test
//    void testDeleteCartById() throws Exception {
//        doNothing().when(cartService).deleteCartById(1L);
//
//        mockMvc.perform(delete("/api/cart/1"))
//                .andExpect(status().isNoContent());
//
//        verify(cartService, times(1)).deleteCartById(1L);
//    }
//
//    // Test for getCartsByUserId()
//    @Test
//    void testGetCartsByUserId() throws Exception {
//        CartResponse cartResponse1 = new CartResponse();
//        cartResponse1.setCartId(1L);
//        cartResponse1.setProductId(1L);
//        cartResponse1.setUserId("user1");
//        cartResponse1.setQuantity(2);
//        cartResponse1.setTotalPrice(200.0);
//
//        CartResponse cartResponse2 = new CartResponse();
//        cartResponse2.setCartId(2L);
//        cartResponse2.setProductId(2L);
//        cartResponse2.setUserId("user1");
//        cartResponse2.setQuantity(3);
//        cartResponse2.setTotalPrice(300.0);
//
//        List<CartResponse> cartResponses = Arrays.asList(cartResponse1, cartResponse2);
//
//        when(cartService.getCartsByUserId("user1")).thenReturn(cartResponses);
//
//        mockMvc.perform(get("/api/cart/user/user1"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("cartItems"))
//                .andExpect(model().attributeExists("cartTotal"))
//                .andExpect(model().attribute("cartTotal", 500.0)); // Fix: Expected correct cart total
//    }
//    // Test for updateCartQuantity()
//    @Test
//    void testUpdateCartQuantity() throws Exception {
//        CartResponse cartResponse = new CartResponse();
//        cartResponse.setCartId(1L);
//        cartResponse.setProductId(1L);
//        cartResponse.setUserId("user1");
//        cartResponse.setQuantity(3);
//        cartResponse.setTotalPrice(300.0);
//
//        when(cartService.updateCartQuantity(anyLong(), any(CartRequest.class))).thenReturn(cartResponse);
//
//        mockMvc.perform(post("/api/cart/updateQuantity")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("productId", "1")
//                .param("quantity", "3"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("cartItems"))
//                .andExpect(model().attributeExists("cartTotal"))
//                .andExpect(model().attribute("message", "Cart updated successfully!"));
//    }
//
////    // Test for moveToFavourites()
//    @Test
//    void testMoveToFavourites() throws Exception {
//        doNothing().when(cartService).moveToFavourites(1L);
//
//        mockMvc.perform(post("/api/cart/delete/1")
//                .param("cartId", "1"))  // Fix: Use param instead of PathVariable
//                .andExpect(status().isOk());
//
//        verify(cartService, times(1)).moveToFavourites(1L);
//    }
//}
