package com.vmzone.demo.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddToFavouritesDTO;
import com.vmzone.demo.dto.ListFavouriteProductDTO;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.User;
import com.vmzone.demo.service.FavouritesService;

@RestController
public class FavouriteController {
	
	@Autowired
	FavouritesService favouritesService;
	
	@PostMapping("/favourite")
	public void addFavourite(@RequestBody AddToFavouritesDTO fav, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (session.getAttribute("user") == null) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You are not logged in! You should log in first!");
		}
		this.favouritesService.addToFavourites(fav, ((User) session.getAttribute("user")).getUserId());
	}
	
	@PutMapping("/favourites/remove/{id}")
	public void removeFavourite(@PathVariable long id, HttpSession session) throws BadCredentialsException, ResourceDoesntExistException {
		if (session.getAttribute("user") == null) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You are not logged in! You should log in first!");
		}
		 this.favouritesService.removeFavouriteById(id, ((User) session.getAttribute("user")).getUserId());
	}
	
	@GetMapping("/favourites")
	public List<ListFavouriteProductDTO> getFavouritesForUser(HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (session.getAttribute("user") == null) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You are not logged in! You should log in first!");
		}
		return this.favouritesService.getFavouritesForUser(((User) session.getAttribute("user")).getUserId());
	}

}
