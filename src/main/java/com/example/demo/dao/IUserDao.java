package com.example.demo.dao;

import java.sql.SQLException;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.exceptions.ResourceAlreadyExistsException;
import com.example.demo.exceptions.ResourceDoesntExistException;
import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.models.User;

public interface IUserDao {
	void register(RegisterDTO user) throws SQLException, ResourceAlreadyExistsException;

	User login(LoginDTO loginDTO) throws BadCredentialsException, ResourceDoesntExistException;

	User findByEmail(String email);
}
