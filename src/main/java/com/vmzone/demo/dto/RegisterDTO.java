package com.vmzone.demo.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@Email
    private String email;
    @NotNull
	private String password;
    
	private String gender;
	@NotNull
	private int isAdmin;
	@NotNull
	private int isSubscribed;
}
