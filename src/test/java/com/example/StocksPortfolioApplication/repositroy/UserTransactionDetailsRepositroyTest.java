package com.example.StocksPortfolioApplication.repositroy;


import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.model.UserTransactionDetails;
import com.example.StocksPortfolioApplication.repository.UserTransactionDetailsRepositroy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase (connection = EmbeddedDatabaseConnection.H2)
public class UserTransactionDetailsRepositroyTest {

    @Autowired
    private UserTransactionDetailsRepositroy userTransactionDetailsRepositroy;
    @Autowired
    private TestEntityManager testEntityManager;

    private List<UserTransactionDetails> listOfUserTransactionDetails=new ArrayList<>();

    @BeforeEach
    public void setup() {
        UserTransactionDetails transaction1 = new UserTransactionDetails(1, 101,  LocalTime.now(), TransactionType.BUY, 10, 50.0);
        UserTransactionDetails transaction2 = new UserTransactionDetails(2, 102,  LocalTime.now(), TransactionType.SELL, 5, 55.5);
        UserTransactionDetails transaction3 = new UserTransactionDetails(3, 103,   LocalTime.now(), TransactionType.BUY, 8, 48.75);
        listOfUserTransactionDetails = Arrays.asList(transaction1, transaction2, transaction3);
        testEntityManager.persist(transaction1);
        testEntityManager.persist(transaction2);
        testEntityManager.persist(transaction3);
        testEntityManager.flush();
    }




    @Test
    public void test_findUserTransactionDetailsByUserAccountId() {

        List<UserTransactionDetails> transactions = userTransactionDetailsRepositroy.findUserTransactionDetailsByUserAccountId(1);


        assertEquals(1, transactions.size());
        assertEquals(listOfUserTransactionDetails.get(0), transactions.get(0));
    }

    @Test
    public void test_findUserTransactionDetailsByUserAccountIdAndStockIdAndTransactionType() {

        Optional<UserTransactionDetails> transaction = userTransactionDetailsRepositroy.findUserTransactionDetailsByUserAccountIdAndStockIdAndTransactionType(2, 102, TransactionType.SELL);


        assertTrue(transaction.isPresent());
        assertEquals(listOfUserTransactionDetails.get(1), transaction.get());
    }

    @Test
    public void test_getTotalQuantity() {

        Optional<Integer> totalQuantity = userTransactionDetailsRepositroy.getTotalQuantity(3, 103, TransactionType.BUY);


        assertTrue(totalQuantity.isPresent());
        assertEquals(8, totalQuantity.get().intValue());
    }

    @Test
    public void test_getTotalTransaction() {

        Optional<Double> totalTransaction = userTransactionDetailsRepositroy.getTotalTransaction(1, 101, TransactionType.BUY);


        assertTrue(totalTransaction.isPresent());
        assertEquals(500.0, totalTransaction.get(), 0.001);
    }


        @Test
        public void test_getTotalQuantity_WhenNewEntityAdd() {
            // Arrange
            UserTransactionDetails newTransaction = new UserTransactionDetails(4, 104, LocalTime.now(), TransactionType.BUY, 15, 60.0);

            // Act
            testEntityManager.persist(newTransaction);
            testEntityManager.flush();

            // Act again to check the total quantity after the new entity is added
            Optional<Integer> totalQuantity = userTransactionDetailsRepositroy.getTotalQuantity(4, 104, TransactionType.BUY);

            // Assert
            assertTrue(totalQuantity.isPresent());
            assertEquals(15, totalQuantity.get().intValue());
        }



    @Test
    public void test_getTotalTransaction_WhenNewEntityAdd() {
        // Arrange
        UserTransactionDetails newTransaction = new UserTransactionDetails(5, 105, LocalTime.now(), TransactionType.SELL, 5, 45.0);

        // Act
        testEntityManager.persist(newTransaction);
        testEntityManager.flush();

        Optional<Double> totalTransaction = userTransactionDetailsRepositroy.getTotalTransaction(5, 105, TransactionType.SELL);

        // Assert
        assertTrue(totalTransaction.isPresent());
        assertEquals(225.0, totalTransaction.get(), 0.001);
    }

    @Test
    public void test_getAllStocks() {

        List<Integer> allStocks = userTransactionDetailsRepositroy.getAllStocks(2);


        assertEquals(1, allStocks.size());
        assertEquals(102, allStocks.get(0).intValue());
    }
}

