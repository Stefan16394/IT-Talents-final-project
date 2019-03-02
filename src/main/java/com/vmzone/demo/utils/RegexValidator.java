package com.vmzone.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexValidator {
	
	private static final String EMAIL_REGEX = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; 
	// Regex to restrict no. of characters in top level domain plus other
	private static final String PASSWORD_REGEX = "^((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{6,16})"; 
//	(?=.*[a-z])     : This matches the presence of at least one lowercase letter.
//	(?=.*d)         : This matches the presence of at least one digit i.e. 0-9.
//	(?=.*[@#$%])    : This matches the presence of at least one special character.
//	((?=.*[A-Z])    : This matches the presence of at least one capital letter.
//	{6,16}          : This limits the length of password from minimum 6 letters to maximum 16 letters.
	
	public static boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}

	public static boolean validatePassword(String password) {
		Pattern pattern = Pattern.compile(PASSWORD_REGEX);
		Matcher matcher = pattern.matcher(password);
		
		return matcher.matches();
	}

}
