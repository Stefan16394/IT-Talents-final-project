package com.vmzone.demo.utils;

import javax.servlet.http.HttpSession;

import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.User;


public class SessionManager {
	
	private static final String USER = "user";

	public static boolean isUserLoggedIn(HttpSession session) {
		if (session.getAttribute(USER) == null) {
			return false;
		}
		return true;
	}
	
	public static boolean isAdmin(HttpSession session) {
		if(!((User) session.getAttribute(USER)).isAdmin()) {
			return false;
		}
		return true;
	}
	
	public static long getLoggedUserId(HttpSession session) throws ResourceDoesntExistException {
		if(!isUserLoggedIn(session)){
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		return ((User) session.getAttribute(USER)).getUserId();
	}
	
	public static User getLoggedUser(HttpSession session) throws ResourceDoesntExistException {
		if(!isUserLoggedIn(session)){
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		return ((User) session.getAttribute(USER));
	}
	

}
