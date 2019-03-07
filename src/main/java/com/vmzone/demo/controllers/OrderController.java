package com.vmzone.demo.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.exceptions.BadRequestException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.exceptions.VMZoneException;
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
}
