package com.loyalty.dto;

import lombok.Data;

@Data
public class CustomerLoyaltyPointDto {
	private Long customerId;
	private Double loyaltyPoints;
}
