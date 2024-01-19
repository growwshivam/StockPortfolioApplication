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
import org.junit.jupiter.api.function.Executable;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTransactionDetailsTest {

    @Spy
    private UserTransactionDetailsRepositroy userTransactionDetailsRepositroy;

    @Spy
    private StockService stockService;

    @Spy
    private UserRepository user;
    @InjectMocks
    private UserTransactionDetailsServiceImpl userTransactionDetailsServiceImpl;

    @Test
    public void test_tradeApiService_Buying_WhenInputIsValid() throws RuntimeException {

        Stock stock = new Stock(50002, 11.23, 17.42, 28.90, 10.45, 19, "ABC", 12.45);

        when(stockService.findStockById(500002)).thenReturn(Optional.of(stock));

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
    public void test_tradeApiService_Buying_WhenStockInputIsNotValid() throws RuntimeException {

        when(stockService.findStockById(500004)).thenThrow(new MockitoException("Transaction not possible as no such stock exists"));

        User createUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            String result = userTransactionDetailsServiceImpl.tradeApiService(createUser, 500004, 30, TransactionType.BUY);
        });
        assertEquals(runtimeException.getMessage(), "Transaction not possible as no such stock exists");
    }

    @Test
    public void test_tradeApiService_Buying_WhenInputIsNotValid() throws RuntimeException {
        Stock stock = new Stock(50002, 11.23, 17.42, 28.90, 10.45, 19, "ABC", 12.45);

        when(stockService.findStockById(500002)).thenReturn(Optional.of(stock));

        TransactionType transactionType = TransactionType.BUY;

        Integer quantity = 1500;

        User createUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        assertEquals(assertThrows(RuntimeException.class, () -> {
            String result = userTransactionDetailsServiceImpl.tradeApiService(createUser, 500002, 50, TransactionType.BUY);
        }).getMessage(), "Insufficient Balance to done transaction");

    }

    @Test
    public void test_tradeApiService_Selling_WhenInputIsValid() throws RuntimeException {

        Stock stock = new Stock(500002, 11.23, 17.42, 28.90, 10.45, 19, "ABC", 12.45);

        when(stockService.findStockById(500002)).thenReturn(Optional.of(stock));


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
    public void test_tradeApiService_Selling_WhenInputIsNotValid() throws RuntimeException {

        Stock stock = new Stock(50002, 11.23, 17.42, 28.90, 10.45, 19, "ABC", 12.45);

        when(stockService.findStockById(50002)).thenReturn(Optional.of(stock));


        User createUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        createUser.setUserAccountId(4);

        TransactionType transactionType = TransactionType.SELL;

        Integer quantity = 15;

        Integer sellQuantity = 11;

        Integer buyQuantity = 25;

        when(userTransactionDetailsRepositroy.getTotalQuantity(createUser.getUserAccountId(), stock.getStockId(), TransactionType.SELL)).thenReturn(Optional.of(sellQuantity));

        when(userTransactionDetailsRepositroy.getTotalQuantity(createUser.getUserAccountId(), stock.getStockId(), TransactionType.BUY)).thenReturn(Optional.of(buyQuantity));

        assertEquals(assertThrows(RuntimeException.class, () -> {
            String result = userTransactionDetailsServiceImpl.tradeApiService(createUser, 50002, quantity, TransactionType.SELL);
        }).getMessage(), "Selling transaction not possible as no of stocks to sell is more than current stocks present in user portfolio");


    }

    @Test
    public void test_getPortfolioOfUser_whenUserHasDoneTrading() throws RuntimeException {

        List<Integer> listOfStocksIds = new ArrayList<>(Arrays.asList(50002, 50004));

        when(userTransactionDetailsRepositroy.getAllStocks(2)).thenReturn(listOfStocksIds);

        List<Stock> listOfStocks = new ArrayList<>();

        Stock stockOne = new Stock(50002, 11.23, 34.56, 56.78, 9.78, 1200, "HDFC", 39.80);
        Stock stockTwo = new Stock(50004, 15.67, 190.89, 256.90, 15.67, 1000, "Estee", 134.67);

        when(stockService.findStockById(50002)).thenReturn(Optional.of(stockOne));

        when(stockService.findStockById(50004)).thenReturn(Optional.of(stockTwo));

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
    public void test_getPortfolioOfUser_WhenUserHasNotDoneTrading() throws RuntimeException {
        List<Integer> emptyStockList = new ArrayList<>();

        when(userTransactionDetailsRepositroy.getAllStocks(4)).thenReturn(emptyStockList);

        UserPortfolioDetailsDto userPortfolioDetailsDtoWithNoStock = userTransactionDetailsServiceImpl.getPortfolioOfUser(4);

        Assertions.assertEquals(userPortfolioDetailsDtoWithNoStock.getProfitAndLoss(), 0.00);

        Assertions.assertEquals(userPortfolioDetailsDtoWithNoStock.getProfitAndLossPercentage(), 0.00);

        Assertions.assertEquals(userPortfolioDetailsDtoWithNoStock.getTotalBuyPrice(), 0.00);

        Assertions.assertEquals(userPortfolioDetailsDtoWithNoStock.getHoldings().size(), 0);

    }

    @Test
    public void test_handleBuyingTransaction() throws RuntimeException {

        User currentUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        Stock currentStock = new Stock(4, 8.0, 12.0, 6.0, 9.2, 50, "ABC", 15.5);


        String result = userTransactionDetailsServiceImpl.handleBuyingTransaction(currentUser, 101, 20, TransactionType.BUY, currentStock);

        ArgumentCaptor<UserTransactionDetails> arguments = ArgumentCaptor.forClass(UserTransactionDetails.class);
        verify(userTransactionDetailsRepositroy, times(1)).save(arguments.capture());

        Assertions.assertEquals(arguments.getValue().getStockPrice(), 15.5);
        Assertions.assertEquals(arguments.getValue().getQuantity(), 20);
        Assertions.assertEquals(arguments.getValue().getTransactionType(), TransactionType.BUY);
        Assertions.assertEquals(result, "Transaction of buying of stocks has been created successfully");
    }


    @Test
    public void test_handleBuyingTransaction_WhenBalanceIsNotValid() throws RuntimeException {

        User currentUser = new User("shivam", "9717255304", "@gmail.com", 400.9);

        Stock currentStock = new Stock(4, 8.0, 12.0, 6.0, 9.2, 50, "ABC", 15.5);


        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            userTransactionDetailsServiceImpl.handleBuyingTransaction(currentUser, 101, 2000, TransactionType.BUY, currentStock);
        });

        assertEquals(runtimeException.getMessage(), "Insufficient Balance to done transaction");
    }


    @Test
    public void test_HandleSellingTransaction() throws RuntimeException {

        User currentUser = new User("shivam", "9717255304", "@gmail.com", 400.9);
        currentUser.setUserAccountId(1);

        Stock currentStock = new Stock(101, 8.0, 12.0, 6.0, 9.2, 50, "ABC", 15.5);
        Integer totalBuyQuantity = 25;
        Integer totalsellQuantity = 0;
        when(userTransactionDetailsRepositroy.getTotalQuantity(1, 101, TransactionType.BUY)).thenReturn(Optional.of(totalBuyQuantity));
        when(userTransactionDetailsRepositroy.getTotalQuantity(1, 101, TransactionType.SELL)).thenReturn(Optional.of(totalsellQuantity));

        ArgumentCaptor<UserTransactionDetails> arguments = ArgumentCaptor.forClass(UserTransactionDetails.class);

        String result = userTransactionDetailsServiceImpl.handleSellingTransaction(currentUser, 101, 20, TransactionType.SELL, currentStock);

        verify(userTransactionDetailsRepositroy, times(1)).save(arguments.capture());

        Assertions.assertEquals(arguments.getValue().getStockPrice(), 15.5);
        Assertions.assertEquals(arguments.getValue().getQuantity(), 20);
        Assertions.assertEquals(arguments.getValue().getTransactionType(), TransactionType.SELL);

        Assertions.assertEquals(result, "Transaction of selling of stocks has been created successfully");
    }

    @Test
    public void test_HandleSellingTransaction_WhenQuantityIsNotValid() throws RuntimeException {
        User currentUser = new User("shivam", "9717255304", "@gmail.com", 400.9);
        currentUser.setUserAccountId(1);

        Stock currentStock = new Stock(101, 8.0, 12.0, 6.0, 9.2, 50, "ABC", 15.5);
        Integer totalBuyQuantity = 25;
        Integer totalsellQuantity = 20;
        when(userTransactionDetailsRepositroy.getTotalQuantity(1, 101, TransactionType.BUY)).thenReturn(Optional.of(totalBuyQuantity));
        when(userTransactionDetailsRepositroy.getTotalQuantity(1, 101, TransactionType.SELL)).thenReturn(Optional.of(totalsellQuantity));


        assertEquals(assertThrows(RuntimeException.class, () -> {
            userTransactionDetailsServiceImpl.handleSellingTransaction(currentUser, 101, 20, TransactionType.SELL, currentStock);
        }).getMessage(), "Selling transaction not possible as no of stocks to sell is more than current stocks present in user portfolio");

    }
}
