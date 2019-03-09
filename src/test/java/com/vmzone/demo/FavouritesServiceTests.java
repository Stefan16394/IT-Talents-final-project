package com.vmzone.demo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import com.vmzone.demo.dto.AddToFavouritesDTO;
import com.vmzone.demo.dto.ListFavouriteProductDTO;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.models.Favourite;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.User;
import com.vmzone.demo.repository.FavouritesRepository;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.repository.UserRepository;
import com.vmzone.demo.service.FavouritesService;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class FavouritesServiceTests {

	private static final User TEST_USER = new User(1L, "User", "User", "user@abv.bg", "1234", "male", 0,  null, null, null, null, 25, 0);

	private static final Product TEST_PRODUCT = new Product(new Category("Shoes",null), "Product", "Information", 1, 24, 20, 1, "Details");

	private static final long DEFAULT_ID_TO_SEARCH = 1L;

	@Mock
	private FavouritesRepository favouritesRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private FavouritesService favouritesService;

	@Test(expected = ResourceDoesntExistException.class)
	public void testAddToFavouritesWhenProductDoesntExist() throws ResourceDoesntExistException {
		when(productRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(Optional.empty());
		favouritesService.addToFavourites(new AddToFavouritesDTO(), DEFAULT_ID_TO_SEARCH);
	}
	
	@Test(expected = ResourceDoesntExistException.class)
	public void testAddToFavouritesWhenUserDoesntExist() throws ResourceDoesntExistException {
		when(userRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(Optional.empty());
		favouritesService.addToFavourites(new AddToFavouritesDTO(), DEFAULT_ID_TO_SEARCH);
	}
	
	@Test
	public void testAddToFavouritesWithCorrectInput() throws ResourceDoesntExistException {
		when(productRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(Optional.of(TEST_PRODUCT));
		when(userRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(Optional.of(TEST_USER));
		favouritesService.addToFavourites(new AddToFavouritesDTO(DEFAULT_ID_TO_SEARCH), DEFAULT_ID_TO_SEARCH);
	}
	
	@Test(expected = ResourceDoesntExistException.class)
	public void testRemoveFavouriteByIdWhenFavouriteDoesntExist() throws ResourceDoesntExistException {
		when(favouritesRepository.findFavouriteFoUser(DEFAULT_ID_TO_SEARCH, DEFAULT_ID_TO_SEARCH)).thenReturn(null);
		favouritesService.removeFavouriteById(DEFAULT_ID_TO_SEARCH, DEFAULT_ID_TO_SEARCH);
	}
	
	@Test
	public void testRemoveFavouriteByIdWhenFavouriteExists() throws ResourceDoesntExistException {
		Favourite fav =new Favourite(TEST_PRODUCT, TEST_USER);
		when(favouritesRepository.findFavouriteFoUser(DEFAULT_ID_TO_SEARCH, DEFAULT_ID_TO_SEARCH)).thenReturn(fav);
		favouritesService.removeFavouriteById(DEFAULT_ID_TO_SEARCH, DEFAULT_ID_TO_SEARCH);
		assertEquals(1,fav.getIsDeleted());
	}
	
	@Test
	public void testGetFavouritesForUser() {
		List<Favourite>	favourites = new ArrayList<>();
		favourites.add(new Favourite(TEST_PRODUCT, TEST_USER));
		favourites.add(new Favourite(TEST_PRODUCT, TEST_USER));
		favourites.add(new Favourite(TEST_PRODUCT,TEST_USER));
		when(favouritesRepository.findFavouritesForUser(DEFAULT_ID_TO_SEARCH)).thenReturn(favourites);
		List<ListFavouriteProductDTO> result = favouritesService.getFavouritesForUser(DEFAULT_ID_TO_SEARCH);
		assertEquals(favourites.size(),result.size());
	}

	@Configuration
	static class Config {

	}
}
