package ControllerTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cart.controller.FavouriteController;
import com.cart.dto.FavouriteResponse;
import com.cart.service.FavouriteService;

public class FavouriteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavouriteService favouriteService;

    @InjectMocks
    private FavouriteController favouriteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(favouriteController).build();
    }

    // Test case for adding a favourite
//    @Test
//    void testAddFavourite() throws Exception {
//        mockMvc.perform(post("/add")
//                .param("productId", "1")
//                .param("userId", "user1")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().is3xxRedirection())  // Expect a redirection on success
//                .andExpect(redirectedUrl("/favourites/view?userId=user1"));
//
//        // Verify the service method is called correctly
//        verify(favouriteService, times(1)).addFavourite(1L, "user1");
//    }

    // Test case for retrieving favourites
    @Test
    void testGetFavourites() throws Exception {
        FavouriteResponse favourite1 = new FavouriteResponse(1L, 1L, "user1");
        FavouriteResponse favourite2 = new FavouriteResponse(2L, 2L, "user1");
        List<FavouriteResponse> favourites = Arrays.asList(favourite1, favourite2);

        when(favouriteService.getFavouritesByUserId("user1")).thenReturn(favourites);

        mockMvc.perform(get("/favourites/view")
                .param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("favourites"))
                .andExpect(model().attribute("userId", "user1"))
                .andExpect(model().attribute("favourites", favourites));

        verify(favouriteService, times(1)).getFavouritesByUserId("user1");
    }

    // Test case for deleting a favourite
    @Test
    void testDeleteFavourite() throws Exception {
        mockMvc.perform(post("/delete/1")  // Matches @PostMapping("/delete/{favouriteId}")
                .param("userId", "user1"));
        
               
}
    // Test for moveToCart() method
//    @Test
//    void testMoveToCart() throws Exception {
//        mockMvc.perform(post("/favourites/moveToCart/1")
//                .param("userId", "user1")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/favourites/view?userId=user1"));
//
//        verify(favouriteService, times(1)).moveToCart(1L);
//    }
}