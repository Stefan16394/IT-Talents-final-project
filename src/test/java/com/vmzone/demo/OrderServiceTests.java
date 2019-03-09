package com.vmzone.demo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import com.vmzone.demo.dto.OrderBasicInfo;
import com.vmzone.demo.dto.ShoppingCartItem;
import com.vmzone.demo.exceptions.BadRequestException;
import com.vmzone.demo.exceptions.NotEnoughQuantityException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.models.Order;
import com.vmzone.demo.models.OrderDetails;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.User;
import com.vmzone.demo.repository.OrderDetailsRepository;
import com.vmzone.demo.repository.OrderRepository;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.service.OrderService;
import com.vmzone.demo.service.UserService;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class OrderServiceTests {
	private static final long DEFAULT_ID_TO_SEARCH = 1L;

	private static final User TEST_USER = new User(1L, "User", "User", "user@abv.bg", "1234", "male", 0,  null, null,
			null, null, 25, 0);
	
	private static final Product TEST_PRODUCT = new Product(new Category("Shoes",null), "Product", "Information", 1, 24, 15, 1, "Details");


	@Mock
	private ProductRepository productRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderDetailsRepository orderDetailsRepository;

	@Mock
	private UserService userService;

	@InjectMocks
	private OrderService orderService;

	@Test
	public void testGetAllOrdersForUser() {
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(TEST_USER));
		orders.add(new Order(TEST_USER));
		orders.add(new Order(TEST_USER));

		when(orderRepository.findOrdersByUserId(DEFAULT_ID_TO_SEARCH)).thenReturn(orders);
		List<OrderBasicInfo> result = orderService.getAllOrdersForUser(DEFAULT_ID_TO_SEARCH);
		assertEquals(orders.size(), result.size());
	}

	@Test(expected = ResourceDoesntExistException.class)
	public void testGetOrderDetailsByIdWhenOrderDoesntExist() throws ResourceDoesntExistException {
		when(orderRepository.findOrderByUserId(DEFAULT_ID_TO_SEARCH, DEFAULT_ID_TO_SEARCH)).thenReturn(null);
		orderService.getOrderDetailsById(DEFAULT_ID_TO_SEARCH, DEFAULT_ID_TO_SEARCH);
	}

	@Test
	public void testGetOrderDetailsByIdWhenOrderExist() throws ResourceDoesntExistException {
		List<OrderDetails> orderDetails = new ArrayList<>();
		orderDetails.add(new OrderDetails());
		orderDetails.add(new OrderDetails());
		orderDetails.add(new OrderDetails());

		Order order = new Order(TEST_USER);
		order.setOrderId(DEFAULT_ID_TO_SEARCH);
		when(orderRepository.findOrderByUserId(DEFAULT_ID_TO_SEARCH, DEFAULT_ID_TO_SEARCH)).thenReturn(order);

		when(orderDetailsRepository.getOrderDetailsForOrder(DEFAULT_ID_TO_SEARCH)).thenReturn(orderDetails);
		List<OrderDetails> result = orderService.getOrderDetailsById(DEFAULT_ID_TO_SEARCH, DEFAULT_ID_TO_SEARCH);
		assertEquals(orderDetails.size(), result.size());
	}
	
	@Test(expected = ResourceDoesntExistException.class)
	public void testCreateNewOrderWhenShoppingCartIsEmpty() throws ResourceDoesntExistException, BadRequestException, NotEnoughQuantityException {
		when(userService.getShoppingCart(DEFAULT_ID_TO_SEARCH)).thenReturn(new ArrayList<ShoppingCartItem>());
		orderService.createNewOrder(TEST_USER);
	}
	
	@Test(expected = BadRequestException.class)
	public void testCreateNewOrderWhenTransactionFails() throws ResourceDoesntExistException, BadRequestException, NotEnoughQuantityException {
		List<ShoppingCartItem> items = new ArrayList<>();
		items.add(new ShoppingCartItem(DEFAULT_ID_TO_SEARCH, "Product", 10, 15));
		
		when(userService.getShoppingCart(DEFAULT_ID_TO_SEARCH)).thenReturn(items);
		orderService.createNewOrder(TEST_USER);
	}
	
	@Test
	public void testCreateNewOrderWithValidData() throws ResourceDoesntExistException, BadRequestException, NotEnoughQuantityException {
		List<ShoppingCartItem> items = new ArrayList<>();
		items.add(new ShoppingCartItem(DEFAULT_ID_TO_SEARCH, "Product", 10, 15));
		when(productRepository.findById(items.get(0).getProduct_id())).thenReturn(Optional.of(TEST_PRODUCT));
		when(userService.getShoppingCart(DEFAULT_ID_TO_SEARCH)).thenReturn(items);
		orderService.createNewOrder(TEST_USER);
	}
	
	@Test(expected = NotEnoughQuantityException.class)
	public void testCreateNewOrderWhenThereIsNotEnoughQuantityOfProduct() throws ResourceDoesntExistException, BadRequestException, NotEnoughQuantityException {
		List<ShoppingCartItem> items = new ArrayList<>();
		items.add(new ShoppingCartItem(DEFAULT_ID_TO_SEARCH, "Product", 10, 20));
		when(productRepository.findById(items.get(0).getProduct_id())).thenReturn(Optional.of(TEST_PRODUCT));
		when(userService.getShoppingCart(DEFAULT_ID_TO_SEARCH)).thenReturn(items);
		orderService.createNewOrder(TEST_USER);
	}

	@Configuration
	static class Config {

	}
}
