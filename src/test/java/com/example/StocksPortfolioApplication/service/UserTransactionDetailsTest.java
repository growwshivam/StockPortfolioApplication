package com.example.StocksPortfolioApplication.service;


import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.model.UserTransactionDetails;
import com.example.StocksPortfolioApplication.repository.UserRepository;
import com.example.StocksPortfolioApplication.repository.UserTransactionDetailsRepositroy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTransactionDetailsTest {

    @Mock
    private UserTransactionDetailsRepositroy userTransactionDetailsRepositroy;

    @Mock
    private StockService stockService;

    @Mock
    private UserRepository user;
    @InjectMocks
    private UserTransactionDetailsServiceImpl userTransactionDetailsServiceImpl;

    @Test
    public void testTradingBuyingApiService_WhenInputIsValid() throws RuntimeException {

        Stock stock = new Stock(50002, 11.23, 17.42, 28.90, 10.45, 19, "ABC", 12.45);

        when(stockService.getStockById(500002)).thenReturn(Optional.of(stock));

        User createUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        TransactionType transactionType = TransactionType.BUY;

        Integer quantity = 15;

        String result = userTransactionDetailsServiceImpl.tradeApiService(createUser, 500002, quantity, transactionType);

        ArgumentCaptor<UserTransactionDetails> arguments = ArgumentCaptor.forClass(UserTransactionDetails.class);

        verify(userTransactionDetailsRepositroy).save(arguments.capture());

        Assertions.assertEquals(500002, arguments.getValue().getStockId());

        Assertions.assertEquals(15, arguments.getValue().getQuantity());


    }

    @Test
    public void testTradingBuyingApiService_WhenStockInputIsNotValid() throws RuntimeException {

        when(stockService.getStockById(500004)).thenThrow(new MockitoException("No Such Stock exists in the table So transaction not possible"));

        User createUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        try {
            String result2 = userTransactionDetailsServiceImpl.tradeApiService(createUser, 500004, 18, TransactionType.BUY);
        } catch (RuntimeException E) {
            Assertions.assertEquals("No Such Stock exists in the table So transaction not possible", E.getMessage());
        }
    }

    @Test
    public void testTradingBuyingApiService_WhenInputIsNotValid() throws RuntimeException {
        Stock stock = new Stock(50002, 11.23, 17.42, 28.90, 10.45, 19, "ABC", 12.45);

        when(stockService.getStockById(500002)).thenReturn(Optional.of(stock));

        TransactionType transactionType = TransactionType.BUY;

        Integer quantity = 1500;

        User createUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        try {
            String result3 = userTransactionDetailsServiceImpl.tradeApiService(createUser, 500002, quantity, transactionType);
        } catch (RuntimeException E) {
            Assertions.assertEquals("Insufficient Balance to done transaction", E.getMessage());
        }
    }

    @Test
    public void testTradingSellingService_WhenInputIsValid() throws RuntimeException {

        Stock stock = new Stock(500002, 11.23, 17.42, 28.90, 10.45, 19, "ABC", 12.45);

        when(stockService.getStockById(500002)).thenReturn(Optional.of(stock));


        User createUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        createUser.setUserAccountId(4);
        TransactionType transactionType = TransactionType.SELL;

        Integer quantity = 15;

        Integer mn = 0;

        Integer buyQuantity = 25;

        when(userTransactionDetailsRepositroy.getTotalQuantity(createUser.getUserAccountId(), stock.getStockId(), TransactionType.SELL)).thenReturn(Optional.of(mn));

        when(userTransactionDetailsRepositroy.getTotalQuantity(createUser.getUserAccountId(), stock.getStockId(), TransactionType.BUY)).thenReturn(Optional.of(buyQuantity));

        String result = userTransactionDetailsServiceImpl.tradeApiService(createUser, 500002, quantity, transactionType);

        ArgumentCaptor<UserTransactionDetails> arguments = ArgumentCaptor.forClass(UserTransactionDetails.class);

        verify(userTransactionDetailsRepositroy).save(arguments.capture());

        Assertions.assertEquals(500002, arguments.getValue().getStockId());

        Assertions.assertEquals(15, arguments.getValue().getQuantity());


    }

    @Test
    public void testTradingSellingService_WhenInputIsNotValid() throws RuntimeException {

        Stock stock = new Stock(50002, 11.23, 17.42, 28.90, 10.45, 19, "ABC", 12.45);

        when(stockService.getStockById(50002)).thenReturn(Optional.of(stock));


        User createUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        createUser.setUserAccountId(4);

        TransactionType transactionType = TransactionType.SELL;

        Integer quantity = 15;

        Integer sellQuantity = 11;

        Integer buyQuantity = 25;

        when(userTransactionDetailsRepositroy.getTotalQuantity(createUser.getUserAccountId(), stock.getStockId(), TransactionType.SELL)).thenReturn(Optional.of(sellQuantity));

        when(userTransactionDetailsRepositroy.getTotalQuantity(createUser.getUserAccountId(), stock.getStockId(), TransactionType.BUY)).thenReturn(Optional.of(buyQuantity));

        try {
            String result = userTransactionDetailsServiceImpl.tradeApiService(createUser, 50002, 15, transactionType);

        } catch (RuntimeException E) {
            Assertions.assertEquals(E.getMessage(), "Selling transaction not possible as no of stocks to sell is more than current stocks present in user portfolio");
        }

    }

    @Test
    public void testPortfolioOfUserApi_whenUserHasDoneTrading() throws RuntimeException {

        List<Integer> listOfStocksIds = new ArrayList<>(Arrays.asList(50002, 50004));

        when(userTransactionDetailsRepositroy.getAllStocks(2)).thenReturn(listOfStocksIds);

        List<Stock> listOfStocks = new ArrayList<>();

        Stock stockOne = new Stock(50002, 11.23, 34.56, 56.78, 9.78, 1200, "HDFC", 39.80);
        Stock stockTwo = new Stock(50004, 15.67, 190.89, 256.90, 15.67, 1000, "Estee", 134.67);

        when(stockService.getStockById(50002)).thenReturn(Optional.of(stockOne));

        when(stockService.getStockById(50004)).thenReturn(Optional.of(stockTwo));

        Integer totalBuyingQuantityOfOneStock = 100;

        Integer totalSellingQuantityOfOneStock = 50;

        Double totalBuyingTransactionOfOneStock = 2825.00;

        Double totalSellingTransactionOfOneStock = 1375.00;

        when(userTransactionDetailsRepositroy.getTotalQuantity(2, 50002, TransactionType.BUY)).thenReturn(Optional.of(totalBuyingQuantityOfOneStock));

        when(userTransactionDetailsRepositroy.getTotalTransaction(2, 50002, TransactionType.BUY)).thenReturn(Optional.of(totalBuyingTransactionOfOneStock));

        when(userTransactionDetailsRepositroy.getTotalQuantity(2, 50002, TransactionType.SELL)).thenReturn(Optional.of(totalSellingQuantityOfOneStock));

        when(userTransactionDetailsRepositroy.getTotalTransaction(2, 50002, TransactionType.SELL)).thenReturn(Optional.of(totalSellingTransactionOfOneStock));

        Integer totalBuyingQuantityOfTwoStock = 200;

        Integer totalSellingQuantityOfTwoStock = 125;

        Double totalBuyingTransactionOfTwoStock = 19000.25;

        Double totalSellingTransactionOfTwoStock = 17000.78;


        when(userTransactionDetailsRepositroy.getTotalQuantity(2, 50004, TransactionType.BUY)).thenReturn(Optional.of(totalBuyingQuantityOfTwoStock));

        when(userTransactionDetailsRepositroy.getTotalTransaction(2, 50004, TransactionType.BUY)).thenReturn(Optional.of(totalBuyingTransactionOfTwoStock));

        when(userTransactionDetailsRepositroy.getTotalQuantity(2, 50004, TransactionType.SELL)).thenReturn(Optional.of(totalSellingQuantityOfTwoStock));

        when(userTransactionDetailsRepositroy.getTotalTransaction(2, 50004, TransactionType.SELL)).thenReturn(Optional.of(totalSellingTransactionOfTwoStock));


        UserPortfolioDetailsDto userPortfolioDetailsDto = userTransactionDetailsServiceImpl.getPortfolioOfUser(2);


        Assertions.assertEquals(userPortfolioDetailsDto.getProfitAndLoss(), 5088.123749999999);

        Assertions.assertEquals(userPortfolioDetailsDto.getTotalBuyPrice(), 21825.25);


    }

    @Test
    public void testPortfolioOfUserApi_WhenUserHasNotDoneTrading() throws RuntimeException {
        List<Integer> emptyStockList = new ArrayList<>();

        when(userTransactionDetailsRepositroy.getAllStocks(4)).thenReturn(emptyStockList);

        UserPortfolioDetailsDto userPortfolioDetailsDtoWithNoStock = userTransactionDetailsServiceImpl.getPortfolioOfUser(4);

        Assertions.assertEquals(userPortfolioDetailsDtoWithNoStock.getProfitAndLoss(), 0.00);

        Assertions.assertEquals(userPortfolioDetailsDtoWithNoStock.getProfitAndLossPercentage(), 0.00);

        Assertions.assertEquals(userPortfolioDetailsDtoWithNoStock.getTotalBuyPrice(), 0.00);

        Assertions.assertEquals(userPortfolioDetailsDtoWithNoStock.getHoldings().size(), 0);
    }
}
