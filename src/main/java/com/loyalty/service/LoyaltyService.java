package com.loyalty.service;

import org.springframework.stereotype.Service;

import com.loyalty.dao.LoyaltyPoints;
import com.loyalty.dao.Transactions;

@Service
public interface LoyaltyService {
	public LoyaltyPoints getLoyaltyPointsByCustomerId(Long customerId);
	public LoyaltyPoints findOrCreateLoyaltyPoint(Transactions transaction);
}
