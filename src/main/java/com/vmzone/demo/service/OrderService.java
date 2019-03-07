package com.vmzone.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.ShoppingCartItem;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.exceptions.VMZoneException;
import com.vmzone.demo.models.Order;
import com.vmzone.demo.models.OrderDetails;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.User;
import com.vmzone.demo.repository.OrderDetailsRepository;
import com.vmzone.demo.repository.OrderRepository;
import com.vmzone.demo.repository.ProductRepository;

@Service
public class OrderService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@Autowired
	private UserService userService;

	public void createNewOrder(User user) throws ResourceDoesntExistException {
		List<ShoppingCartItem> items = this.userService.getShoppingCart(user.getUserId());
		if (items.isEmpty()) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "You shopping cart is empty.");
		}
		Order order = new Order(user);
		this.orderRepository.save(order);
		createOrderDetails(user,items, order.getOrderId());
	}

	public void createOrderDetails(User user,List<ShoppingCartItem> items, Long id) {
		for (ShoppingCartItem item : items) {
			Product p = productRepository.findById(item.getProduct_id()).get();
			OrderDetails orderDetail = new OrderDetails(item.getQuantity(), id, p);
			this.orderDetailsRepository.save(orderDetail);
		}
	}
}
