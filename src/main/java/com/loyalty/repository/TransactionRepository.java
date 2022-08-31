package com.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loyalty.dao.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
	
}
