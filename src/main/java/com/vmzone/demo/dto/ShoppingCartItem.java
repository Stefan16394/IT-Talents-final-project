package com.vmzone.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItem {
	private Long product_id;
	private String title;
	private double price;
	private int quantity;

	@Override
	public boolean equals(Object obj) {
		ShoppingCartItem sc = (ShoppingCartItem) obj;
		return this.product_id.equals(sc.getProduct_id()) && this.title.equals(sc.title) && this.price == sc.getPrice()
				&& this.quantity == sc.getQuantity();
	}
}
