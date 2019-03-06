package com.vmzone.demo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vmzone.demo.dto.EditProfileDTO;
import com.vmzone.demo.dto.EditReviewDTO;
import com.vmzone.demo.dto.LoginDTO;
import com.vmzone.demo.dto.RegisterDTO;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.InvalidEmailException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.User;
import com.vmzone.demo.repository.UserRepository;
import com.vmzone.demo.service.UserService;

@RunWith(MockitoJUnitRunner.Silent.class) 
@SpringBootTest
public class UserServiceTests {
	private static final String PASSWORD = "1234";

	private static final String EMAIL = "example@abv.bg";

	private static final EditProfileDTO EDITED_USER_DTO = new EditProfileDTO("EditName", "EditSurname", EMAIL, "Male", 1, "1234", "Sofia", "12345", "EditAdress", 25);

	private static final User EXPECTED_USER = new User(1L, "Ivan", "Ivanov", EMAIL, PASSWORD, "Male", 0, 0, null, null, null, null, 25, 0);

	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Test(expected = ResourceAlreadyExistsException.class)
	public void testRegisterUserWithAlreadyExistentUser() throws AddressException, ResourceAlreadyExistsException, InvalidEmailException, SQLException, MessagingException, IOException{
		when(userRepository.findByEmail(EMAIL)).thenReturn(new User());
		userService.register(new RegisterDTO(1L, "Ivan", "Ivanov", EMAIL, PASSWORD, "Male", 0, 0));
	}
	
	@Test
	public void testRegisterUserWithNonExistingUser() throws AddressException, ResourceAlreadyExistsException, InvalidEmailException, SQLException, MessagingException, IOException{
		when(userRepository.findByEmail(EMAIL)).thenReturn(null);
		when(bCryptPasswordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
		userService.register(new RegisterDTO(1L, "Ivan", "Ivanov", EMAIL, PASSWORD, "Male", 0, 0));
	}
	
	@Test(expected = ResourceDoesntExistException.class)
	public void testLoginUserWithNonExistentUser() throws BadCredentialsException, ResourceDoesntExistException{
		when(userRepository.findByEmail(EMAIL)).thenReturn(null);
		userService.login(new LoginDTO(EMAIL,PASSWORD));
	}
	
	@Test(expected = BadCredentialsException.class)
	public void testLoginUserWithWrongPasswordInput() throws  BadCredentialsException, ResourceDoesntExistException{
		when(userRepository.findByEmail(EMAIL)).thenReturn(EXPECTED_USER);
		userService.login(new LoginDTO(EMAIL,"wrongPassword"));
	}
	
	@Test
	public void testLoginUserWithCorrectInput() throws ResourceDoesntExistException, BadCredentialsException {
		when(userRepository.findByEmail(EMAIL)).thenReturn(EXPECTED_USER);
		when(bCryptPasswordEncoder.matches(PASSWORD,PASSWORD)).thenReturn(true);
		User u = userService.login(new LoginDTO(EMAIL,PASSWORD));
		assertEquals(u, EXPECTED_USER);
	}
	
	@Test(expected = ResourceDoesntExistException.class)
	public void testEditProfileWithNonExistingUser() throws ResourceDoesntExistException {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		userService.editProfile(1L, EDITED_USER_DTO);
	}
	
	@Test
	public void testEditProfileWithCorrectInput() throws ResourceDoesntExistException {
		when(userRepository.findById(1L)).thenReturn(Optional.of(EXPECTED_USER));
		User user = userService.editProfile(1L, EDITED_USER_DTO);
		
		assertEquals(user.getName(),EDITED_USER_DTO.getName());
		assertEquals(user.getSurname(),EDITED_USER_DTO.getSurname());
		assertEquals(user.getEmail(),EDITED_USER_DTO.getEmail());
		assertEquals(user.getGender(),EDITED_USER_DTO.getGender());
		assertEquals(user.getIsSubscribed(),EDITED_USER_DTO.getIsSubscribed());
		assertEquals(user.getPhone(),EDITED_USER_DTO.getPhone());
		assertEquals(user.getCity(),EDITED_USER_DTO.getCity());
		assertEquals(user.getPostCode(),EDITED_USER_DTO.getPostCode());
		assertEquals(user.getAdress(),EDITED_USER_DTO.getAdress());
		assertEquals(user.getAge(),EDITED_USER_DTO.getAge());

	}
	
	@Configuration
	static class Config {

	}
	
}
