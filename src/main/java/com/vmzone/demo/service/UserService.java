package com.vmzone.demo.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.LoginDTO;
import com.vmzone.demo.dto.RegisterDTO;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.User;
import com.vmzone.demo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	public void register(RegisterDTO user) throws SQLException, ResourceAlreadyExistsException {
		User u = this.userRepository.findByEmail(user.getEmail());
		if (u != null) {
			throw new ResourceAlreadyExistsException(HttpStatus.CONFLICT,
					"There is already an account with this email address " + u.getEmail());
		}
		String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		
		User newUser = new User(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmail(), hashedPassword,
				user.getGender(), user.getIsAdmin(), user.getIsSubscribed(), null, null, null, null, 0, 0);
		this.userRepository.save(newUser);
	}

	public User login(LoginDTO loginDTO) throws ResourceDoesntExistException, BadCredentialsException {
		User user = this.userRepository.findByEmail(loginDTO.getEmail());
		if (user == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User doesn't exist");
		}
		boolean passwordsMatch = bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword());
		if (!passwordsMatch) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "Incorrect email or password");
		}
		return user;
	}
}
