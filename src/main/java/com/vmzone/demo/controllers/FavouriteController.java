package com.vmzone.demo.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddToFavouritesDTO;
import com.vmzone.demo.dto.ListFavouriteProductDTO;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Favourite;
import com.vmzone.demo.service.FavouritesService;
import com.vmzone.demo.utils.SessionManager;

/**
 * Rest Controller for managing favourites requests
 * 
 * @author Sabiha Djurina and Stefan Rangelov
 * 
 *
 */

@RestController
public class FavouriteController {
	
	@Autowired
	FavouritesService favouritesService;
	
	@PostMapping("/favourite")
	public Favourite addFavourite(@RequestBody @Valid AddToFavouritesDTO fav, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You are not logged in! You should log in first!");
		}
		return this.favouritesService.addToFavourites(fav, SessionManager.getLoggedUserId(session));
	}
	
	@PutMapping("/favourites/remove")
	public void removeFavourite(@RequestParam("favouriteId") long id, HttpSession session) throws BadCredentialsException, ResourceDoesntExistException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You are not logged in! You should log in first!");
		}
		 this.favouritesService.removeFavouriteById(id, SessionManager.getLoggedUserId(session));
	}
	
	@GetMapping("/favourites")
	public List<ListFavouriteProductDTO> getFavouritesForUser(HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You are not logged in! You should log in first!");
		}
		return this.favouritesService.getFavouritesForUser(SessionManager.getLoggedUserId(session));
	}

}
