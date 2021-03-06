package com.vmzone.demo.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.CartProductDTO;
import com.vmzone.demo.dto.ChangePasswordDTO;
import com.vmzone.demo.dto.ContactUsDTO;
import com.vmzone.demo.dto.EditProfileDTO;
import com.vmzone.demo.dto.LoginDTO;
import com.vmzone.demo.dto.RegisterDTO;
import com.vmzone.demo.dto.ShoppingCartItem;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.InvalidEmailException;
import com.vmzone.demo.exceptions.NotEnoughQuantityException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.User;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.repository.UserRepository;
import com.vmzone.demo.utils.EmailSender;
import com.vmzone.demo.utils.PasswordGenerator;
import com.vmzone.demo.utils.RegexValidator;
/**
 * Service layer communicating with user repository and product repository for managing user requests
 * 
 * @author Stefan Rangelov and Sabiha Djurina
 *
 */

@Service
public class UserService {
	private static final int LENGTH_FOR_FORGOTTEN_PASSWORD = 8;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	public User register(RegisterDTO user) throws SQLException, ResourceAlreadyExistsException, AddressException,
			InvalidEmailException, MessagingException, IOException {
		User u = this.userRepository.findByEmail(user.getEmail());
		if (u != null) {
			throw new ResourceAlreadyExistsException(HttpStatus.CONFLICT,
					"There is already an account with this email address " + u.getEmail());
		}
		String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());

		User newUser = new User(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmail(),
				hashedPassword, user.getGender(), user.getIsSubscribed(), null, null, null, null, 0, 0);
		EmailSender.registration(user.getEmail());

		return this.userRepository.save(newUser);
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
	
	/**
	 * Edit profile of user
	 * 
	 * @param id - id of user object stored in db
	 * @param user - EditProfileDTO user information for editing profile
	 * @return User - the user with the edited profile
	 * @throws ResourceDoesntExistException - when the user has been deleted or does not exist in DB
	 * @throws ResourceAlreadyExistsException - when the edited email already exist in DB and its to another user
	 */
	
	public User editProfile(long id, EditProfileDTO user)
			throws ResourceDoesntExistException, ResourceAlreadyExistsException {
		User u = null;
		try {
			u = this.userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User doesn't exist");
		}
		if(u.getIsDeleted() == 1) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User has been deleted");
		}
		User check = this.userRepository.findByEmail(user.getEmail());

		if (check != null && !u.equals(check)) {
			throw new ResourceAlreadyExistsException(HttpStatus.CONFLICT, "There is already a user with this email!");
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
		User u = null;
		try {
			u = this.userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User doesn't exist");
		}
		if(u.getIsDeleted() == 1) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User has been deleted");
		}

