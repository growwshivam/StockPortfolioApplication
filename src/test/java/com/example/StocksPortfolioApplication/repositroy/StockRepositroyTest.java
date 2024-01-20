package com.example.StocksPortfolioApplication.repositroy;


import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.repository.StockRepositroy;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StockRepositroyTest {

    @Autowired
    private StockRepositroy stockRepositroy;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    public void setUp() {
        Stock stock1 = new Stock(50001, 10.5, 12.3, 9.8, 11.2, 150, "Stock_ABC", 11.8);
        Stock stock2 = new Stock(50002, 9.7, 11.8, 8.5, 10.2, 180, "Stock_XYZ", 10.5);
        Stock stock3 = new Stock(50003, 11.2, 13.5, 10.5, 12.8, 200, "Stock_PQR", 12.0);

        testEntityManager.persist(stock1);
        testEntityManager.persist(stock2);
        testEntityManager.persist(stock3);
    }

    @Test
    public void testFindAllReturnsListOfStock() throws SQLException {

        List<Stock> stocks = stockRepositroy.findAll();


        assertEquals(3, stocks.size());
    }

    @Test
    public void testFindStockByStockId() throws SQLException {

        Optional<Stock> foundStock = stockRepositroy.findStockByStockId(50001);


        assertTrue(foundStock.isPresent());
        assertEquals(10.5, foundStock.get().getOpenPrice());
    }

    @Test
    public void testFindStockByStockIdReturnsEmpty() throws SQLException {

        Optional<Stock> notFoundStock = stockRepositroy.findStockByStockId(407);


        assertTrue(notFoundStock.isEmpty());
    }

    @Test
    public void testDeleteStockByStockId() throws SQLException {

        stockRepositroy.deleteStockByStockId(50002);


        assertTrue(stockRepositroy.findStockByStockId(50002).isEmpty());
    }

    @Test
    public void testFindStockByName() throws SQLException {

        Optional<Stock> foundStock = stockRepositroy.findStockByName("Stock_XYZ");


        assertTrue(foundStock.isPresent());
        assertEquals(8.5, foundStock.get().getHighPrice());
    }
}

