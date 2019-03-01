package com.vmzone.demo.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vmzone.demo.dto.ChangePasswordDTO;
import com.vmzone.demo.dto.EditProfileDTO;
import com.vmzone.demo.dto.LoginDTO;
import com.vmzone.demo.dto.RegisterDTO;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.User;
import com.vmzone.demo.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/user/register")
	public void registerUser(@RequestBody @Valid  RegisterDTO user, BindingResult result) throws ResourceAlreadyExistsException, SQLException {
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
	public void login(@RequestBody @Valid LoginDTO loginDTO, HttpSession session)
			throws ResourceDoesntExistException, BadCredentialsException {
		User user = this.userService.login(loginDTO);
		session.setAttribute("user", user);

	}
	
	@PostMapping("/editProfile/{id}")
	public User editProfile(@PathVariable long id, @RequestBody @Valid EditProfileDTO user, HttpSession session) throws ResourceDoesntExistException {
		if (session.getAttribute("userId") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		
		return this.userService.editProfile(id, user);
	}
	
	@PostMapping("/changePassword/{id}")
	public void changePassword(@PathVariable long id, @RequestBody @Valid ChangePasswordDTO pass, HttpSession session) throws ResourceDoesntExistException {
		if (session.getAttribute("userId") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		
		this.userService.changePassword(id, pass);
	}
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
	}
}
