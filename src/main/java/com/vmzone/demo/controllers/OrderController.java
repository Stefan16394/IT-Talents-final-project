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
import com.vmzone.demo.utils.SessionManager;

@RestController
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/order")
	public long createNewOrder(HttpSession session) throws ResourceDoesntExistException, BadRequestException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		return this.orderService.createNewOrder(SessionManager.getLoggedUser(session));
	}

	@GetMapping("/orders")
	public List<OrderBasicInfo> getAllOrdersForUser(HttpSession session) throws BadCredentialsException, ResourceDoesntExistException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"Unauthorized: Access is denied due to invalid credentials.");
		}
		return this.orderService.getAllOrdersForUser(SessionManager.getLoggedUserId(session));
	}

	@GetMapping("/order/{id}")
	public List<OrderDetails> getDetailsForOrderById(@PathVariable long id,HttpSession session) throws BadCredentialsException, ResourceDoesntExistException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You are not logged in! You should log in first!");
		}
		return this.orderService.getOrderDetailsById(id,SessionManager.getLoggedUserId(session));
	}
}