		String hashedPassword = bCryptPasswordEncoder.encode(pass.getPassword());
		u.setPassword(hashedPassword);
		this.userRepository.save(u);
	}

	/**
	 * Send forgotten password to email
	 * 
	 * @param email - email to send the forgotten password to
	 * @throws ResourceDoesntExistException - when the user with this email does not exist in DB
	 * @throws AddressException
	 * @throws InvalidEmailException
	 * @throws MessagingException
	 * @throws IOException
	 */

	public void forgottenPassword(String email) throws ResourceDoesntExistException, AddressException,
			InvalidEmailException, MessagingException, IOException {
		User u = this.userRepository.findByEmail(email);

		if (u == null || u.getIsDeleted() == 1) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User doesn't exist");
		}
		
		String newPass = PasswordGenerator.makePassword(LENGTH_FOR_FORGOTTEN_PASSWORD);
		String hashedPassword = bCryptPasswordEncoder.encode(newPass);
		EmailSender.forgottenPassword(u.getEmail(), newPass);
		u.setPassword(hashedPassword);
		this.userRepository.save(u);
	}
	
	@Scheduled(fixedRate = 7*24*60*60000)
	public void sendSubscribed() throws AddressException, InvalidEmailException, MessagingException, IOException {
		List<String> emails = this.userRepository.findAll().stream().filter(user -> user.getIsSubscribed() == 1)
				.map(user -> user.getEmail()).collect(Collectors.toList());

		EmailSender.sendSubscripedPromotions(emails);

	}

	public void removeUserById(long id) throws ResourceDoesntExistException {
		User u = null;
		try {
			u = this.userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User doesn't exist");
		}
		if(u.getIsDeleted() == 1) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User has been deleted");
		}
		u.setIsDeleted(1);
		this.userRepository.save(u);

	}

	public void contactUs(ContactUsDTO contact)
			throws InvalidEmailException, AddressException, MessagingException, IOException {
		if (!RegexValidator.validateEmail(contact.getEmail())) {
			throw new InvalidEmailException(HttpStatus.UNAUTHORIZED, "Incorrect email or password");
		}

		EmailSender.contactUs(contact.toString());
	}

	public List<ShoppingCartItem> getShoppingCart(long id) {
		List<ShoppingCartItem> items = new ArrayList<>();
		List<Object> objects = this.userRepository.getShoppingCart(id);
		for (Object o : objects) {
			Object[] row = (Object[]) o;
			items.add(new ShoppingCartItem(Long.parseLong(row[0].toString()), row[1].toString(),
					Double.parseDouble(row[2].toString()), Integer.parseInt(row[3].toString())));
		}
		return items;
	}

	/**
	 * Add product in users cart
	 * 
	 * @param addProduct - dto with information for the product
	 * @param id - id of product object stored in db
	 * @return id -  id of product added to cart
	 * @throws NotEnoughQuantityException - when there is not enough quantity of the added product
	 * @throws ResourceDoesntExistException - when the product has been deleted or does not exist in db
	 */
	
	public long addProductToCart(CartProductDTO addProduct, long id) throws NotEnoughQuantityException, ResourceDoesntExistException {
		final Product p;
		try {
		    p = this.productRepository.findById(addProduct.getProductId()).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist");
		}
		if (p.getQuantity() < addProduct.getQuantity()) {
			throw new NotEnoughQuantityException(HttpStatus.BAD_REQUEST,
					"There is not enough quantity of this product! Try with less or add it to you cart later.");
		}
		if(p.getIsDeleted() == 1) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product has been deleted");
		}
		this.userRepository.addProductToCart(addProduct.getProductId(), addProduct.getQuantity(), id);
		return addProduct.getProductId();
	}
	
	/**
	 * Edit product in users cart
	 * 
	 * @param editProduct - dto with info for editing product in the cart
	 * @param id - id of product object stored in db
	 * @return id - id of product object updated in cart
	 * @throws NotEnoughQuantityException - when there is not enough quantity if the added product
	 * @throws ResourceDoesntExistException - when the product has been deleted or does not exist in db
	 */

	public long updateProductInCart(CartProductDTO editProduct, long id) throws NotEnoughQuantityException, ResourceDoesntExistException {
		final Product p;
		try {
		    p = this.productRepository.findById(editProduct.getProductId()).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist");
		}
		List<ShoppingCartItem> items = this.getShoppingCart(id);
		boolean isInShoppingCart =items.stream().filter(i->i.getProduct_id().equals(p.getProductId())).findAny().isPresent();

		if(!isInShoppingCart) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist in your cart.");
		}
		if (p.getQuantity() < editProduct.getQuantity()) {
			throw new NotEnoughQuantityException(HttpStatus.BAD_REQUEST,
					"There is not enough quantity of this product! Try with less or add it to you cart later.");
		}
		if(p.getIsDeleted() == 1) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product has been deleted");
		}
		this.userRepository.updateProductInCart(editProduct.getProductId(), editProduct.getQuantity(), id);
		return editProduct.getProductId();
	}

	public void deleteProductInCart(long productId, long userId) {
		this.userRepository.deleteProductInCart(productId, userId);
	}

}
