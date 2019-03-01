package com.vmzone.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	
	private String name;
  
	private String surname;
	
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
