package com.example.demo.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class RegisterDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long user_id;

	private String firstName;
  
	private String lastName;
	
    private String email;
    
	private String password;
    
	private String gender;
    
	private int isAdmin;
    
	private int isSubscribed;
}
