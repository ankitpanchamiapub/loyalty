package com.loyalty.service.impl;

import com.loyalty.dao.LoyaltyPoints;
import com.loyalty.dao.Transactions;
import com.loyalty.repository.LoyaltyRepository;
import com.loyalty.service.LoyaltyService;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyServiceImpl implements LoyaltyService {

	private final LoyaltyRepository loyaltyRepository;

	public LoyaltyServiceImpl(LoyaltyRepository loyaltyRepository) {
		this.loyaltyRepository = loyaltyRepository;
	}

	@Override
	public LoyaltyPoints getLoyaltyPointsByCustomerId(Long customerId) {
		return loyaltyRepository.findByCustomerId(customerId);
	}

	@Override
	public LoyaltyPoints findOrCreateLoyaltyPoint(Transactions transaction) {
		LoyaltyPoints loyaltyPoint = loyaltyRepository.findByCustomerId(transaction.getCustomerId());
		if(loyaltyPoint == null) {
			loyaltyPoint = new LoyaltyPoints();
			loyaltyPoint.setCustomerId(transaction.getCustomerId());
			loyaltyPoint.setPoints(0.0);
			loyaltyRepository.save(loyaltyPoint);
		}
		return loyaltyPoint;
	}

}
