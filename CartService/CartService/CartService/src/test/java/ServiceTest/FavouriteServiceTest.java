//package ServiceTest;
//
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
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
//import com.cart.dto.FavouriteResponse;
//import com.cart.model.Favourites;
//import com.cart.repository.FavouriteRepository;
//import com.cart.service.CartService;
//import com.cart.service.FavouriteService;
//
//public class FavouriteServiceTest {
//
//    @Mock
//    private FavouriteRepository favouriteRepository;
//
//    @Mock
//    private CartService cartService;
//
//    @InjectMocks
//    private FavouriteService favouriteService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this); // Initialize mocks
//    }
//
//    // Test for addFavourite() method
//    @Test
//    void testAddFavourite() {
//        Favourites favourite = new Favourites();
//        favourite.setProductId(1L);
//        favourite.setUserId("user1");
//
//        Favourites savedFavourite = new Favourites();
//        savedFavourite.setFavouriteId(1L);
//        savedFavourite.setProductId(1L);
//        savedFavourite.setUserId("user1");
//
//        when(favouriteRepository.save(any(Favourites.class))).thenReturn(savedFavourite);
//
//        FavouriteResponse response = favouriteService.addFavourite(1L, "user1");
//
//        assertNotNull(response);
//        assertEquals(1L, response.getProductId());
//        assertEquals("user1", response.getUserId());
//    }
//
//    // Test for getFavouritesByUserId() method
//    @Test
//    void testGetFavouritesByUserId() {
//        Favourites favourite1 = new Favourites();
//        favourite1.setFavouriteId(1L);
//        favourite1.setProductId(1L);
//        favourite1.setUserId("user1");
//
//        Favourites favourite2 = new Favourites();
//        favourite2.setFavouriteId(2L);
//        favourite2.setProductId(2L);
//        favourite2.setUserId("user1");
//
//        when(favouriteRepository.findByUserId("user1")).thenReturn(Arrays.asList(favourite1, favourite2));
//
//        List<FavouriteResponse> favourites = favouriteService.getFavouritesByUserId("user1");
//
//        assertNotNull(favourites);
//        assertEquals(2, favourites.size());
//        assertEquals(1L, favourites.get(0).getProductId());
//        assertEquals(2L, favourites.get(1).getProductId());
//    }
//
//    // Test for deleteFavouriteById() method
//    @Test
//    void testDeleteFavouriteById() {
//        when(favouriteRepository.existsById(1L)).thenReturn(true);
//
//        favouriteService.deleteFavouriteById(1L);
//
//        verify(favouriteRepository, times(1)).deleteById(1L);
//    }
//
//    // Test for deleteFavouriteById() when ID does not exist
//    @Test
//    void testDeleteFavouriteById_FavouriteNotFound() {
//        when(favouriteRepository.existsById(1L)).thenReturn(false);
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            favouriteService.deleteFavouriteById(1L);
//        });
//
//        assertEquals("Favourite with ID 1 does not exist.", exception.getMessage());
//    }
//
//    // Test for moveToCart() method
//    @Test
//    void testMoveToCart() {
//        Favourites favourite = new Favourites();
//        favourite.setFavouriteId(1L);
//        favourite.setProductId(1L);
//        favourite.setUserId("user1");
//
//        when(favouriteRepository.findById(1L)).thenReturn(Optional.of(favourite));
//
//        favouriteService.moveToCart(1L);
//
//        verify(cartService, times(1)).createCart(any(CartRequest.class));
//        verify(favouriteRepository, times(1)).deleteById(1L);
//    }
//
//    // Test for moveToCart() when favourite is not found
//    @Test
//    void testMoveToCart_FavouriteNotFound() {
//        when(favouriteRepository.findById(1L)).thenReturn(Optional.empty());
//
//        favouriteService.moveToCart(1L);
//
//        verify(cartService, times(0)).createCart(any(CartRequest.class));
//        verify(favouriteRepository, times(0)).deleteById(1L);
//    }
//
//    // Helper method to create a Favourite object for testing
//    private Favourites createTestFavourite(Long favouriteId, String userId, Long productId) {
//        Favourites favourite = new Favourites();
//        favourite.setFavouriteId(favouriteId);
//        favourite.setUserId(userId);
//        favourite.setProductId(productId);
//        return favourite;
//    }
//}
