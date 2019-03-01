package com.vmzone.demo.dao;

import java.sql.SQLException;

import com.vmzone.demo.dto.LoginDTO;
import com.vmzone.demo.dto.RegisterDTO;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.User;

public interface IUserDao {
	void register(RegisterDTO user) throws SQLException, ResourceAlreadyExistsException;

	User login(LoginDTO loginDTO) throws BadCredentialsException, ResourceDoesntExistException;

	User findByEmail(String email);
}
