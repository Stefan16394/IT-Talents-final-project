package com.vmzone.demo.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	private String title;
	private String information;
	private double price;
	private int inStock;
	private int delivery;
	private LocalDateTime date;
	private int quantity;
	private int isDeleted;
	private int inSale;
	private String detailedInformation;
	private Double rating;
	
	public Product(Category category, String title, String information, int inStock, int delivery,
			 int quantity, int inSale, String detailedInformation) {
		super();
		this.category = category;
		this.title = title;
		this.information = information;
		this.inStock = inStock;
		this.delivery = delivery;
		this.date = LocalDateTime.now();
		this.quantity = quantity;
		this.inSale = inSale;
		this.detailedInformation = detailedInformation;
	}
}
