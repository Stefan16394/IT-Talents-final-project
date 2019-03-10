package com.vmzone.demo.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
		
		@NotBlank
		private String name;
		@NotBlank
		private String surname;
		@NotNull
		@Email
	    private String email;
		@NotBlank
		@Pattern(regexp = "^Male|Female$",message = "Gender should be Male or Female")
		private String gender;
	    @Min(0)
	    @Max(1)
		private int isSubscribed;
		@NotBlank
	    private String phone; 
		@NotBlank
	    private String city;
		@NotBlank
	    private String postCode;
	    @NotBlank
	    private String adress;
	    @Positive
	    @Max(100)
	    private int age;
	

}
