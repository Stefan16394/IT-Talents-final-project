package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class User {

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
    
    private String phone; 
    private String city;
    private String postCode;
    private String adress;
    private int age;
    private int isDeleted;
}
