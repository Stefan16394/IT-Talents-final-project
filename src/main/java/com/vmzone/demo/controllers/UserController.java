package com.vmzone.demo.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vmzone.demo.dto.CartProductDTO;
import com.vmzone.demo.dto.ChangePasswordDTO;
import com.vmzone.demo.dto.ContactUsDTO;
import com.vmzone.demo.dto.EditProfileDTO;
import com.vmzone.demo.dto.LoginDTO;
import com.vmzone.demo.dto.RegisterDTO;
import com.vmzone.demo.dto.ShoppingCartItem;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.InvalidEmailException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.User;
import com.vmzone.demo.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/user/register")
	public void registerUser(@RequestBody @Valid  RegisterDTO user, BindingResult result) throws ResourceAlreadyExistsException, SQLException, AddressException, InvalidEmailException, MessagingException, IOException {
		UserValidator userValidator = new UserValidator();
		userValidator.validate(user, result);

		if (!result.hasErrors()) {
			this.userService.register(user);
		} else {
			ObjectError error = result.getAllErrors().stream().findFirst().get();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getDefaultMessage());
		}
	}

	@PostMapping("/user/login")
	public User  login(@RequestBody @Valid LoginDTO loginDTO, HttpSession session)
			throws ResourceDoesntExistException, BadCredentialsException {
		User user = this.userService.login(loginDTO);
		
		session.setAttribute("user", user);
        return user;
	}
	
	@GetMapping("/cart")
	public List<ShoppingCartItem> getShoppingCart(HttpSession session) throws ResourceDoesntExistException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		return this.userService.getShoppingCart(((User) session.getAttribute("user")).getUserId());
	}
	
	@PostMapping("/product/cart")
	public void addProductToCart(@RequestBody CartProductDTO addProduct, HttpSession session) throws ResourceDoesntExistException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		this.userService.addProductToCart(addProduct, ((User) session.getAttribute("user")).getUserId());
	}
	
	@PutMapping("/product/cart/update")
	public void updateProductInCart(@RequestBody CartProductDTO editProduct, HttpSession session) throws ResourceDoesntExistException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		this.userService.updateProductInCart(editProduct, ((User) session.getAttribute("user")).getUserId());
	}
	
	@DeleteMapping("/product/cart/delete")
	public void deleteProductInCart(@RequestParam("product") long productId,HttpSession session) throws ResourceDoesntExistException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		this.userService.deleteProductInCart(productId, ((User) session.getAttribute("user")).getUserId());
	}
	
	
	@PutMapping("/editProfile")
	public User editProfile(@RequestBody @Valid EditProfileDTO user, HttpSession session) throws ResourceDoesntExistException, ResourceAlreadyExistsException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		
		return this.userService.editProfile(((User) session.getAttribute("user")).getUserId(), user);
	}
	
	@PostMapping("/changePassword")
	public void changePassword(@RequestBody @Valid ChangePasswordDTO pass, HttpSession session) throws ResourceDoesntExistException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		
		
		this.userService.changePassword(((User) session.getAttribute("user")).getUserId(), pass);
	}
	
	@PostMapping("/forgottenPassword")
	public void forgottenPassword(@RequestParam("email") String email) throws AddressException, ResourceDoesntExistException, InvalidEmailException, MessagingException, IOException {
		this.userService.forgottenPassword(email);
	}
	
	//TODO threads
	@PostMapping("/sendSubscribed")
	public void sendSubcribed(HttpSession session) throws AddressException, ResourceDoesntExistException, InvalidEmailException, MessagingException, IOException, BadCredentialsException {
		
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
		this.userService.sendSubscribed();
	}
	
	
	@PostMapping("/contactUs")
	public void contactUs(@RequestBody ContactUsDTO contact) throws InvalidEmailException, AddressException, MessagingException, IOException {
		this.userService.contactUs(contact);
	}
	
	@PutMapping("/user/remove/{id}")
	public void removeUser(@PathVariable long id, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
		
		this.userService.removeUserById(id);
	}
	
	@PostMapping("/user/logout")
	public void logout(HttpSession session) throws ResourceDoesntExistException {
		
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		
		session.invalidate();
	}
	
	
	
	
}
