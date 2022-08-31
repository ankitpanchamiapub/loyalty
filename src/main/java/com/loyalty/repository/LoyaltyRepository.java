package com.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loyalty.dao.LoyaltyPoints;

@Repository
public interface LoyaltyRepository extends JpaRepository<LoyaltyPoints, Long> {
	public LoyaltyPoints findByCustomerId(Long customerId);
}
