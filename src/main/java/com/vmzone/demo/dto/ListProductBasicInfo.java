package com.vmzone.demo.dto;

import java.time.LocalDateTime;

import com.vmzone.demo.models.Photo;

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
public class ListProductBasicInfo {
	private long id;
	private String title;
	private double price;
	private LocalDateTime date;
	private Photo photo;
	
	public ListProductBasicInfo(long id, String title){
		this.id = id;
		this.title = title;
	}
	
	public ListProductBasicInfo(String title, double price){
		this.title = title;
		this.price = price;
	}
	
}
