package com.loyalty.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestRecordTransaction {
	private Long id;
	private Long customerId;
	private String orderId;
	private Double orderAmount;
	private Double points;
}
