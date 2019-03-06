package com.vmzone.demo.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileDTO {
		
		@NotNull
		private String name;
		@NotNull
		private String surname;
		@NotNull
	    private String email;
		private String gender;
	    
		private int isSubscribed;
		@NotNull
	    private String phone; 
		@NotNull
	    private String city;
	    private String postCode;
	    @NotNull
	    private String adress;
	    @Positive
	    private int age;
	

}
