package com.vmzone.demo.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.OrderBasicInfo;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.BadRequestException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.OrderDetails;
import com.vmzone.demo.models.User;
import com.vmzone.demo.service.OrderService;

@RestController
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/order")
	public void createNewOrder(HttpServletRequest request) throws ResourceDoesntExistException, BadRequestException {
		User user = (User) request.getSession().getAttribute("user");
		this.orderService.createNewOrder(user);
	}

	@GetMapping("/orders/{id}")
	public List<OrderBasicInfo> getAllOrdersForUser(@PathVariable long id,HttpSession session) throws BadCredentialsException {
		if (session.getAttribute("user") == null || ((User) session.getAttribute("user")).getUserId()!=id) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"Unauthorized: Access is denied due to invalid credentials.");
		}
		return this.orderService.getAllOrdersForUser(id);
	}

	@GetMapping("/order/{id}")
	public List<OrderDetails> getDetailsForOrderById(@PathVariable long id,HttpSession session) throws BadCredentialsException, ResourceDoesntExistException {
		if (session.getAttribute("user") == null) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You are not logged in! You should log in first!");
		}
		return this.orderService.getOrderDetailsById(id,((User) session.getAttribute("user")).getUserId());
	}
}
