package com.loyalty.service;

import org.springframework.stereotype.Service;

import com.loyalty.dao.Transactions;

@Service
public interface TransactionService {
	Transactions storeTransaction(Transactions requestBody) throws Exception;
}
