package com.vmzone.demo.models;

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
@Table(name = "photos")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Photo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long photoId;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	private String photoPath;
	
	public Photo(Product product, String photoPath) {
		super();
		this.product = product;
		this.photoPath = photoPath;
		
	}
	
	

}
