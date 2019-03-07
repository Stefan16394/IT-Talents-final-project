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
@Table(name = "order_details")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long orderDetailsId;
	private int quantity;
	private Long orderId;
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	public OrderDetails(int quantity, Long id, Product product) {
		super();
		this.quantity = quantity;
		this.orderId = id;
		this.product = product;
	}
}
