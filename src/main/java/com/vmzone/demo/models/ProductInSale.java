package com.vmzone.demo.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "in_sale")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductInSale {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long saleId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product products;

	
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private int discountPercentage;
	
	public ProductInSale(Product product, LocalDateTime startDate, LocalDateTime endDate, int discountPercentage) {
		super();
		this.products = product;
		this.startDate = startDate;
		this.endDate = endDate;
		this.discountPercentage = discountPercentage;
	}
	
	

}
