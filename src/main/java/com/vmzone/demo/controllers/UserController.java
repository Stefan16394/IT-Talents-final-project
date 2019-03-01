package com.vmzone.demo.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
	public void registerUser(@RequestBody RegisterDTO user, BindingResult result) throws ResourceAlreadyExistsException, SQLException {
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
	public void login(@RequestBody LoginDTO loginDTO, HttpSession session)
			throws ResourceDoesntExistException, BadCredentialsException {
		User user = this.userService.login(loginDTO);
		session.setAttribute("user", user);

	}
}
