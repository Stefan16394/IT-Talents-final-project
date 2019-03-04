package com.vmzone.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddToFavouritesDTO;
import com.vmzone.demo.dto.ListFavouriteProductDTO;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.service.FavouritesService;

@RestController
public class FavouriteController {
	
	@Autowired
	FavouritesService favouritesService;
	
	@PostMapping("/favourite")
	public void addFavourite(@RequestBody AddToFavouritesDTO fav) {
		this.favouritesService.addToFavourites(fav);
	}
	
	@PutMapping("/favourites/remove/{id}")
	public void removeFavourite(@PathVariable long id) throws ResourceDoesntExistException {
		 this.favouritesService.removeFavouriteById(id);
	}
	
	@GetMapping("/favourites/{id}")
	public List<ListFavouriteProductDTO> getFavouritesForUser(@PathVariable long id) {
		return this.favouritesService.getFavouritesForUser(id);
	}

}
