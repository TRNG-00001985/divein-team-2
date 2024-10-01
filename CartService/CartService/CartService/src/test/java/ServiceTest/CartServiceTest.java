//package ServiceTest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
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
//
//import com.cart.dto.CartRequest;
//import com.cart.dto.CartResponse;
//import com.cart.model.Cart;
//import com.cart.repository.CartRepository;
//import com.cart.service.CartService;
//import com.cart.service.FavouriteService;
//
//public class CartServiceTest {
//	@Mock
//    private CartRepository cartRepository;
//
//    @Mock
//    private FavouriteService favouriteService;
//
//    @InjectMocks
//    private CartService cartService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this); // Initialize mocks
//    }
//
//    // Test for createCart() method
//    @Test
//    void testCreateCart() {
//        CartRequest request = new CartRequest();
//        request.setProductId(1L);
//        request.setUserId("user1");
//        request.setQuantity(2);
//        request.setSkuCode("sku123");
//        request.setUnitPrice(100.0);
//
//        Cart savedCart = new Cart();
//        savedCart.setCartId(1L);
//        savedCart.setProductId(1L);
//        savedCart.setUserId("user1");
//        savedCart.setQuantity(2);
//        savedCart.setSkuCode("sku123");
//        savedCart.setUnitPrice(100.0);
//        savedCart.setTotalPrice(200.0);
//
//        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);
//
//        String result = cartService.createCart(request);
//
//        assertNotNull(result);
//        assertEquals("Cart created successfully with ID: 1", result);
//    }
//
//    // Test for getCartById() method
//    @Test
//    void testGetCartById() {
//        Cart cart = new Cart();
//        cart.setCartId(1L);
//        cart.setProductId(1L);
//        cart.setUserId("user1");
//        cart.setQuantity(2);
//        cart.setUnitPrice(100.0);
//        cart.setTotalPrice(200.0);
//
//        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
//
//        Optional<CartResponse> response = cartService.getCartById(1L);
//
//        assertTrue(response.isPresent());
//        assertEquals(1L, response.get().getCartId());
//        assertEquals(200.0, response.get().getTotalPrice());
//    }
//
//    // Test for getCartById() when cart is not found
//    @Test
//    void testGetCartById_NotFound() {
//        when(cartRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Optional<CartResponse> response = cartService.getCartById(1L);
//
//        assertFalse(response.isPresent());
//    }
//
//    // Test for deleteCartById() method
//    @Test
//    void testDeleteCartById() {
//        when(cartRepository.existsById(1L)).thenReturn(true);
//
//        cartService.deleteCartById(1L);
//
//        verify(cartRepository, times(1)).deleteById(1L);
//    }
//
//    // Test for deleteCartById() when cart is not found
//    @Test
//    void testDeleteCartById_CartNotFound() {
//        when(cartRepository.existsById(1L)).thenReturn(false);
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            cartService.deleteCartById(1L);
//        });
//
//        assertEquals("Cart with ID 1 does not exist.", exception.getMessage());
//    }
//
//    // Test for getCartsByUserId() method
//    @Test
//    void testGetCartsByUserId() {
//        Cart cart1 = new Cart();
//        cart1.setCartId(1L);
//        cart1.setProductId(1L);
//        cart1.setUserId("user1");
//        cart1.setQuantity(2);
//        cart1.setUnitPrice(100.0);
//        cart1.setTotalPrice(200.0);
//
//        Cart cart2 = new Cart();
//        cart2.setCartId(2L);
//        cart2.setProductId(2L);
//        cart2.setUserId("user1");
//        cart2.setQuantity(1);
//        cart2.setUnitPrice(50.0);
//        cart2.setTotalPrice(50.0);
//
//        when(cartRepository.findByUserId("user1")).thenReturn(Arrays.asList(cart1, cart2));
//
//        List<CartResponse> responses = cartService.getCartsByUserId("user1");
//
//        assertEquals(2, responses.size());
//        assertEquals(200.0, responses.get(0).getTotalPrice());
//        assertEquals(50.0, responses.get(1).getTotalPrice());
//    }
//
//    // Test for updateCartQuantity() method
//    @Test
//    void testUpdateCartQuantity() {
//        CartRequest request = new CartRequest();
//        request.setQuantity(3);
//
//        Cart cart = new Cart();
//        cart.setCartId(1L);
//        cart.setProductId(1L);
//        cart.setUserId("user1");
//        cart.setQuantity(2);
//        cart.setUnitPrice(100.0);
//        cart.setTotalPrice(200.0);
//
//        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
//        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
//
//        CartResponse updatedResponse = cartService.updateCartQuantity(1L, request);
//
//        assertEquals(3, updatedResponse.getQuantity());
//        assertEquals(300.0, updatedResponse.getTotalPrice());
//    }
//
//    // Test for moveToFavourites() method
//    @Test
//    void testMoveToFavourites() {
//        Cart cart = new Cart();
//        cart.setCartId(1L);
//        cart.setProductId(1L);
//        cart.setUserId("user1");
//
//        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
//
//        cartService.moveToFavourites(1L);
//
//        verify(favouriteService, times(1)).addFavourite(1L, "user1");
//        verify(cartRepository, times(1)).delete(cart);
//    }
//
//    // Helper method to create a Cart object for testing
//    private Cart createTestCart(Long cartId, String userId, Long productId, int quantity, Double unitPrice) {
//        Cart cart = new Cart();
//        cart.setCartId(cartId);
//        cart.setUserId(userId);
//        cart.setProductId(productId);
//        cart.setQuantity(quantity);
//        cart.setUnitPrice(unitPrice);
//        cart.setTotalPrice(unitPrice * quantity);
//        return cart;
//    }
//}
//
