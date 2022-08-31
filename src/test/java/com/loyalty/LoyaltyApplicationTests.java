package com.loyalty;

import com.loyalty.dao.LoyaltyPoints;
import com.loyalty.dao.Transactions;
import com.loyalty.dto.ValidatorException;
import com.loyalty.repository.LoyaltyRepository;
import com.loyalty.repository.TransactionRepository;
import com.loyalty.service.LoyaltyService;
import com.loyalty.service.impl.LoyaltyServiceImpl;
import com.loyalty.service.impl.TransactionServiceImpl;
import com.loyalty.util.LoyaltyConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoyaltyApplicationTests {

    @Spy
    LoyaltyRepository loyaltyRepository;
    @Spy
    TransactionRepository transactionRepository;
    @Spy
    LoyaltyPoints loyaltyPoints;

    @Spy
    LoyaltyConfiguration loyaltyConfiguration;

    @InjectMocks
    LoyaltyServiceImpl loyaltyServiceImpl;

    @Spy
    LoyaltyService loyaltyService;

    @InjectMocks
    TransactionServiceImpl transactionService;

    private AutoCloseable openMocks;

    Transactions dummyTransaction;

    @BeforeEach
    public void beforeEach() {
        when(loyaltyRepository.findByCustomerId(99L)).thenReturn(null);
        dummyTransaction = mock(Transactions.class);
        when(dummyTransaction.getCustomerId()).thenReturn(10L);
        when(dummyTransaction.getOrderAmount()).thenReturn(120.0);
        when(transactionRepository.save(any(Transactions.class))).thenReturn(dummyTransaction);
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void afterEach() throws Exception {
        openMocks.close();
    }

    @Test
    void getPointsForCustomer_doesNotExistMock() {
        LoyaltyPoints responseLoyaltyPoints = loyaltyServiceImpl.getLoyaltyPointsByCustomerId(99L);
        assertNull(responseLoyaltyPoints);
    }

    @Test
    void getPointsForCustomer_existsMock() {
        when(loyaltyPoints.getCustomerId()).thenReturn(1L);
        when(loyaltyPoints.getPoints()).thenReturn(120.00);
        when(loyaltyRepository.findByCustomerId(1L)).thenReturn(loyaltyPoints);
        LoyaltyPoints responseLoyaltyPoints = loyaltyServiceImpl.getLoyaltyPointsByCustomerId(1L);
        assertEquals(1L, responseLoyaltyPoints.getCustomerId());
        assertEquals(120.0, responseLoyaltyPoints.getPoints());
    }


    @Test
    void createTransaction_Mock() throws Exception { //Failing now
        dummyTransaction = new Transactions();
        dummyTransaction.setCustomerId(1L);
        dummyTransaction.setOrderAmount(120.0);
        dummyTransaction.setOrderId("10");
        when(loyaltyConfiguration.getRateSlab1()).thenReturn(1L);
        when(loyaltyConfiguration.getRateSlab2()).thenReturn(2L);
        when(loyaltyConfiguration.getSlab1()).thenReturn(50L);
        when(loyaltyConfiguration.getSlab2()).thenReturn(100L);
        when(loyaltyPoints.getPoints()).thenReturn(90.0);
        when(loyaltyService.findOrCreateLoyaltyPoint(dummyTransaction)).thenReturn(loyaltyPoints);
        when(loyaltyRepository.findByCustomerId(1L)).thenReturn(loyaltyPoints);
        when(transactionRepository.save(any(Transactions.class))).thenReturn(dummyTransaction);
        Transactions storedTransaction = transactionService.storeTransaction(dummyTransaction);
        assertEquals(120.0, storedTransaction.getOrderAmount());
        assertEquals(90.0, storedTransaction.getPoints());
    }

    @Test
    void validationTest_Mock() {
        Transactions mockTransaction = mock(Transactions.class);
        when(mockTransaction.getCustomerId()).thenReturn(null);
        try {
            transactionService.storeTransaction(mockTransaction);
        } catch (Exception e) {
            assertInstanceOf(ValidatorException.class, e);
            assertEquals("Customer ID cannot be null", e.getMessage());
        }
        when(mockTransaction.getCustomerId()).thenReturn(1L);
        when(mockTransaction.getOrderAmount()).thenReturn(null);
        try {
            transactionService.storeTransaction(mockTransaction);
        } catch (Exception e) {
            assertInstanceOf(ValidatorException.class, e);
            assertEquals("Invalid purchase amount", e.getMessage());
        }
    }

}
