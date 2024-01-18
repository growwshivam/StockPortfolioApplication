package com.example.StocksPortfolioApplication.service;


import com.example.StocksPortfolioApplication.dto.InputUserDto;
import com.example.StocksPortfolioApplication.dto.StockDetailsDto;
import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserTransactionDetailsService userTransactionDetailsService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;


    @BeforeEach
    public void setUp() {
        List<User> listOfUsers = new ArrayList<>();
        for (Integer i = 0; i < 5; i++) {
            listOfUsers.add(new User("shivam", "9717255304", "@gmail.com", i * 100.0));
        }

    }

    @Test
    public void testUserCreateApi() throws RuntimeException {
        InputUserDto createToBeCreate = new InputUserDto("9717255304", "SHIVAM", "@GMAIL.COM", 345.67);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        String result = userServiceImpl.addUser(createToBeCreate);
        verify(userRepository).save(userArgumentCaptor.capture());
        System.out.println(userArgumentCaptor.getAllValues());
        Assertions.assertEquals(userArgumentCaptor.getValue().getName(), "SHIVAM");
        Assertions.assertEquals(userArgumentCaptor.getValue().getPhoneNumber(), "9717255304");
        Assertions.assertEquals(result, "User has been added successfully to the table");
    }


    @Test
    public void testGetUserApi_WhenInputIsValid() throws RuntimeException {
        Integer idToBeCheck = 2;
        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);
        when(userRepository.findUserByUserAccountId(2)).thenReturn(Optional.of(currentUser));
        Optional<User> resultOfQuery = userServiceImpl.getUserById(2);
        verify(userRepository, times(1)).findUserByUserAccountId(2);
        Assertions.assertEquals(resultOfQuery.get().getName(), "Shivam");
        Assertions.assertEquals(resultOfQuery.get().getBalance(), 567.89);

    }

    @Test
    public void testGetUserApi_WhenInputIsNotValid() throws RuntimeException {

        when(userRepository.findUserByUserAccountId(6)).thenThrow(new MockitoException("No Such user exists in the table"));
        try {
            Optional<User> resultOfQuery = userServiceImpl.getUserById(6);
        } catch (RuntimeException E) {
            Assertions.assertEquals(E.getMessage(), "No Such user exists in the table");
        }

    }


    @Test
    public void testDeleteUserApi_WhenInputIsValid() throws RuntimeException {
        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);
        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));
        doNothing().when(userRepository).deleteUserByUserAccountId(4);
        String resultOfDelete = userServiceImpl.deleteUserById(4);
        Assertions.assertEquals(resultOfDelete, "Deletion of User has been successfully completed");
    }

    @Test
    public void testDeleteUserApi_WhenInputIsNotValid() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(10)).thenThrow(new MockitoException("No Such User AccountId didnot exists in the user table So deletion is not possible"));
        try {
            String userToBeDeleted = userServiceImpl.deleteUserById(10);
        } catch (RuntimeException E) {
            Assertions.assertEquals(E.getMessage(), "No Such User AccountId didnot exists in the user table So deletion is not possible");
        }
    }

    @Test
    public void testTradingApi_WithInputIsValid_WhenBuyingHappens() throws RuntimeException {
        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);
        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));
        when(userTransactionDetailsService.tradeApiService(currentUser, 500002, 16, TransactionType.BUY)).thenReturn("Transaction of buying of stocks has been created successfully");
        currentUser.setBalance(currentUser.getBalance() - 16 * 2.34);
        when(userRepository.save(currentUser)).thenReturn(currentUser);
        String resultString = userServiceImpl.tradingApi(4, 500002, 16, TransactionType.BUY);
        Assertions.assertEquals(resultString, "Transaction of buying of stocks has been created successfully");

    }

    @Test
    public void testTradingApi_WithInputIsValid_WhenSellingHappens() throws RuntimeException {
        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);
        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));
        when(userTransactionDetailsService.tradeApiService(currentUser, 500002, 8, TransactionType.SELL)).thenReturn("Transaction of selling of stocks has been created successfully");
        currentUser.setBalance(currentUser.getBalance() + 8 * 3.45);
        when(userRepository.save(currentUser)).thenReturn(currentUser);
        String resultString = userServiceImpl.tradingApi(4, 500002, 8, TransactionType.SELL);
        Assertions.assertEquals(resultString, "Transaction of selling of stocks has been created successfully");
    }

    @Test
    public void testTradingApi_WhenUserIsNotValid_WhenBuyingHappens() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(8)).thenThrow(new MockitoException("No such user exists in the table"));
        try {
            String result = userServiceImpl.tradingApi(8, 500002, 15, TransactionType.BUY);
        } catch (RuntimeException E) {
            Assertions.assertEquals(E.getMessage(), "No such user exists in the table");
        }
    }

    @Test
    public void testTradingApi_WhenUserIsNotValid_WhenSellingHappens() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(8)).thenThrow(new MockitoException("No such user exists in the table"));
        try {
            String result = userServiceImpl.tradingApi(8, 500002, 15, TransactionType.BUY);
        } catch (RuntimeException E) {
            Assertions.assertEquals(E.getMessage(), "No such user exists in the table");
        }
    }

    @Test
    public void testTradingApi_WhenInputIsNotValid() throws RuntimeException {

        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);
        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));
        when(userTransactionDetailsService.tradeApiService(currentUser, 500002, 15, TransactionType.BUY)).thenThrow(new MockitoException("Insufficient Balance to done transaction"));
        try {
            String result = userServiceImpl.tradingApi(4, 500002, 15, TransactionType.BUY);
        } catch (RuntimeException E) {
            Assertions.assertEquals(E.getMessage(), "Insufficient Balance to done transaction");
        }
    }

    @Test
    public void testGetPortfolioOfUserApi_WhenInputIsValid() throws RuntimeException {
        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);
        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));
        List<StockDetailsDto> listOfStockDetails = new ArrayList<>();
        listOfStockDetails.add(new StockDetailsDto("HDFC", 500002, 18, 26.00, 34.00, 8.00));
        listOfStockDetails.add(new StockDetailsDto("ABC", 500004, 16, 23.00, 34.00, 11.00));

        UserPortfolioDetailsDto currentUserPortfolioDetailsDto = new UserPortfolioDetailsDto(listOfStockDetails, 4456.0, 4900.0, 456.0, 5.92);

        when(userTransactionDetailsService.getPortfolioOfUser(4)).thenReturn(currentUserPortfolioDetailsDto);

        UserPortfolioDetailsDto testUserPortfolioDetailsDto = userServiceImpl.getPortfolioOfUser(4);
        Assertions.assertEquals(testUserPortfolioDetailsDto.getTotalBuyPrice(), 4900.00);
        Assertions.assertEquals(testUserPortfolioDetailsDto.getProfitAndLoss(), 456.0);

    }

    @Test
    public void testGetPortfolioOfUserApi_WhenInputIsNotValid() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(14)).thenThrow(new MockitoException("No such user exists in the table"));
        try {

            UserPortfolioDetailsDto userPortfolioDetailsDto = userServiceImpl.getPortfolioOfUser(14);
        } catch (RuntimeException E) {

            Assertions.assertEquals(E.getMessage(), "No such user exists in the table");
        }
    }
}
