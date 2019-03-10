package com.vmzone.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.OrderBasicInfo;
import com.vmzone.demo.dto.ShoppingCartItem;
import com.vmzone.demo.exceptions.BadRequestException;
import com.vmzone.demo.exceptions.NotEnoughQuantityException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Order;
import com.vmzone.demo.models.OrderDetails;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.User;
import com.vmzone.demo.repository.OrderDetailsRepository;
import com.vmzone.demo.repository.OrderRepository;
import com.vmzone.demo.repository.ProductRepository;

/**
 * Service layer communicating with product repository, order repository and order details repository for managing orders requests
 * 
 * @author Stefan Rangelov and Sabiha Djurina
 *
 */

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

	/**
	 * make order for user with products in his shopping cart
	 * 
	 * @param user - make order for this user with his products in shopping cart
	 * @return Order - the newly added order
	 * @throws ResourceDoesntExistException -  when the users shopping cart is empty
	 * @throws BadRequestException - when the transaction failed
	 * @throws NotEnoughQuantityException - when there is not enough quantity of some product in shopping cart
	 */
	
	@Transactional(rollbackOn = Exception.class)
	public Order createNewOrder(User user) throws ResourceDoesntExistException, BadRequestException, NotEnoughQuantityException {
		List<ShoppingCartItem> items = this.userService.getShoppingCart(user.getUserId());
		if (items.isEmpty()) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "You shopping cart is empty.");
		}
		try {
			Order order = new Order(user);
			this.orderRepository.save(order);
			for (ShoppingCartItem item : items) {
				Product p = productRepository.findById(item.getProduct_id()).get();
				if(p.getQuantity()<item.getQuantity()) {
					throw new NotEnoughQuantityException(HttpStatus.BAD_REQUEST,"There is not enough quantity of product with id "+p.getProductId());
				}
				p.setQuantity(p.getQuantity()-item.getQuantity());
				this.productRepository.save(p);
				OrderDetails orderDetail = new OrderDetails(item.getQuantity(), order.getOrderId(), p);
				this.orderDetailsRepository.save(orderDetail);
			}
			this.orderRepository.clearShoppingCart(user.getUserId());
			return order;
		}catch(NotEnoughQuantityException e) { 
			throw e;
		}
		catch (Exception e) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Transaction failed.");
		}
	}

	public List<OrderBasicInfo> getAllOrdersForUser(long id) {
		List<OrderBasicInfo> orders = this.orderRepository.findOrdersByUserId(id).stream()
				.filter(order -> order.getIsDeleted() == 0)
				.map(o -> new OrderBasicInfo(o.getOrderId(), o.getDateOfOrder()))
				.collect(Collectors.toList());

		return orders;
	}

	public List<OrderDetails> getOrderDetailsById(long id, Long userId) throws ResourceDoesntExistException {
		Order order = this.orderRepository.findOrderByUserId(id, userId);
		if(order == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Review doesn't exist or it is not your review");
		}
		return this.orderDetailsRepository.getOrderDetailsForOrder(order.getOrderId());
	}
}
