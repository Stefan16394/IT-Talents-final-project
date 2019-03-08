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
public class OrderBasicInfo {
	private Long orderId;
	private LocalDateTime dateOfOrder;
}
