package com.vmzone.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.repository.UserRepository;


@RestController
public class AdminController {
	
	@Autowired
    private UserRepository userRepository;
	
	
	
}
