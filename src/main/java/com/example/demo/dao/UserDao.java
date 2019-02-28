package com.example.demo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.example.demo.controllers.UserValidator;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.exceptions.ResourceAlreadyExistsException;
import com.example.demo.exceptions.ResourceDoesntExistException;
import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.models.User;

@Component
public class UserDao implements IUserDao {
	private static final String FIND_USER_BY_EMAIL = "SELECT * FROM users u WHERE u.email = :email";
	private static final String CREATE_NEW_USER = "INSERT INTO users (user_id,name,surname,email,password,gender,isAdmin,isSubscribed) VALUES(:user_id,:name,:surname,:email,:password,:gender,:isAdmin,:isSubscribed)";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Override
	public void register(RegisterDTO user) throws SQLException, ResourceAlreadyExistsException {
		User u = this.findByEmail(user.getEmail());
		if (u != null) {
			throw new ResourceAlreadyExistsException(HttpStatus.CONFLICT,
					"There is already an account with this email address " + u.getEmail());
		}
		String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());

		SqlParameterSource parameters = new MapSqlParameterSource().addValue("user_id", user.getUser_id())
				.addValue("name", user.getFirstName()).addValue("surname", user.getLastName())
				.addValue("email", user.getEmail()).addValue("password", hashedPassword)
				.addValue("gender", user.getGender()).addValue("isAdmin", user.getIsAdmin())
				.addValue("isSubscribed", user.getIsSubscribed());

		this.namedParameterJdbcTemplate.update(CREATE_NEW_USER, parameters);

	}

	@Override
	public User findByEmail(String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		User user = null;
		try {
			user = this.namedParameterJdbcTemplate.queryForObject(FIND_USER_BY_EMAIL, params, new RowMapper<User>() {

				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
							rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getString(9), rs.getString(10),
							rs.getString(11), rs.getString(12), rs.getInt(13), rs.getInt(14));
				}
			});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User login(LoginDTO loginDTO) throws ResourceDoesntExistException, BadCredentialsException {
		User user = this.findByEmail(loginDTO.getEmail());
		if (user == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "User doesn't exist");
		}
		boolean passwordsMatch = bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword());
		if (!passwordsMatch) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "Incorrect email or password");
		}
		return user;
	}

}
