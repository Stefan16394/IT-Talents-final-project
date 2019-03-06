package com.vmzone.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListProductBasicInfo {
	private long id;
	private String title;
	private double price;
	private LocalDateTime date;
	
	public ListProductBasicInfo(long id, String title){
		this.id = id;
		this.title = title;
	}
	
	// TO DO ADD MORE FIELDS HERE!!!

}
