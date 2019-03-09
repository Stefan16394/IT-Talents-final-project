package com.vmzone.demo.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vmzone.demo.dto.ListProductBasicInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
@NoArgsConstructor

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
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
	public User(Long userId, String name, String surname, String email, String password, String gender,
			int isSubscribed, String phone, String city, String postCode, String adress, int age, int isDeleted) {
		super();
		this.userId = userId;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.isSubscribed = isSubscribed;
		this.phone = phone;
		this.city = city;
		this.postCode = postCode;
		this.adress = adress;
		this.age = age;
		this.isDeleted = isDeleted;
	}
	
	public boolean isAdmin() {
		return this.getIsAdmin() == 1;
	}

}
