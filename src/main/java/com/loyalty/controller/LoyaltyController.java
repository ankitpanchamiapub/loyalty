package com.loyalty.controller;

import com.loyalty.dao.LoyaltyPoints;
import com.loyalty.dao.Transactions;
import com.loyalty.dto.CustomerLoyaltyPointDto;
import com.loyalty.dto.ErrorResponse;
import com.loyalty.dto.RequestRecordTransaction;
import com.loyalty.service.LoyaltyService;
import com.loyalty.service.TransactionService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class LoyaltyController {
	
	private final LoyaltyService loyaltyService;
	private final TransactionService transactionService;
	private final ModelMapper modelMapper;

	public LoyaltyController(LoyaltyService loyaltyService, TransactionService transactionService, ModelMapper modelMapper) {
		this.loyaltyService = loyaltyService;
		this.transactionService = transactionService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/points/{customerId}")
	public ResponseEntity<Object> getLoyaltyPointsByCustomer(@PathVariable Long customerId){
		LoyaltyPoints loyaltyPoints = loyaltyService.getLoyaltyPointsByCustomerId(customerId);
		if(loyaltyPoints != null) {
			return new ResponseEntity<>(modelMapper.map(loyaltyPoints, CustomerLoyaltyPointDto.class), HttpStatus.OK);
		}else {
			log.warn("No records found");
			return new ResponseEntity<>(new ErrorResponse("No records found"), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/transaction")
	public ResponseEntity<RequestRecordTransaction> recordTransaction(@RequestBody RequestRecordTransaction requestBody) throws Exception {
		Transactions transactions = transactionService.storeTransaction(modelMapper.map(requestBody, Transactions.class));
		return new ResponseEntity<>(modelMapper.map(transactions, RequestRecordTransaction.class), HttpStatus.OK);
	}
}
