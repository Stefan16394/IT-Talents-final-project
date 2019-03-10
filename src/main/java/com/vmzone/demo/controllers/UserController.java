package com.vmzone.demo.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.CartProductDTO;
import com.vmzone.demo.dto.ChangePasswordDTO;
import com.vmzone.demo.dto.ContactUsDTO;
import com.vmzone.demo.dto.EditProfileDTO;
import com.vmzone.demo.dto.ForgottenPasswordDTO;
import com.vmzone.demo.dto.LoginDTO;
import com.vmzone.demo.dto.RegisterDTO;
import com.vmzone.demo.dto.ShoppingCartItem;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.InvalidEmailException;
import com.vmzone.demo.exceptions.NotEnoughQuantityException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.User;
import com.vmzone.demo.service.UserService;
import com.vmzone.demo.utils.SessionManager;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/user/register")
	public User registerUser(@RequestBody @Valid  RegisterDTO user) throws ResourceAlreadyExistsException, SQLException, AddressException, InvalidEmailException, MessagingException, IOException {
			return this.userService.register(user);
	}

	@PostMapping("/user/login")
	public User  login(@RequestBody @Valid LoginDTO loginDTO, HttpSession session)
			throws ResourceDoesntExistException, BadCredentialsException {
		User user = this.userService.login(loginDTO);
		
		session.setAttribute("user", user);
        return user;
	}
	
	@GetMapping("/cart")
	public List<ShoppingCartItem> getShoppingCart(HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		return this.userService.getShoppingCart(SessionManager.getLoggedUserId(session));
	}
	
	@PostMapping("/product/cart")
	public long addProductToCart(@RequestBody @Valid CartProductDTO addProduct, HttpSession session) throws ResourceDoesntExistException, NotEnoughQuantityException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		return this.userService.addProductToCart(addProduct,SessionManager.getLoggedUserId(session));
	}
	
	@PutMapping("/product/cart/update")
	public long updateProductInCart(@RequestBody @Valid CartProductDTO editProduct, HttpSession session) throws ResourceDoesntExistException, NotEnoughQuantityException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		return this.userService.updateProductInCart(editProduct, SessionManager.getLoggedUserId(session));
	}
	
	@DeleteMapping("/product/cart/delete")
	public void deleteProductInCart(@RequestParam("product") long productId,HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		this.userService.deleteProductInCart(productId, SessionManager.getLoggedUserId(session));
	}
	
	
	@PutMapping("/editProfile")
	public User editProfile(@RequestBody @Valid EditProfileDTO user, HttpSession session) throws ResourceDoesntExistException, ResourceAlreadyExistsException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		return this.userService.editProfile(SessionManager.getLoggedUserId(session), user);
	}
	
	@PostMapping("/changePassword")
	public void changePassword(@RequestBody @Valid ChangePasswordDTO pass, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		this.userService.changePassword(SessionManager.getLoggedUserId(session), pass);
	}
	
	@PostMapping("/forgottenPassword")
	public void forgottenPassword(@RequestBody ForgottenPasswordDTO pass) throws AddressException, ResourceDoesntExistException, InvalidEmailException, MessagingException, IOException {
		this.userService.forgottenPassword(pass.getEmail());
	}
	
	//TODO threads
	@PostMapping("/sendSubscribed")
	public void sendSubcribed(HttpSession session) throws AddressException, ResourceDoesntExistException, InvalidEmailException, MessagingException, IOException, BadCredentialsException {
		
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
		}
		this.userService.sendSubscribed();
	}
	
	
	@PostMapping("/contactUs")
	public void contactUs(@RequestBody ContactUsDTO contact) throws InvalidEmailException, AddressException, MessagingException, IOException {
		this.userService.contactUs(contact);
	}
	
	@PutMapping("/user/remove/{id}")
	public void removeUser(@PathVariable long id, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
		}
		
		this.userService.removeUserById(id);
	}
	
	@PostMapping("/user/logout")
	public void logout(HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You are not logged in! You should log in first!");
		}
		
		session.invalidate();
	}	
}
