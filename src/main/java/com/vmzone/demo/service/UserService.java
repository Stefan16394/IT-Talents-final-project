package com.vmzone.demo.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.ChangePasswordDTO;
import com.vmzone.demo.dto.EditProfileDTO;
import com.vmzone.demo.dto.LoginDTO;
import com.vmzone.demo.dto.RegisterDTO;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Category;
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
	

	public User editProfile(long id, EditProfileDTO user) throws ResourceDoesntExistException {
		User u = this.userRepository.findById(id);
		if (u == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User doesn't exist");
		}
		
		u.setName(user.getName());
		u.setSurname(user.getSurname());
		u.setEmail(user.getEmail());
		u.setGender(user.getGender());
		u.setIsSubscribed(user.getIsSubscribed());
		u.setPhone(user.getPhone());
		u.setCity(user.getCity());
		u.setPostCode(user.getPostCode());
		u.setAdress(user.getAdress());
		u.setAge(user.getAge());

		this.userRepository.save(u);
		return u;
		
	}
	
	public void changePassword(long id, ChangePasswordDTO pass) throws ResourceDoesntExistException {
		User u = this.userRepository.findById(id);
		if (u == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User doesn't exist");
		}
		String hashedPassword = bCryptPasswordEncoder.encode(pass.getPassword());
		u.setPassword(hashedPassword);
		System.out.println(bCryptPasswordEncoder.matches(pass.getPassword(), u.getPassword()));
	}
}