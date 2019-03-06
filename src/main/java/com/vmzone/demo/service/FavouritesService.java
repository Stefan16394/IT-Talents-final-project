package com.vmzone.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddToFavouritesDTO;
import com.vmzone.demo.dto.ListFavouriteProductDTO;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Favourite;
import com.vmzone.demo.repository.FavouritesRepository;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.repository.UserRepository;

@Service
public class FavouritesService {
	
	@Autowired
	private FavouritesRepository favouritesRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public void addToFavourites(AddToFavouritesDTO fav ) {
		System.out.println(fav);
		Favourite newFav = new Favourite(
				this.productRepository.findById(fav.getProductId()).get(),
				this.userRepository.findById(fav.getUserId()).get());
		this.favouritesRepository.save(newFav);
	}
	
	public void removeFavouriteById(long id) throws ResourceDoesntExistException {
		Favourite fav = this.favouritesRepository.findById(id);
		if(fav == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist");
		}
		fav.setIsDeleted(1);
		this.favouritesRepository.save(fav);
	}
	
	public List<ListFavouriteProductDTO> getFavouritesForUser(long id) {
		return this.favouritesRepository.findAll().stream()
				.filter(fav -> fav.getFavouritesId() != null && fav.getUser().getUserId().equals(id))
				.map(fav -> new ListFavouriteProductDTO(fav.getProduct().getTitle(), fav.getProduct().getInformation(), fav.getProduct().getRating()))
				.collect(Collectors.toList());
	}
	
	

}
