package com.vmzone.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomePageDTO {

	private List<ListCategory> categories;
	private List<ListProduct> products;
}
