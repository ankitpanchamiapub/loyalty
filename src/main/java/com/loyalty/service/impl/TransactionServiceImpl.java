package com.loyalty.service.impl;

import com.loyalty.dao.LoyaltyPoints;
import com.loyalty.dao.Transactions;
import com.loyalty.dto.ValidatorException;
import com.loyalty.repository.TransactionRepository;
import com.loyalty.service.LoyaltyService;
import com.loyalty.service.TransactionService;
import com.loyalty.util.LoyaltyConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final LoyaltyService loyaltyService;
	private final LoyaltyConfiguration loyaltyConfiguration;

	public TransactionServiceImpl(TransactionRepository transactionRepository, LoyaltyService loyaltyService, LoyaltyConfiguration loyaltyConfiguration) {
		this.transactionRepository = transactionRepository;
		this.loyaltyService = loyaltyService;
		this.loyaltyConfiguration = loyaltyConfiguration;
	}

	@Override
	@Transactional
	public Transactions storeTransaction(Transactions transaction) throws Exception {
		try {
			if(transaction.getCustomerId() == null) {
				throw new ValidatorException("Customer ID cannot be null");
			}
			if(transaction.getOrderAmount() == null || transaction.getOrderAmount() <= 0) {
				throw new ValidatorException("Invalid purchase amount");
			}
			Double points = calculateLoyaltyPoint(transaction);
			transaction.setPoints(points);
			transactionRepository.save(transaction);
			LoyaltyPoints loyaltyPoints = loyaltyService.findOrCreateLoyaltyPoint(transaction);
			loyaltyPoints.setPoints(loyaltyPoints.getPoints() + points);
			return transaction;
		}catch(Exception ex) {
			throw ex;
		}
	}


	private Double calculateLoyaltyPoint(Transactions transaction) {
		Double points = 0.0;
		if(transaction.getOrderAmount() > loyaltyConfiguration.getSlab1()) {
			Double limitForSlab1 = Math.min(loyaltyConfiguration.getSlab2(), transaction.getOrderAmount());
			Double applicableAmountForSlab1 = limitForSlab1 - loyaltyConfiguration.getSlab1();
			points = points + applicableAmountForSlab1 * loyaltyConfiguration.getRateSlab1();
			if(transaction.getOrderAmount() > loyaltyConfiguration.getSlab2()) {
				Double applicableAmountForSlab2 = transaction.getOrderAmount() - loyaltyConfiguration.getSlab2();
				points = points + applicableAmountForSlab2 * loyaltyConfiguration.getRateSlab2();
			}
		}
		return points;
	}

}
