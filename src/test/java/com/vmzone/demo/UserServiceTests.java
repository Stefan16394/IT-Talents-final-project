package com.vmzone.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vmzone.demo.dto.ChangePasswordDTO;
import com.vmzone.demo.dto.EditProfileDTO;
import com.vmzone.demo.dto.LoginDTO;
import com.vmzone.demo.dto.RegisterDTO;
import com.vmzone.demo.dto.ShoppingCartItem;
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

	private static final EditProfileDTO EDITED_USER_DTO = new EditProfileDTO("EditName", "EditSurname", EMAIL, "Male",
			1, "1234", "Sofia", "12345", "EditAdress", 25);

	private static User EXPECTED_USER;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Mock
	private PasswordEncoder bCryptPasswordEncoder;

	@Before
	public void init() {
		EXPECTED_USER = new User(1L, "Ivan", "Ivanov", EMAIL, PASSWORD, "Male", 0, 0, null, null, null, null, 25, 0);
	}

	@Test(expected = ResourceAlreadyExistsException.class)
	public void testRegisterUserWithAlreadyExistentUser() throws AddressException, ResourceAlreadyExistsException,
			InvalidEmailException, SQLException, MessagingException, IOException {
		when(userRepository.findByEmail(EMAIL)).thenReturn(new User());
		userService.register(new RegisterDTO(1L, "Ivan", "Ivanov", EMAIL, PASSWORD, "Male", 0, 0));
	}

	@Test
	public void testRegisterUserWithNonExistingUser() throws AddressException, ResourceAlreadyExistsException,
			InvalidEmailException, SQLException, MessagingException, IOException {
		when(userRepository.findByEmail(EMAIL)).thenReturn(null);
		when(bCryptPasswordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
		userService.register(new RegisterDTO(1L, "Ivan", "Ivanov", EMAIL, PASSWORD, "Male", 0, 0));
	}

	@Test(expected = ResourceDoesntExistException.class)
	public void testLoginUserWithNonExistentUser() throws BadCredentialsException, ResourceDoesntExistException {
		when(userRepository.findByEmail(EMAIL)).thenReturn(null);
		userService.login(new LoginDTO(EMAIL, PASSWORD));
	}

	@Test(expected = BadCredentialsException.class)
	public void testLoginUserWithWrongPasswordInput() throws BadCredentialsException, ResourceDoesntExistException {
		when(userRepository.findByEmail(EMAIL)).thenReturn(EXPECTED_USER);
		userService.login(new LoginDTO(EMAIL, "wrongPassword"));
	}

	@Test
	public void testLoginUserWithCorrectInput() throws ResourceDoesntExistException, BadCredentialsException {
		when(userRepository.findByEmail(EMAIL)).thenReturn(EXPECTED_USER);
		when(bCryptPasswordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);
		User u = userService.login(new LoginDTO(EMAIL, PASSWORD));
		assertEquals(u, EXPECTED_USER);
	}

//	@Test(expected = ResourceDoesntExistException.class)
//	public void testEditProfileWithNonExistingUser() throws ResourceDoesntExistException {
//		when(userRepository.findById(1L)).thenReturn(Optional.empty());
//		userService.editProfile(1L, EDITED_USER_DTO);
//	}

//	@Test
//	public void testEditProfileWithCorrectInput() throws ResourceDoesntExistException {
//		when(userRepository.findById(1L)).thenReturn(Optional.of(EXPECTED_USER));
//		User user = userService.editProfile(1L, EDITED_USER_DTO);
//
//		assertEquals(user.getName(), EDITED_USER_DTO.getName());
//		assertEquals(user.getSurname(), EDITED_USER_DTO.getSurname());
//		assertEquals(user.getEmail(), EDITED_USER_DTO.getEmail());
//		assertEquals(user.getGender(), EDITED_USER_DTO.getGender());
//		assertEquals(user.getIsSubscribed(), EDITED_USER_DTO.getIsSubscribed());
//		assertEquals(user.getPhone(), EDITED_USER_DTO.getPhone());
//		assertEquals(user.getCity(), EDITED_USER_DTO.getCity());
//		assertEquals(user.getPostCode(), EDITED_USER_DTO.getPostCode());
//		assertEquals(user.getAdress(), EDITED_USER_DTO.getAdress());
//		assertEquals(user.getAge(), EDITED_USER_DTO.getAge());
//
//	}

	@Test(expected = ResourceDoesntExistException.class)
	public void testChangePasswordWithInvalidUserId() throws ResourceDoesntExistException {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		userService.changePassword(1L, new ChangePasswordDTO("12345"));
	}

	@Test
	public void testChangePasswordWithCorrectData() throws ResourceDoesntExistException {
		final String newPassword = "12345";
		when(userRepository.findById(1L)).thenReturn(Optional.of(EXPECTED_USER));
		when(bCryptPasswordEncoder.encode(newPassword)).thenReturn(newPassword);
		userService.changePassword(1L, new ChangePasswordDTO(newPassword));
		assertTrue(EXPECTED_USER.getPassword().equals(newPassword));
	}

	@Test(expected = ResourceDoesntExistException.class)
	public void testForgottenPasswordWhenUserDoesntExist() throws AddressException, ResourceDoesntExistException,
			InvalidEmailException, MessagingException, IOException {
		when(userRepository.findByEmail(EMAIL)).thenReturn(null);
		userService.forgottenPassword(EMAIL);
	}

	@Test(expected = ResourceDoesntExistException.class)
	public void testRemoveUserByIdWhenUserDoesntExist() throws ResourceDoesntExistException {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		userService.removeUserById(1L);
	}

	@Test
	public void testRemoveUserByIdWithCorrectInput() throws ResourceDoesntExistException {
		when(userRepository.findById(1L)).thenReturn(Optional.of(EXPECTED_USER));
		userService.removeUserById(1L);
		assertTrue(EXPECTED_USER.getIsDeleted() == 1);
	}

	@Test
	public void testGetShoppingCart() {
		List<ShoppingCartItem> expectedResult= new ArrayList<>();
		expectedResult.add(new ShoppingCartItem(1L, "Product 1", 2.0, 5));
		expectedResult.add(new ShoppingCartItem(2L, "Product 2", 3.0, 6));

		List<Object> objects = new ArrayList<>();
		objects.add(new Object[] { new Long(1), new String("Product 1"), new Double(2.0), new Integer(5) });
		objects.add(new Object[] { new Long(2), new String("Product 2"), new Double(3.0), new Integer(6) });
		when(userRepository.getShoppingCart(1L)).thenReturn(objects);

		boolean areEqual = true;
		List<ShoppingCartItem> items=userService.getShoppingCart(1L);
		for(int i =0;i<expectedResult.size();i++) {
			if(!expectedResult.get(i).equals(items.get(i))) {
				areEqual = false;
				break;
			}
		}
		
		assertTrue(areEqual);
	}
	
	@Configuration
	static class Config {

	}

}
